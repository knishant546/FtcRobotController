package org.firstinspires.ftc.teamcode.localization;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.teamcode.drivers.GoBildaPinpointDriver;

public class PinpointLocalizer {
    private final GoBildaPinpointDriver odo;

    // Flip signs here if field axes come out reversed.
    private static final int X_SIGN = +1;   // +x forward
    private static final int Y_SIGN = +1;   // +y left
    private static final int H_SIGN = +1;   // CCW+ heading

    public PinpointLocalizer(HardwareMap hw) {
        odo = hw.get(GoBildaPinpointDriver.class, "odo"); // RC config name
        // Optional: one-time device setup
        // odo.resetPosAndIMU();
        // odo.setMmPerTick(...); odo.setOffsets(...);
    }

    /** Call every loop before getPose(). */
    public void update() { odo.update(); }

    /** Return Pedro Pose (inches + radians). */
    public Pose getPose() {
        Pose2D p = odo.getPosition(); // mm+radians in the driver
        double x = X_SIGN * p.getX(DistanceUnit.INCH);
        double y = Y_SIGN * p.getY(DistanceUnit.INCH);
        double h = H_SIGN * p.getHeading(AngleUnit.RADIANS);
        return new Pose(x, y, h);
    }

    /** Keep Pinpoint and Pedro in sync if you seed a nonzero start. */
    public void setPose(Pose start) {
        // If your driver supports it, mirror the pose into Pinpoint too:
        // odo.setPosition(new Pose2D(DistanceUnit.INCH, start.x, start.y, AngleUnit.RADIANS, start.heading));
    }
}
