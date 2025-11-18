package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Utils;

import java.util.concurrent.atomic.AtomicBoolean;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

import dev.nextftc.hardware.powerable.SetPower;

public class Spinner implements Subsystem {

    private static final Spinner INSTANCE = new Spinner();
    private AtomicBoolean running =  new AtomicBoolean(false);
    public static Spinner getInstance() {
        return INSTANCE;
    }

    private NormalizedColorSensor colorSensor;

    //TODO will change this name to pickMotor
    private final MotorEx spinnerMotor = new MotorEx("spinner");

    private double pow = -0.65;

    private Spinner() {
    }
    public float getSpinnerPower() {
        return (float) spinnerMotor.getPower();
    }

    @Override
    public void initialize() {
        this.stopSpinner().schedule();
        setPower(-0.65);
    }

    public void setColorSensor(NormalizedColorSensor colorSensor){
        this.colorSensor = colorSensor;
    }

    @Override
    public void periodic() {
        if (colorSensor != null) {
            controlBasedColor();
        }
    }

    /**
     * Check the Color Sensor and if it detects the color , then stop the spinner
     * else start the spinner
     */
    private void controlBasedColor(){
        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.detectColorName(rgba);
        ActiveOpMode.telemetry().addData("Color Detected :",color);
        if(color.equals("Nothing")) {
          //  if (running.compareAndSet(false,true)) {
                startSpinner().schedule();
         //   }
        }else{
        //    if (running.compareAndSet(true,false)) {
                stopSpinner().schedule();
        //    }
        }
    }

    public void setPower(double pow) {
        this.pow = pow;
    }
    public Command startSpinner() {
        return new SetPower(spinnerMotor,pow);
    }

    public Command stopSpinner() {
        return new SetPower(spinnerMotor,0.0);
    }
}
