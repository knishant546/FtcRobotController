package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="SpinnerSensorShooterTestBench:Auton")
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
        Spinner.getInstance().initialize();
        Shooter.getInstance().initialize();
    }

    private Command onColorDetectedBegin() {
        return new SequentialGroup(Spinner.getInstance().stopSpinner()
                .thenWait(0.5)
                .then(onStartShoot())
                .thenWait(0.5)
        );
    }

    private Command onStartSpinLift() {
        return new ParallelGroup(Spinner.getInstance().startSpinner(),
                Lift.getInstance().liftUp()
        );
    }

    private Command onStartShoot() {
        return new SequentialGroup(
                Shooter.getInstance().startShooter(),
                new Delay(1.0),
                onStartSpinLift(),
                new Delay(1.0),
                Lift.getInstance().liftDown(),
                Shooter.getInstance().stopShooter()
        );
    }

    private boolean schedule = false;
//    @Override
//    public void onUpdate() {
//
//        float[] rgba = Utils.getRGBA(colorSensor);
//        String color = Utils.getColorName(rgba);
//
//        telemetry.addData("Color Detected:",color);
//        telemetry.addData("Shooter Power",
//                Spinner.getInstance().getSpinnerPower());
//        if(!color.equals("Nothing") && !doOnce) {
//            telemetry.addData("I am here", "Jan");
//
//            Command stopCommand = Spinner.getInstance().stopSpinner();
//            stopCommand.schedule();
//            if(stopCommand.isDone() && (_sgrp == null || _sgrp.isDone())) {
//                if(_sgrp == null) {
//                    _cgrp = new ParallelGroup(Spinner.getInstance().startSpinner(),
//                            Lift.getInstance().liftUp()
//                            );
//
//                    _sgrp = new SequentialGroup(
//                            Shooter.getInstance().startShooter(),
//                            new Delay(1.0),
//                            _cgrp,
//                            new Delay(1.0),
//                            Lift.getInstance().liftDown(),
//                            new Delay(1.0),
//                            Shooter.getInstance().stopShooter()
//                    );
//
//                    _sgrp.schedule();
//                    telemetry.addData("scheduled seq grp", "Jan");
//                }
//
//                if(_sgrp.isDone()) {
//                    _sgrp = null;
//                    doOnce = true;
//                    telemetry.addData("seqGrp Done", "Jan");
//                }
//            }
//        } else {
//            telemetry.addData("I am leaving", "Jan");
//            Spinner.getInstance().startSpinner().schedule();
//        }
//
//        telemetry.update();
//    }

    @Override
    public void onUpdate() {

        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        telemetry.addData("Color Detected:",color);
        telemetry.addData("Shooter Power",
                Spinner.getInstance().getSpinnerPower());
        if(!color.equals("Nothing") && !schedule) {
            telemetry.addData("Object Detected", "By Sensor");
            new SequentialGroup(
                    this.onColorDetectedBegin(),
                    this.onStartShoot()
            ).schedule();
            schedule = true;

            telemetry.addData("scheduled seq grp", "By Jan");
        } else {
            telemetry.addData("I am leaving", "Jan");
            Spinner.getInstance().startSpinner().schedule();
            schedule = false;
        }

        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {

        Spinner.getInstance().startSpinner().schedule();
        Lift.getInstance().liftDown().schedule();
    }
}
