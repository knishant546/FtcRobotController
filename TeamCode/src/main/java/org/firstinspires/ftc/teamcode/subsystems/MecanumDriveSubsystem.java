package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

import dev.nextftc.ftc.Gamepads;


public class MecanumDriveSubsystem implements Subsystem {

    private static final MecanumDriveSubsystem INSTANCE = new MecanumDriveSubsystem();
    public static MecanumDriveSubsystem getInstance() {
        return INSTANCE;
    }

    private final MotorEx frontLeft;
    private final MotorEx frontRight;
    private final MotorEx backLeft;
    private final MotorEx backRight;

    private DriverControlledCommand mecanumDrive;

    private MecanumDriveSubsystem() {

        // Initialize your motors
        frontLeft = new MotorEx(hardwareMap.get(DcMotorEx.class, "frontleft"));
        frontRight = new MotorEx(hardwareMap.get(DcMotorEx.class, "frontright"));
        backLeft = new MotorEx(hardwareMap.get(DcMotorEx.class, "backleft"));
        backRight = new MotorEx(hardwareMap.get(DcMotorEx.class, "backright"));

        mecanumDrive = new MecanumDriverControlled(frontLeft, frontRight, backLeft, backRight,
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().rightStickX()
        );
        //        frontLeft = new MotorEx("frontleft");
//        frontRight = new MotorEx("frontright");
//        backLeft = new MotorEx("backleft");
//        backRight = new MotorEx("backright");
    }

    public void driveAutonomousForward(double power, double distance) {
        //Set the direction of the motors to move forward
        frontLeft.setDirection(1);
        frontRight.setDirection(-1);
        backLeft.setDirection(-1);
        backRight.setDirection(-1);
        /*
        1440 encoder counts per revolution means the motor's encoder generates 1440 distinct pulses (counts)
         for every full rotation of the motor shaft. This value is used to measure rotation and
         calculate distance traveled by the wheel. It is a common specification
         for certain DC motors with built-in encoders.
         */

        double countsPerInch = 1440.0 / (4 * Math.PI); // Example for 4-inch diameter wheels with 1440 counts per revolution
        int target = (int)(distance * countsPerInch);
        while (frontLeft.getCurrentPosition() < target &&
                frontRight.getCurrentPosition() < target &&
                backLeft.getCurrentPosition() < target &&
                backRight.getCurrentPosition() < target) {
            // Wait until the motors reach the target position
            // This loop will keep the motors running until they reach the target
            // You might want to add a timeout or other safety checks here
            // Optionally, you can add telemetry or logging here to monitor progress
            // Set motor powers to drive forward
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);
        }
    }

    public void driveAutonomousBackward(double power, double distance) {
        //Set the direction of the motors to move backward

    }


    public void drive() {
        mecanumDrive.schedule();
    }
}
