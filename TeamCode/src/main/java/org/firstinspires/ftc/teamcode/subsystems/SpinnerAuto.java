package org.firstinspires.ftc.teamcode.subsystems;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Sensors.ColorSensor;

import java.util.concurrent.atomic.AtomicBoolean;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.powerable.SetPower;

public class SpinnerAuto implements Subsystem {

    private static final SpinnerAuto INSTANCE = new SpinnerAuto();
    public static SpinnerAuto getInstance() {
        return INSTANCE;
    }

    private final AtomicBoolean isRunnning = new AtomicBoolean(false);


    //TODO will change this name to pickMotor
    private  DcMotor spinnerMotor;


    private ColorSensor colorSensor ;

    private double pow = -0.65;

    private SpinnerAuto() {
    }

    public void setColorSensor(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    public float getSpinnerPower() {
        return (float) spinnerMotor.getPower();
    }

    @Override
    public void initialize() {
        spinnerMotor =  ActiveOpMode.hardwareMap().get(DcMotor.class,"spinner");
     //   this.stopSpinner().schedule();
        setPower(-0.65);
        isRunnning.set(false);
    }


    @Override
    public void periodic() {
        if (colorSensor != null ){
            controlBasedColor();
        }
    }

    /**
     * Check the Color Sensor and if it detects the Object , then stop the spinner
     * else start the spinner
     */
    private void controlBasedColor(){
        if (colorSensor.isDetected()) {
            if (isRunnning.get()) {
                isRunnning.set(false);
                spinnerMotor.setPower(0.0);
            }
        }else{
            if (!isRunnning.get()) {
                isRunnning.set(true);
                spinnerMotor.setPower(this.pow);
            }
        }
    }

    public void setPower(double pow) {
        this.pow = pow;
    }
}
