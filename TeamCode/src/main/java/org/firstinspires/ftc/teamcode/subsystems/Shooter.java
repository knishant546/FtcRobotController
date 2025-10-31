package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
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

    public Command startShooter = new SetPower(shootermotor,pow);

    public Command stopShooter = new SetPower(shootermotor,0.0);
}
