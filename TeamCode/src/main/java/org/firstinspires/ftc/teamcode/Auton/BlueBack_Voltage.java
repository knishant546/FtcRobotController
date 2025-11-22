package org.firstinspires.ftc.teamcode.Auton;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Utils;
import org.firstinspires.ftc.teamcode.VoltageRecord;
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

@Autonomous(name="BlueBack V3")
public class BlueBack_Voltage extends NextFTCOpMode {

    Pose startPoseStraight = new Pose(1.5, -32.4, Math.toRadians(180));
    Pose adjustPoseToShoot = new Pose(46, -9, Math.toRadians(220));

    Pose moveToPickRow = new Pose(52, -15, Math.toRadians(270));

    Pose moveToPick2Balls = new Pose(52, -44, Math.toRadians(270));

    Pose moveToPickSecondRow = new Pose(75, -17, Math.toRadians(270));

    Pose moveToPick2SecondBalls = new Pose(75, -52, Math.toRadians(270));
    Pose adjustOut = new Pose(63, -12, Math.toRadians(270));

    private final double maxPower = 0.9;

    private VoltageSensor batteryVoltageSensor;
    private VoltageRecord voltageRecord;

    public BlueBack_Voltage() {
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
        return new FollowPath(move, true, maximumPower);
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
       // Spinner.getInstance().startColorSensor();
        follower().setStartingPose(startPoseStraight);
        follower().setPose(startPoseStraight);
        ShooterNew.getInstance().setShooterPowerFactor(voltageRecord.getAutoShootCloserFactor());
        Spinner.getInstance().setPower(-0.8);
        return new SequentialGroup(
                ShooterNew.getInstance().startShooter(),
                buildMoveCommand(startPoseStraight,adjustPoseToShoot,this.maxPower),
                shoot(),
                buildMoveCommand(adjustPoseToShoot,moveToPickRow,this.maxPower),
                buildMoveCommand(moveToPickRow,moveToPick2Balls,voltageRecord.getAutoPickSpeed()),
                Intake.getInstance().stopIntake,
                ShooterNew.getInstance().startShooter(),
                buildMoveCommand(moveToPick2Balls,adjustPoseToShoot,this.maxPower),
                shoot(),
                buildMoveCommand(adjustPoseToShoot,moveToPickSecondRow,this.maxPower),
                buildMoveCommand(moveToPickSecondRow,moveToPick2SecondBalls,voltageRecord.getAutoPickSpeed()),
                Intake.getInstance().stopIntake,
                buildMoveCommand(moveToPick2SecondBalls,moveToPickSecondRow,this.maxPower),
                buildMoveCommand(moveToPickSecondRow,adjustPoseToShoot,this.maxPower),
                shoot(),
                stopAll(),
                buildMoveCommand(adjustPoseToShoot,adjustOut,this.maxPower)
        );
    }

    @Override
    public void onInit() {
        Utils.popuplateTable();
        voltageRecord = Utils.getVoltageRecord();
        follower().breakFollowing();
    }


    @Override
    public void onUpdate() {
        follower().update();
      //  telemetry.addData("x", follower().getPose().getX());
      //  telemetry.addData("y", follower().getPose().getY());
      //  telemetry.addData("heading", follower().getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }
}
