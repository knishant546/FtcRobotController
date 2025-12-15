package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import dev.nextftc.ftc.ActiveOpMode;

public class ColorSensor implements Runnable{

    private final NormalizedColorSensor colorSensor;
    private boolean isDetected = false;

    private boolean shouldRun = false;

    public ColorSensor(NormalizedColorSensor colorSensor){
        this.colorSensor = colorSensor;
        this.shouldRun = false;
        init();
    }

    public void init() {
        this.isDetected = false;
        colorSensor.setGain(20);
    }

    public void startColorSensor(){
        this.shouldRun = true;
    }

    public void stopColorSensor(){
        this.shouldRun = false;
    }

    public boolean isDetected(){
        return this.isDetected;
    }

    public boolean isObjectDetected() {
        float alphaValue = colorSensor.getNormalizedColors().alpha;
        ActiveOpMode.telemetry().addData("Alpha value :",alphaValue);
        this.isDetected = (alphaValue >= 0.07);
        return this.isDetected;
    }



    @Override
    public void run() {
        while(shouldRun) {
            this.isDetected = isObjectDetected();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
