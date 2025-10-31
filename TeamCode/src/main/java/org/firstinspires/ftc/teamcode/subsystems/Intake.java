package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements Subsystem {

    private static final Intake INSTANCE = new Intake();
    public static Intake getInstance() {
        return INSTANCE;
    }

    //TODO will change this name to pickMotor
    private MotorEx intakeMotor = new MotorEx("intake");

    private Intake() {
    }

    public Command startIntake = new SetPower(intakeMotor,-1.0);

    public Command stopIntake = new SetPower(intakeMotor,0.0);
}
