package org.firstinspires.ftc.teamcode.subsystems;

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
    public static Shooter getInstance() {
        return INSTANCE;
    }

    //TODO will change this name to pickMotor
    private MotorEx shootermotor = new MotorEx("shooter");

    private double pow = -1.0;

    private Shooter() {
    }

    public Command startShooter() {
        return new SetPower(shootermotor,pow);
    }

    public Command stopShooter() {
        return new SetPower(shootermotor,0.0);
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
