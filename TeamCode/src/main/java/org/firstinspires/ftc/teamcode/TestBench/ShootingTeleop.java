package org.firstinspires.ftc.teamcode.TestBench;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Utils;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "ShootingTeleop")

public class ShootingTeleop  extends NextFTCOpMode {

    private NormalizedColorSensor colorSensor;

    private boolean firstDetection = false;


    public ShootingTeleop() {
        addComponents(
                new SubsystemComponent(
                        Intake.getInstance(),
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
        Intake.getInstance().initialize();
    }

    private Command onColorDetectedBegin() {
        return new SequentialGroup(Spinner.getInstance().stopSpinner()
                .thenWait(0.5)
        );
    }

    @Override
    public void onUpdate() {

        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        telemetry.addData("Color Detected:", color);
        telemetry.addData("Spinner Power",
                Spinner.getInstance().getSpinnerPower());
        telemetry.addData("Shooter Power", Shooter.getInstance().getShooterPower());

        if(!color.equals("Nothing") && !firstDetection) {
            telemetry.addData("Object Detected", "By Sensor");
            new SequentialGroup(
                    this.onColorDetectedBegin(),
                    new Delay(0.5)
            ).schedule();
            telemetry.addData("scheduled seq grp", "By Jan");
            telemetry.addData("Spinner", "Stopped");
            firstDetection = true;
            telemetry.addData("firstDetection1", firstDetection);
        }

        if(!firstDetection) {
            telemetry.addData("Start Spinner", "Running");
            if(Spinner.getInstance().getSpinnerPower() == 0.0)
                Spinner.getInstance().startSpinner().schedule();
            telemetry.addData("firstDetection3", firstDetection);
        }

        telemetry.update();
    }

    private Command stopShooter() {
        telemetry.addData("Shooting Stopped", "By Jan");

        firstDetection = false;
        telemetry.addData("Shooting Stopped", "By Jan");
        telemetry.addData("firstDetection2", firstDetection);
        telemetry.update();

        return new SequentialGroup(
                Shooter.getInstance().stopShooter(),
                new Delay(0.5)
        ).requires(this);
    }

    private Command startwithoutDelayShooter() {
        firstDetection = true;
        if(Spinner.getInstance().getSpinnerPower() != -0.6) {
            return new SequentialGroup(
                    Shooter.getInstance().startShooter(),
                    new Delay(1.0)
                    //Spinner.getInstance().startSpinner()
            );
        }

        return Shooter.getInstance().startShooter();
    }

    @Override
    public void onStartButtonPressed() {

        Spinner.getInstance().startSpinner().schedule();
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Intake.getInstance().startIntake);
        Gamepads.gamepad1().dpadDown()
                .whenBecomesTrue(Intake.getInstance().stopIntake);

        Gamepads.gamepad1().y()
                .whenBecomesTrue(this.startwithoutDelayShooter());

        Gamepads.gamepad1().a()
                .whenBecomesTrue(this.stopShooter());

        Gamepads.gamepad1().b()
                .whenBecomesTrue(Lift.getInstance().liftUp());

        Gamepads.gamepad1().x()
                .whenBecomesTrue(Lift.getInstance().liftDown());

        Gamepads.gamepad1().dpadLeft()
                .whenBecomesTrue(Spinner.getInstance().startSpinner());

        Gamepads.gamepad1().dpadRight()
                .whenBecomesTrue(Spinner.getInstance().stopSpinner());

    }


}
