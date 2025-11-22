package org.firstinspires.ftc.teamcode.Auton;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.ShooterNew;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="BlueFront V3")
public class BlueFront extends NextFTCOpMode {

    Pose startPoseStraight = new Pose(0, 9, Math.toRadians(0));
    Pose adjustPoseToShoot = new Pose(11, 4, Math.toRadians(22));

    Pose moveToPickRow = new Pose(29, 18.6, Math.toRadians(90));

    Pose moveToPick2Balls = new Pose(29, 53.5, Math.toRadians(90));

    Pose moveToPickSecondRow = new Pose(54, 18, Math.toRadians(90));

    Pose moveToPick2SecondBalls = new Pose(54, 53.5, Math.toRadians(90));
    Pose adjustOut = new Pose(26, 7.5, Math.toRadians(22));

    private final double maxPower = 0.8;

    private VoltageSensor batteryVoltageSensor;

    public BlueFront() {
        addComponents(
                new SubsystemComponent(
                        Intake.getInstance(),
                        Spinner.getInstance(),
                        Lift.getInstance(),
                        ShooterNew.getInstance()
                ),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE
        );

    }

    private Command buildMoveCommand(Pose fromPosition,Pose toPosition, double maximumPower) {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(fromPosition, toPosition))
                .setLinearHeadingInterpolation(fromPosition.getHeading(), toPosition.getHeading())
                .build();
        return new FollowPath(move, false, maximumPower);
    }

    private Command shoot() {
        return Lift.getInstance().LiftUpDown()
                .then(Spinner.getInstance().startSpinner())
                .then(Intake.getInstance().startIntake)
                .then(new Delay(0.5))
                .then(Lift.getInstance().LiftUpDown()
                .then(new Delay(0.5))
                .then(Lift.getInstance().LiftUpDown())
                .then(Spinner.getInstance().stopSpinner()));
    }

    private Command stopAll() {
        return new SequentialGroup(
                Intake.getInstance().stopIntake,
                ShooterNew.getInstance().stopShooter(),
                Spinner.getInstance().stopSpinner()
        );
    }

    @Override
    public void onStop() {
        stopAll().schedule();
    }

    private Command autonomousRoutine() {
        Spinner.getInstance().stopColorSensor();
        follower().setStartingPose(startPoseStraight);
        follower().setPose(startPoseStraight);
        ShooterNew.getInstance().setShooterPowerFactor(0.8);
        ShooterNew.getInstance().increasePower.schedule();
        Spinner.getInstance().setPower(-0.8);
        return new SequentialGroup(
                buildMoveCommand(startPoseStraight,adjustPoseToShoot,0.6),
                shoot(),
                buildMoveCommand(adjustPoseToShoot,moveToPickRow,this.maxPower),
                buildMoveCommand(moveToPickRow,moveToPick2Balls,0.35),
                Intake.getInstance().stopIntake,
                ShooterNew.getInstance().startShooter(),
                buildMoveCommand(moveToPick2Balls,adjustPoseToShoot,this.maxPower),
                shoot(),
                buildMoveCommand(adjustPoseToShoot,moveToPickSecondRow,this.maxPower),
                buildMoveCommand(moveToPickSecondRow,moveToPick2SecondBalls,0.35),
                Intake.getInstance().stopIntake,
                buildMoveCommand(moveToPick2SecondBalls,adjustPoseToShoot,this.maxPower),
                shoot(),
                stopAll(),
                buildMoveCommand(adjustPoseToShoot,adjustOut,this.maxPower)
        );
    }

    @Override
    public void onInit() {
       follower().breakFollowing();
    }


    @Override
    public void onUpdate() {
        follower().update();
      /*  double voltage = batteryVoltageSensor.getVoltage();
        telemetry.addData("Battery Voltage", "%.2f V", voltage);
        telemetry.addData("x", follower().getPose().getX());
        telemetry.addData("y", follower().getPose().getY());
        telemetry.addData("heading", follower().getPose().getHeading());*/
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }
}
