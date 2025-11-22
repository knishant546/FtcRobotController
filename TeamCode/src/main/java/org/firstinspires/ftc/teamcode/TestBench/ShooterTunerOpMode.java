package org.firstinspires.ftc.teamcode.TestBench;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

@Config
@TeleOp(name="ShooterTunerOpMode")
public class ShooterTunerOpMode extends LinearOpMode {

    // PIDF coefficients
    public static double kP = 0.00023;
    public static double kI = 0.000115;
    public static double kD = 0.000115;
    public static double kF = 0.000405;

    public static double TARGET_VELOCITY = 1500; // ticks/sec

    private DcMotor shooterMotor;

    // PIDF internal state
    private double integral = 0;
    private double lastError = 0;

    // For manual velocity calculation
    private int lastPosition = 0;
    private long lastTime = 0;

    private long startTime = System.nanoTime();

    @Override
    public void runOpMode() throws InterruptedException {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        shooterMotor = hardwareMap.get(DcMotor.class, "shooter");
        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lastPosition = shooterMotor.getCurrentPosition();
        lastTime = System.nanoTime();

        telemetry.addData("Status", "Initialized. Waiting for start...");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            long now = System.nanoTime();
            double dt = (now - lastTime) / 1e9; // seconds
            if (dt < 1){
                continue;
            }
            lastTime = now;
            // Get current position
            int currentPosition = shooterMotor.getCurrentPosition();

            // Calculate velocity in ticks/sec
            double currentVelocity = (currentPosition - lastPosition) / dt;
            lastPosition = currentPosition;

            // PIDF calculation
            double error = TARGET_VELOCITY - currentVelocity;
            integral += error * dt;
            double derivative = (error - lastError) / dt;
            lastError = error;

            double power = kP * error + kI * integral + kD * derivative + kF * TARGET_VELOCITY;

            // Clamp power between -1 and 1
            power = Math.max(-1.0, Math.min(1.0, power));

            shooterMotor.setPower(power);

            // Dashboard telemetry
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("TargetVelocity", TARGET_VELOCITY);
            packet.put("CurrentVelocity", currentVelocity);
            packet.put("MotorPower", power);
            packet.put("Time Spent", (now -startTime) /1e9 );
            packet.put("Error", error);
            packet.put("kP", kP);
            packet.put("kI", kI);
            packet.put("kD", kD);
            packet.put("kF", kF);
            dashboard.sendTelemetryPacket(packet);

            // Driver station telemetry
            telemetry.addData("Target", TARGET_VELOCITY);
            telemetry.addData("Velocity", currentVelocity);
            telemetry.addData("Power", power);
            telemetry.update();
        }
    }
}

