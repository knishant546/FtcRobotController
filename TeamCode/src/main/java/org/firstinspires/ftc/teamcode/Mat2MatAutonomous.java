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

@Autonomous(name = "Mat2MatAutonomous with Pedro 1")
public class Mat2MatAutonomous  extends NextFTCOpMode {

    //45 inches back, 16 inches left
    //30 units = 45 inches i.e 10 units = 9 inches
    //10 units = 16 inches
    Pose startPose =  new Pose(0, 0, Math.toRadians(45));
    PathChain StrafeRight;
    PathChain MoveForward;

    PathChain MoveDiagonal;

    public void populatePaths() {
        //y positive forward, x positive move right
        //1/15 units = 35 inches
        Pose endPose = new Pose(15, 15, Math.toRadians(45));
        MoveForward = follower().pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();

        Pose afterRightStrafe = new Pose(15, 15, Math.toRadians(45));
//        StrafeRight = follower().pathBuilder()
//                .addPath(new BezierLine(endPose, afterRightStrafe))
//                .setLinearHeadingInterpolation(endPose.getHeading(), afterRightStrafe.getHeading())
//                .build();

        //generate the code for moving diagonally
//        Pose afterDiagonal = new Pose(30, 30);
//        MoveDiagonal = follower().pathBuilder()
//                .addPath(new BezierLine(endPose, afterRightStrafe))
//                .setLinearHeadingInterpolation(endPose.getHeading(), afterDiagonal.getHeading())
//                .build();
    }

    public Mat2MatAutonomous() {
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
                new FollowPath(MoveForward, true, 0.3)
//                ,
//                new FollowPath(StrafeRight, true, 0.3)
//                ,
//                new FollowPath(MoveDiagonal,true, 0.3)
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
