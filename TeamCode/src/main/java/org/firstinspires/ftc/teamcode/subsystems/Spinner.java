package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Sensors.ColorSensor;
import org.firstinspires.ftc.teamcode.Utils;

import java.util.concurrent.atomic.AtomicBoolean;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

import dev.nextftc.hardware.powerable.SetPower;

public class Spinner implements Subsystem {

    private static final Spinner INSTANCE = new Spinner();
    public static Spinner getInstance() {
        return INSTANCE;
    }

    private final AtomicBoolean isRunnning = new AtomicBoolean(false);


    //TODO will change this name to pickMotor
    private  MotorEx spinnerMotor = new MotorEx("spinner");


    private ColorSensor colorSensor ;

    private double pow = -0.65;

    private Spinner() {
    }

    public void setColorSensor(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    public float getSpinnerPower() {
        return (float) spinnerMotor.getPower();
    }

    @Override
    public void initialize() {
       // spinnerMotor =  ActiveOpMode.hardwareMap().get(DcMotor.class,"spinner");
        this.spinnerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.stopSpinner().schedule();
        setPower(-0.65);
    }


    @Override
    public void periodic() {
        if (colorSensor != null){
            controlBasedColor();
        }
    }

    /**
     * Check the Color Sensor and if it detects the Object , then stop the spinner
     * else start the spinner
     */
    private void controlBasedColor(){
        if (colorSensor.isObjectDetected()) {
            if (isRunnning.get()) {
               stopSpinner().schedule();
            }
        }else{
            if (!isRunnning.get()) {
               startSpinner().schedule();
            }
        }
    }

    public void setPower(double pow) {
        this.pow = pow;
    }

    public Command startSpinner() {
        isRunnning.set(true);
        return new SetPower(spinnerMotor,pow);
    }

    public Command stopSpinner() {
        isRunnning.set(false);
        return new SetPower(spinnerMotor,0.0);
    }
}
