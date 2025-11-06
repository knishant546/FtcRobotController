package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AprilTag;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToState;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {

    private static final Shooter INSTANCE = new Shooter();
    Telemetry telemetry;
    private double pow = 1.0;
    public static Shooter getInstance() {
        return INSTANCE;
    }

    public void setTelemetry(Telemetry telemetry ) {
        this.telemetry = telemetry;
    }

    //TODO will change this name to pickMotor
    private MotorEx shootermotor = new MotorEx("shooter");

    private Shooter() {
    }

    @Override
    public void initialize() {
        this.stopShooter().schedule();
    }


    public float getShooterPower() {
        return (float) shootermotor.getPower();
    }

    public Command startShooter() {
        return new SetPower(shootermotor,this.pow);
    }

    /**
     * Use this to change the power Dynamically. Mostly with April Tag detection
     * @return
     */
    public Command startShooterWithAprilTag() {
        double distance = AprilTag.getInstance().getAprilTagDistance()/100;
        telemetry.addData("Power given:",distance);
        distance = distance > 1 ? 1 : distance;
        telemetry.addData("After Rounding Power given:",distance);
        telemetry.update();
        return new SetPower(shootermotor, distance);
    }


    public Command stopShooter() {
        double pow = 0.0;
        return new SetPower(shootermotor,pow);
    }

    /*
    private final ControlSystem controller = ControlSystem.builder()
            .velPid(0.005, 0, 0)
            .basicFF(0.01, 0.02, 0.03)
            .build();

    public final Command off = new RunToVelocity(controller, 0.0).requires(this);
    public final Command on = new RunToVelocity(controller, 500.0).requires(this);

    @Override
    public void periodic() {
        shootermotor.setPower(controller.calculate(shootermotor.getState()));
    }

     */
}
