package org.firstinspires.ftc.teamcode.opmodes;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.vision.AprilTagHelper;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

import java.util.List;

@Autonomous(name = "POC: Pedro + Pinpoint + AprilTag")
public class AutoPathWithTags extends NextFTCOpMode {
    enum PathChoice { A, B, C }
    private AprilTagHelper tags;
    private PathChoice choice = PathChoice.A;
    private PathChain chainA, chainB, chainC;

    @Override public void defineComponents() {
        addComponents(
                new BulkReadComponent(),
                new SubsystemComponent(new MecanumDriveSubsystem(hardwareMap)),
                new PedroComponent(Constants::createFollower),
                new BindingsComponent()
        );
    }

    @Override public void onInit() {
        Pose start = Constants.START_POSE();
        follower.setPose(start);

        // Define 3 simple demo paths
        Pose A1 = new Pose(24, 0, 0); // forward
        chainA = follower.pathBuilder()
                .addPath(new BezierLine(start, A1))
                .setLinearHeadingInterpolation(start.getHeading(), A1.getHeading())
                .build();

        Pose B1 = new Pose(0, 24, Math.toRadians(90)); // left
        chainB = follower.pathBuilder()
                .addPath(new BezierLine(start, B1))
                .setLinearHeadingInterpolation(start.getHeading(), B1.getHeading())
                .build();

        Pose C1 = new Pose(-24, 0, Math.toRadians(180)); // backward
        chainC = follower.pathBuilder()
                .addPath(new BezierLine(start, C1))
                .setLinearHeadingInterpolation(start.getHeading(), C1.getHeading())
                .build();

        // Start AprilTag camera
        tags = new AprilTagHelper(hardwareMap);
        telemetry.addLine("Scanning tags… show Tag #1/#2 to pick path A/B, else path C.");
    }

    @Override public void onInitLoop() {
        List<AprilTagDetection> dets = tags.detections();
        AprilTagDetection best = null;
        for (AprilTagDetection d : dets) {
            if (d.metadata != null && d.pose != null && d.poseAmbiguity < 0.20) {
                best = d; break;
            }
        }
        if (best != null) {
            if (best.id == 1) choice = PathChoice.A;
            else if (best.id == 2) choice = PathChoice.B;
            else choice = PathChoice.C;

            telemetry.addData("Tag", "#%d amb=%.2f", best.id, best.poseAmbiguity);

            // Optional: if SDK gives robot pose, you can sync odometry here:
            // Pose2D robotField = best.ftcPose; // check your SDK
            // follower.setPose(new Pose(robotField.getX(INCH), robotField.getY(INCH), robotField.getHeading(RADIANS)));
        }
        telemetry.addData("Detections", dets.size());
        telemetry.update();
    }

    private Command routine() {
        switch (choice) {
            case A: return new SequentialGroup(new FollowPath(chainA));
            case B: return new SequentialGroup(new FollowPath(chainB));
            default: return new SequentialGroup(new FollowPath(chainC));
        }
    }

    @Override public void onStartButtonPressed() { routine().schedule(); }

    @Override public void onStop() {
        if (tags != null) tags.close();
    }
}

