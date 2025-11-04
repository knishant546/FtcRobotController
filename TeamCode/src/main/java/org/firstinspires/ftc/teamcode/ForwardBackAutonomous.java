package org.firstinspires.ftc.teamcode;

//import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;


import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;

@Autonomous(name = "ForwardBackAutonomous with Pedro 1")
public class ForwardBackAutonomous  extends NextFTCOpMode {

    Pose startPose =  new Pose(60, 100, Math.toRadians(90));
    PathChain MoveBackward;
    PathChain StrafeLeft;
    PathChain MoveForward;
    PathChain StrafeRight;

    public void populatePaths() {
        Pose endPose = new Pose(60, 70, Math.toRadians(90));
        MoveBackward = follower().pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                        .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();

        Pose afterLeftStrafe = new Pose(50, 70, Math.toRadians(90));
        StrafeLeft = follower().pathBuilder()
                .addPath(new BezierLine(endPose, afterLeftStrafe))
                .setLinearHeadingInterpolation(endPose.getHeading(), afterLeftStrafe.getHeading())
                .build();


        Pose afterRightStrafe = new Pose(60, 70, Math.toRadians(90));
        StrafeRight = follower().pathBuilder()
                .addPath(new BezierLine(afterLeftStrafe, afterRightStrafe))
                .setLinearHeadingInterpolation(afterLeftStrafe.getHeading(), afterRightStrafe.getHeading())
                .build();

        Pose endForwardPose = new Pose(60, 100, Math.toRadians(90));
        MoveForward = follower().pathBuilder()
                .addPath(new BezierLine(afterRightStrafe, endForwardPose))
                .setLinearHeadingInterpolation(afterRightStrafe.getHeading(), endForwardPose.getHeading())
                .build();
    }

    public ForwardBackAutonomous() {
        addComponents(new PedroComponent(Constants::createFollower));
    }

    @Override
    public void onInit() {
        //These are 3 methods called, preInit, Init, and PostInit
        follower().setStartingPose(startPose);
        populatePaths();
    }

    private Command autonomousRoutine() {

        return new SequentialGroup(
                new FollowPath(MoveBackward, true, 0.5),
                new FollowPath(StrafeLeft, true, 0.5),
                new FollowPath(StrafeRight, true, 0.5),
                new FollowPath(MoveForward, true, 0.5)
        );
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }

    @Override
    public void onUpdate() {
        // These loop the movements of the robot, these must be called continuously in order to work
        //follower.update(); adding the follower to the PedroComponent takes care of this
        // Feedback to Driver Hub for debugging
        telemetry.addData("x", follower().getPose().getX());
        telemetry.addData("y", follower().getPose().getY());
        telemetry.addData("heading", follower().getPose().getHeading());
        telemetry.update();
    }
}
