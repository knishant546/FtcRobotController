package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="ColorSensor Test")
public class ColorSensorTestBench extends NextFTCOpMode {

    public NormalizedColorSensor colorSensor;

    public ColorSensorTestBench() {
        telemetry.addData("Instr", "Colosensor Set up");

        addComponents(
                new SubsystemComponent(Spinner.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
                );
    }

    @Override
    public void onInit() {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color_distance");
        colorSensor.setGain(10);

        printCurrentRGBA();
    }

    private float[] getRGBA() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();


        float red = colors.red/colors.alpha;
        float green = colors.green/colors.alpha;
        float blue = colors.blue/colors.alpha;

        float [] rgba = new float[4];
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = colors.alpha;
        return rgba;
    }

    private void printCurrentRGBA() {

        float[] rgba = getRGBA();

        telemetry.addData("Red", rgba[0]);
        telemetry.addData("Green", rgba[1]);
        telemetry.addData("Blue", rgba[2]);
        telemetry.addData("Alpha", rgba[3]);
        telemetry.update();
    }

    /*
    10
    Nothing - 0.219, 0.3809, 0.333, 0.016
    Purple - 0.086, 0.106, 0.1646, 0.1233
    green - 0.067,0.2313,0.1752, 0.06

     */
    private String getColorName(float[] rgba) {
        float threshold = 0.2f;
        if(rgba[0] > threshold && rgba[1] > threshold && rgba[2] > threshold) {
            return "Nothing";
        } else if (rgba[2] > rgba[0] && rgba[2] > rgba[1]) {
            return "Purple";
        } else if (rgba[1] > threshold && rgba[0] <= threshold && rgba[2] <= threshold) {
            return "Green";
        } else {
            return "Unknown";
        }
    }

    @Override
    public void onUpdate() {
        //printCurrentRGBA();
        //detect color and print the name
        float[] rgba = getRGBA();
        String color = getColorName(rgba);

        telemetry.addData("Color Detected:", color);
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {



    }
}
