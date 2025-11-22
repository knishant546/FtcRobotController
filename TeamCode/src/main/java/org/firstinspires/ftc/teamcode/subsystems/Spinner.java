package org.firstinspires.ftc.teamcode.subsystems;

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
    public static Spinner getInstance() {
        return INSTANCE;
    }

    private final AtomicBoolean isRunnning = new AtomicBoolean(false);

    private NormalizedColorSensor colorSensor ;

    //TODO will change this name to pickMotor
    private final MotorEx spinnerMotor = new MotorEx("spinner");


    private double pow = -0.65;

    private double lastTime = System.nanoTime();

    private boolean startColorSensor;

    public void startColorSensor(){
        this.startColorSensor = true;
    }

    public void stopColorSensor() {
        this.startColorSensor = false;
    }

    private Spinner() {
    }
    public float getSpinnerPower() {
        return (float) spinnerMotor.getPower();
    }

    @Override
    public void initialize() {
        this.stopSpinner().schedule();
        setPower(-0.65);
        this.startColorSensor = false;
        lastTime = System.nanoTime();
    }

    public void initColorSensor() {
        colorSensor = ActiveOpMode.hardwareMap().get(NormalizedColorSensor.class,"sensor_color_distance");
        colorSensor.setGain(20);
    }

    @Override
    public void periodic() {
        if (this.startColorSensor && colorSensor != null) {
            double dt = (System.nanoTime() - lastTime) / 1e9;
            if (dt <= 1) {
                return;
            }
            lastTime = dt;
            controlBasedColor();
        }

    }

    /**
     * Check the Color Sensor and if it detects the Object , then stop the spinner
     * else start the spinner
     */
    private void controlBasedColor(){
        if(Utils.isObjectDetected(colorSensor)) {
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
