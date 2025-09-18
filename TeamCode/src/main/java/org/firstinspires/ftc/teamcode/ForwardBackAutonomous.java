package org.firstinspires.ftc.teamcode;

//import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;


import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "ForwardBackAutonomous with Pedro 1")
public class ForwardBackAutonomous  extends NextFTCOpMode {

    Pose startPose =  new Pose(60, 100, Math.toRadians(90));

    Pose endPose = new Pose(60, 0, Math.toRadians(90));
    PathChain MovePreload;

    public void populatePaths() {
        MovePreload = follower().pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                        .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
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
                new FollowPath(MovePreload)
        );
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }
}
