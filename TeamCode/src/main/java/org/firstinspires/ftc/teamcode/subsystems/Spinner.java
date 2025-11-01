package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

import dev.nextftc.hardware.powerable.SetPower;

public class Spinner implements Subsystem {

    private static final Spinner INSTANCE = new Spinner();
    public static Spinner getInstance() {
        return INSTANCE;
    }

    //TODO will change this name to pickMotor
    private MotorEx spinnerMotor = new MotorEx("spinner");

    private double pow = -0.6;

    private Spinner() {
    }
    public float getSpinnerPower() {
        return (float) spinnerMotor.getPower();
    }

    public Command startSpinner() {
        return new SetPower(spinnerMotor,pow);
    }

    public Command stopSpinner() {
        return new SetPower(spinnerMotor,0.0);
    }
}
