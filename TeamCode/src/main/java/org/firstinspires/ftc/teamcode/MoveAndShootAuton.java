package org.firstinspires.ftc.teamcode;

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
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="MoveAndShootAuton")
public class MoveAndShootAuton extends NextFTCOpMode {


    //Pose startPose = new Pose( 20/multiplier, 20/multiplier, Math.toRadians(0));
    //
    //Pose endPose = new Pose(30/multiplier, 10/multiplier, Math.toRadians(0));

    Pose startPose = new Pose(0, 0, Math.toRadians(0));
    Pose endPose = new Pose(getUnits(24), 0, Math.toRadians(0));

    //1 unit = 2.333 inches

    private double getUnits(double inches) {
        return inches * 0.5;
    }

    public MoveAndShootAuton() {
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

    private Command moveToScore() {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                //.setConstantHeadingInterpolation(endPose.getHeading())
                .build();
        return new FollowPath(move, true, 1.0);
    }

    private Command autonomousRoutine() {
        follower().setStartingPose(startPose);
        return new SequentialGroup(
                moveToScore()
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
