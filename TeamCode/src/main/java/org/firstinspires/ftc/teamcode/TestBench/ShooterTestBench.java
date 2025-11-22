package org.firstinspires.ftc.teamcode.TestBench;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;


@Config
@TeleOp(name="ShooterTestBench")
public class ShooterTestBench extends NextFTCOpMode {
    public  static double kP = 0.00023;
    public  static double kI = 0.00001;
    public  static double kD = 0.0;
    public  static double kF = 0.00037;

    public  static double max_goal = 2000;
    public PIDCoefficients pidCoefficients = new PIDCoefficients(kP , kI, kD);

    private  final ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(pidCoefficients)
            .basicFF(0)
            .build();

    private MotorEx motor = new MotorEx("intake");
    public ShooterTestBench(){
    }

    @Override
    public void onInit() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.zero();
        start.schedule();
    }

    @Override
    public void onUpdate() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        double powerTarget = controlSystem.calculate(motor.getState());
// Divide by the max velocity to normalize the power (e.g., this gives 0.5 for half speed)
        double scaledPower = powerTarget / (2795.52 * 60);

// IMPORTANT: Clamp the power value to the acceptable range [-1, 1]
        double motorPower = Math.max(0, Math.min(1.0, scaledPower));

        // Dashboard telemetry
        TelemetryPacket packet = new TelemetryPacket();
        packet.put("TargetVelocity", max_goal);
        packet.put("Motor Current State",motor.getState());
        packet.put("CurrentVelocity", motor.getVelocity());
        packet.put("Referece",controlSystem.getReference());
        packet.put("Raw Power Cal", powerTarget);
        packet.put("Scaled Power", scaledPower);
        packet.put("Cliped Power", scaledPower);
        packet.put("Goal", controlSystem.getGoal());
        packet.put("kP", kP);
        packet.put("kI", kI);
        packet.put("kD", kD);
        packet.put("kF", kF);
        dashboard.sendTelemetryPacket(packet);
        motor.setPower(motorPower);
    }

    public Command start = new InstantCommand(() -> {
        // Setting Goal by Wrapping....
        new RunToVelocity(controlSystem, max_goal).requires(this).schedule();
    }).requires(this);
}
