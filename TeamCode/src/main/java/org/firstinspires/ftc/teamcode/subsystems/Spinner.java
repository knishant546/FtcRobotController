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

    public void setPower(double power) {
        pow = power;
    }

    public Command startSpinner = new SetPower(spinnerMotor,pow);

    public Command stopSpinner = new SetPower(spinnerMotor,0.0);
}
