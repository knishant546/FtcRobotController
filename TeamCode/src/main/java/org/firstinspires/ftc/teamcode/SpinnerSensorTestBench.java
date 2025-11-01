package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="ShooterTestBench")
public class SpinnerSensorTestBench extends NextFTCOpMode {

    private NormalizedColorSensor colorSensor;

    public SpinnerSensorTestBench() {
        addComponents(
                new SubsystemComponent(Shooter.getInstance(),
                        Spinner.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE);
    }

    @Override
    public void onInit() {
        //Set up the colorSensor
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color_distance");
        colorSensor.setGain(10);

    }

    @Override
    public void onUpdate() {

        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        telemetry.addData("Color Detected:",color);
        telemetry.addData("Shooter Power",
                Spinner.getInstance().getSpinnerPower());
        if(!color.equals("Nothing")) {
            telemetry.addData("I am here", "Jan");
            Spinner.getInstance().stopSpinner().schedule();
            telemetry.update();
        } else {
            telemetry.addData("I am leaving", "Jan");
            Spinner.getInstance().startSpinner().schedule();
            telemetry.update();
        }

        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {


        Spinner.getInstance().startSpinner().schedule();

        String color = Utils.getColorName(
                Utils.getRGBA(colorSensor)
        );

    }
}
