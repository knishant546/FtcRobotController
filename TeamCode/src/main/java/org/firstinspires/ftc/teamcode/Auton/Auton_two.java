package org.firstinspires.ftc.teamcode.Auton;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="BlueBack V1")
public class Auton_two extends NextFTCOpMode {

    //Pose initPose = new Pose(getUnits(16), getUnits(-41), Math.toRadians(230));

    Pose startPoseStraight = new Pose(getUnits(16), getUnits(-40), Math.toRadians(230));
    Pose adjustPoseToShoot = new Pose(getUnits(72), getUnits(-6), Math.toRadians(210));

    Pose moveToPickRow = new Pose(56, -22, Math.toRadians(270));

    Pose moveToPick2Balls = new Pose(56, -45, Math.toRadians(270));

    Pose moveToPick3rdBall = new Pose(51, -40, Math.toRadians(260));

    Pose adjustOut = new Pose(56, -45, Math.toRadians(270));

    private double getUnits(double inches) {
        return inches * 1;
    }

    public Auton_two() {
        addComponents(
                new SubsystemComponent(
                        Intake.getInstance(),
                        Spinner.getInstance(),
                        Lift.getInstance(),
                        Shooter.getInstance()
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
        return new FollowPath(move, true, 0.7);
    }

    private Command moveToPickRowOne() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(adjustPoseToShoot, moveToPickRow))
                .setLinearHeadingInterpolation(adjustPoseToShoot.getHeading(), moveToPickRow.getHeading())
                .build();
        return new FollowPath(move, true, 0.7);
    }

    private Command moveToPickBalls() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPickRow, moveToPick2Balls))
                .setLinearHeadingInterpolation(moveToPickRow.getHeading(), moveToPick2Balls.getHeading())
                .addPath(new BezierLine(moveToPick2Balls, moveToPick3rdBall))
                .setLinearHeadingInterpolation(moveToPick2Balls.getHeading(), moveToPick3rdBall.getHeading())
                .build();
        return new FollowPath(move, true, 0.25);

    }

    private Command moveToOriginal() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(moveToPick3rdBall, adjustPoseToShoot))
                .setLinearHeadingInterpolation(moveToPick3rdBall.getHeading(), adjustPoseToShoot.getHeading())
                .build();
        return new FollowPath(move, true, 0.7);
    }

    private Command moveOut() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(adjustPoseToShoot, adjustOut))
                .setLinearHeadingInterpolation(adjustPoseToShoot.getHeading(), adjustOut.getHeading())
                .build();
        return new FollowPath(move, true, 0.7);
    }

    private Command shoot() {
        return Intake.getInstance().startIntake
                .then(Lift.getInstance().LiftUpDown())
                .then(new Delay(1))
                .then(Spinner.getInstance().startSpinner())
                .then(Lift.getInstance().LiftUpDown())
                .then(new Delay(1))
                .then(Spinner.getInstance().stopSpinner())
                .then(Lift.getInstance().LiftUpDown());
    }

    private Command stopAll() {
        return new SequentialGroup(
                Intake.getInstance().stopIntake,
                Shooter.getInstance().stopShooter(),
                Spinner.getInstance().stopSpinner()
        );
    }

    @Override
    public void onStop() {
        stopAll().schedule();
    }

    private Command autonomousRoutine() {
        follower().setStartingPose(startPoseStraight);
        return new SequentialGroup(
                Shooter.getInstance().startShooter(),
                moveToShoot(),
                new Delay(1.0),
                shoot(),
                moveToPickRowOne(),
                moveToPickBalls(),
                moveToOriginal(),
                shoot(),
                stopAll(),
                moveOut()
        );

//        return new SequentialGroup(
//                Intake.getInstance().startIntake,
//                Shooter.getInstance().startShooter(),
//                Spinner.getInstance().startSpinner(),
//                moveToScore(),
//                //moveToScore(),
//                Shooter.getInstance().startShooter(),
//                new Delay(1),
//                Intake.getInstance().startIntake,
//                Lift.getInstance().LiftUpDown(),
//                new Delay(0.5),
//                Spinner.getInstance().startSpinner(),
//                Lift.getInstance().LiftUpDown(),
//                Spinner.getInstance().stopSpinner(),
//                Lift.getInstance().LiftUpDown()
//        );

    }

    @Override
    public void onInit() {
        Intake.getInstance().initialize();
        Spinner.getInstance().initialize();
        Lift.getInstance().initialize();
        Shooter.getInstance().initialize();
    }

    @Override
    public void onUpdate() {
        follower().update();
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
