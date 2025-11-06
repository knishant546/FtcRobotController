package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import java.util.List;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="TeleopWithAprilTag_v2")
public class TeleopWithAprilTag_v1 extends NextFTCOpMode {
    private NormalizedColorSensor colorSensor;


    public TeleopWithAprilTag_v1() {
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
        Shooter.getInstance().setTelemetry(telemetry);
        Lift.getInstance().initialize();
        Spinner.getInstance().initialize();
        Shooter.getInstance().initialize();
        Intake.getInstance().initialize();
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
        AprilTag.getInstance(limelight);
    }



    private Command onColorDetectedBegin = new InstantCommand(() -> {
        new Delay(0.4).then(
                Spinner.getInstance().stopSpinner()).schedule();
    }).requires(this);

    private Command onLifted = new InstantCommand( () -> {
        Lift.getInstance().LiftUpDown().then(Spinner.getInstance().startSpinner()).schedule();
    }).requires(this);

    @Override
    public void onUpdate() {

        float[] rgba = Utils.getRGBA(colorSensor);
        String color = Utils.getColorName(rgba);

        telemetry.addData("Color Detected:", color);
        telemetry.addData("Spinner Power",
                Spinner.getInstance().getSpinnerPower());
        telemetry.addData("Shooter Power", Shooter.getInstance().getShooterPower());


        if(!color.equals("Nothing") ) {
            telemetry.addData("Object Detected", "By Sensor");
            onColorDetectedBegin.thenWait(0.2).requires(this).schedule();
            telemetry.addData("scheduled seq grp", "By Jan");
            telemetry.addData("Spinner", "Stopped");
        }
        else {
            Spinner.getInstance().startSpinner().schedule();
        }

        telemetry.addData("AprilTag Distance:",AprilTag.getInstance().getAprilTagDistance());

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
                .whenBecomesTrue(Shooter.getInstance().startShooterWithAprilTag());

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
