package org.firstinspires.ftc.teamcode.TestBench;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Utils;
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

    private void printCurrentRGBA() {

        float[] rgba = Utils.getRGBA(colorSensor);

        telemetry.addData("Red", rgba[0]);
        telemetry.addData("Green", rgba[1]);
        telemetry.addData("Blue", rgba[2]);
        telemetry.addData("Alpha", rgba[3]);
        telemetry.update();
    }

    @Override
    public void onUpdate() {
        //printCurrentRGBA();
        //detect color and print the name
        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        telemetry.addData("Color Detected:", color);
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {



    }
}
