package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.CommandGroup;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="SpinnerSensorShooterTestBench")
public class SpinnerSensorShooterTestBench extends NextFTCOpMode {

    private NormalizedColorSensor colorSensor;

    public SpinnerSensorShooterTestBench() {
        addComponents(
                new SubsystemComponent(Shooter.getInstance(),
                        Spinner.getInstance(),
                        Shooter.getInstance(),
                        Lift.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE);
    }

    @Override
    public void onInit() {
        //Set up the colorSensor
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color_distance");
        colorSensor.setGain(10);
        Lift.getInstance().initialize();
    }

    private ParallelGroup _cgrp;

    @Override
    public void onUpdate() {

        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        telemetry.addData("Color Detected:",color);
        telemetry.addData("Shooter Power",
                Spinner.getInstance().getSpinnerPower());
        if(!color.equals("Nothing")) {
            telemetry.addData("I am here", "Jan");

            Command stopCommand = Spinner.getInstance().stopSpinner();
            stopCommand.schedule();
            if(stopCommand.isDone() && (_cgrp == null || _cgrp.isDone())) {
                if(_cgrp == null) {
                    _cgrp = new ParallelGroup(Spinner.getInstance().startSpinner(),
                            Lift.getInstance().liftUp()
                    );
                    _cgrp.schedule();
                }

                if(_cgrp.isDone()) {
                    Lift.getInstance().liftDown().schedule();
                    _cgrp = null;
                }
            }
        } else {
            telemetry.addData("I am leaving", "Jan");
            Spinner.getInstance().startSpinner().schedule();
        }

        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {

        Spinner.getInstance().startSpinner().schedule();
        Lift.getInstance().liftDown().schedule();
    }
}
