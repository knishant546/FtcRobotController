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

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

@Autonomous(name = "POC: Pedro + Pinpoint (Forward & Back)")
public class PedroPinpointSimpleAuto extends NextFTCOpMode {
    private PathChain chain;

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

        Pose A = new Pose(24, 0, 0); // 24” forward
        chain = follower.pathBuilder()
                .addPath(new BezierLine(start, A))
                .setLinearHeadingInterpolation(start.getHeading(), A.getHeading())
                .addPath(new BezierLine(A, start))
                .setLinearHeadingInterpolation(A.getHeading(), start.getHeading())
                .build();

        telemetry.addLine("Ready. ▶ to run Pinpoint-powered Pedro path.");
        telemetry.update();
    }

    private Command routine() { return new SequentialGroup(new FollowPath(chain)); }

    @Override public void onStartButtonPressed() { routine().schedule(); }
}

