package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="FTCDecodeTeleopChallengeTwo")
public class FTCDecodeTeleopChallengeTwo extends NextFTCOpMode {
    private NormalizedColorSensor colorSensor;

    public FTCDecodeTeleopChallengeTwo() {
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

    private Command onColorDetectedBegin = new InstantCommand(() -> {
        new Delay(0.2).then(
                Spinner.getInstance().stopSpinner()).schedule();
    }).requires(this);

    private Command onLifted = new InstantCommand( () -> {
        Lift.getInstance().LiftUpDown().then(Spinner.getInstance().startSpinner()).schedule();
    }).requires(this);

    @Override
    public void onUpdate() {

        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        boolean ball_found = false;
        if(color.equals("green") || color.equals("purple"))
            ball_found = true;

        if(ball_found) {
            telemetry.addData("Object Detected", "By Sensor");
            onColorDetectedBegin.thenWait(0.2).requires(this).schedule();
            telemetry.addData("scheduled seq grp", "By Jan");
            telemetry.addData("Spinner", "Stopped");

            telemetry.addData("Color Detected:", color);
            telemetry.addData("Spinner Power",
                    Spinner.getInstance().getSpinnerPower());
            telemetry.addData("Shooter Power", Shooter.getInstance().getShooterPower());
        }
        else {
            new InstantCommand(
                    () -> {
                        Spinner.getInstance().startSpinner().schedule();
                    }
            ).requires(this).schedule();

        }

        telemetry.update();
    }


    @Override
    public void onStartButtonPressed() {

        DriveTrain.getInstance().startDrive.schedule();
        Spinner.getInstance().startSpinner().schedule();
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Intake.getInstance().startIntake);
        Gamepads.gamepad1().dpadDown()
                .whenBecomesTrue(Intake.getInstance().stopIntake);

        Gamepads.gamepad2().y()
                .whenBecomesTrue(Shooter.getInstance().startShooter());

        Gamepads.gamepad2().a()
                .whenBecomesTrue(Shooter.getInstance().stopShooter());

        Gamepads.gamepad2().b()
                .whenBecomesTrue(this.onLifted);

        Gamepads.gamepad2().x()
                .whenBecomesTrue(Lift.getInstance().liftDown());

        Gamepads.gamepad2().dpadLeft()
                .whenBecomesTrue(Spinner.getInstance().startSpinner());

        Gamepads.gamepad2().dpadRight()
                .whenBecomesTrue(Spinner.getInstance().stopSpinner());

    }



}
