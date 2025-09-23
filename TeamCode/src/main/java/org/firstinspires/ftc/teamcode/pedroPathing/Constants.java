/*package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    //Mass must be in kg
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5);

    //This is used incase of drive motor encoders
    //See different type of localization here: https://pedropathing.com/docs/pathing/tuning/localization
    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName("frontright")
            .rightRearMotorName("backright")
            .leftRearMotorName("backleft")
            .leftFrontMotorName("frontleft")
            .leftFrontEncoderDirection(Encoder.FORWARD)
            .leftRearEncoderDirection(Encoder.FORWARD)
            .rightFrontEncoderDirection(Encoder.REVERSE)
            .rightRearEncoderDirection(Encoder.REVERSE)
            .robotLength(16) //Distance between left and right wheels
            .robotWidth(14); //Distance between front and back wheels
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontright")
            .rightRearMotorName("backright")
            .leftRearMotorName("backleft")
            .leftFrontMotorName("frontleft")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .driveEncoderLocalizer(localizerConstants)
                .build();
    }
}*/
package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.geometry.Pose;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.localization.PinpointLocalizer;

public class Constants {

    /** Create a Pedro follower and attach Pinpoint as the pose source. */
    public static Follower createFollower(HardwareMap hw) {
        // --- 1) Build follower ---
        // If your artifact exposes MecanumFollower(...) use that; otherwise Follower(hw) is available in 2.0.x.
        Follower follower = new Follower(hw);   // <— swap to new MecanumFollower(hw, …) if you prefer

        // --- 2) Attach pose supplier from Pinpoint ---
        PinpointLocalizer localizer = new PinpointLocalizer(hw);
        // Most 2.0.x builds expose one of these two; keep the one that compiles in your project:
        follower.setLocalizer(() -> { localizer.update(); return localizer.getPose(); });
        // OR:
        // follower.setExternalPoseSupplier(() -> { localizer.update(); return localizer.getPose(); });

        // (Optional) default start pose
        follower.setPose(new Pose(0, 0, 0));
        return follower;
    }

    public static Pose START_POSE() { return new Pose(0, 0, 0); }
}
