package org.firstinspires.ftc.teamcode.Auton;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
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

@Autonomous(name="BlueBack V2.1")
// @TODO Reset to center position
public class BlueBack extends NextFTCOpMode {

    Pose startPoseStraight = new Pose(1.5, -32.4, Math.toRadians(180));
    Pose adjustPoseToShoot = new Pose(46, -9, Math.toRadians(220));

    Pose moveToPickRow = new Pose(50, -15, Math.toRadians(270));

    Pose moveToPick2Balls = new Pose(50, -44, Math.toRadians(270));

    Pose moveToPickSecondRow = new Pose(73, -17, Math.toRadians(270));

    Pose moveToPick2SecondBalls = new Pose(73, -52, Math.toRadians(270));
    Pose adjustOut = new Pose(63, -12, Math.toRadians(270));

    private final double maxPower = 0.9;

    private VoltageSensor batteryVoltageSensor;

    public BlueBack() {
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

    private Command moveToShoot() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(startPoseStraight, adjustPoseToShoot))
                .setLinearHeadingInterpolation(startPoseStraight.getHeading(), adjustPoseToShoot.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }

    private Command moveToPickSecondRow() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(adjustPoseToShoot, moveToPickSecondRow))
                .setLinearHeadingInterpolation(adjustPoseToShoot.getHeading(), moveToPickSecondRow.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }

    private Command moveToPick2SecondRow() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPickSecondRow, moveToPick2SecondBalls))
                .setLinearHeadingInterpolation(moveToPickSecondRow.getHeading(), moveToPick2SecondBalls.getHeading())
                .build();
        return new FollowPath(move, true, 0.5);
    }

    private Command moveBackSecondRow() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPick2SecondBalls, moveToPickSecondRow))
                .setLinearHeadingInterpolation(moveToPick2SecondBalls.getHeading(), moveToPickSecondRow.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }

    private Command moveToOriginalTwo() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPickSecondRow, adjustPoseToShoot))
                .setLinearHeadingInterpolation(moveToPickSecondRow.getHeading(), adjustPoseToShoot.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }


    private Command moveToPickRowOne() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(adjustPoseToShoot, moveToPickRow))
                .setLinearHeadingInterpolation(adjustPoseToShoot.getHeading(), moveToPickRow.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }

    private Command moveToPickBalls() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPickRow, moveToPick2Balls))
                .setLinearHeadingInterpolation(moveToPickRow.getHeading(), moveToPick2Balls.getHeading())
                .build();
        return new FollowPath(move, true, 0.5);

    }

    private Command moveToOriginal() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPick2Balls, adjustPoseToShoot))
                .setLinearHeadingInterpolation(moveToPick2Balls.getHeading(), adjustPoseToShoot.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }

    private Command moveOut() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(adjustPoseToShoot, adjustOut))
                .setLinearHeadingInterpolation(adjustPoseToShoot.getHeading(), adjustOut.getHeading())
                .build();
        return new FollowPath(move, true, this.maxPower);
    }

    private Command shoot() {
        return Lift.getInstance().LiftUpDown()
                .then(Intake.getInstance().startIntake)
                .then(new Delay(0.5))
                .then(Lift.getInstance().LiftUpDown()
                .then(new Delay(0.5))
                .then(Lift.getInstance().LiftUpDown()));
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
        follower().setStartingPose(startPoseStraight);
        follower().setPose(startPoseStraight);
        ShooterNew.getInstance().setShooterPowerFactor(0.65);
        Spinner.getInstance().setPower(-0.8);
        return new SequentialGroup(
                ShooterNew.getInstance().startShooter(),
                moveToShoot(),
                shoot(),
            //    Shooter.getInstance().stopShooter(),
                moveToPickRowOne(),
                moveToPickBalls(),
                Intake.getInstance().stopIntake,
                ShooterNew.getInstance().startShooter(),
                moveToOriginal(),
                shoot(),
                moveToPickSecondRow(),
                moveToPick2SecondRow(),
                Intake.getInstance().stopIntake,
                moveBackSecondRow(),
          //      Shooter.getInstance().startShooter(),
                moveToOriginalTwo(),
                shoot(),
                stopAll(),
                moveOut()
        );
    }

    @Override
    public void onInit() {
        batteryVoltageSensor = hardwareMap.voltageSensor.get("Control Hub");
        NormalizedColorSensor colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color_distance");
        colorSensor.setGain(20);
        Intake.getInstance().initialize();
        Spinner.getInstance().initialize();
        Lift.getInstance().initialize();
        ShooterNew.getInstance().initialize();
    }


    @Override
    public void onUpdate() {
        follower().update();
        double voltage = batteryVoltageSensor.getVoltage();
        telemetry.addData("Battery Voltage", "%.2f V", voltage);
        telemetry.addData("x", follower().getPose().getX());
        telemetry.addData("y", follower().getPose().getY());
        telemetry.addData("heading", follower().getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }

}
