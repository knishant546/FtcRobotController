package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Utils;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterNew;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="FTCDecodeTeleopChallengeFour")
public class FTCDecodeTeleopChallengeFour extends NextFTCOpMode {

    private VoltageSensor batteryVoltageSensor;

    boolean ignoreColor = false;

    public FTCDecodeTeleopChallengeFour() {
        addComponents(
                new SubsystemComponent(
                        Intake.getInstance(),
                        Spinner.getInstance(),
                        ShooterNew.getInstance(),
                        Lift.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE);
    }

    @Override
    public void onInit() {
        batteryVoltageSensor = hardwareMap.voltageSensor.get("Control Hub");
    }



    private Command onLifted = new InstantCommand( () -> {
        Lift.getInstance().LiftUpDown().then(Spinner.getInstance().startSpinner()).schedule();
    }).requires(this);

    @Override
    public void onUpdate() {
        // Get the current voltage
        double voltage = batteryVoltageSensor.getVoltage();
        telemetry.addData("Battery Voltage", "%.2f V", voltage);
        telemetry.addData("Shooter Power Variable :",ShooterNew.getInstance().getShooterPowerFactor());
        telemetry.addData("Spinner Power",
                Spinner.getInstance().getSpinnerPower());
        telemetry.addData("Shooter Power", ShooterNew.getInstance().getShooterPower());
        telemetry.update();
    }

    private Command manualIntake = new InstantCommand(()->{

        ignoreColor = true;

        Spinner.getInstance().startSpinner().schedule();

    }).requires(ignoreColor);

    private Command stopmanualIntake = new InstantCommand(()->{
        ignoreColor = false;
        Spinner.getInstance().stopSpinner().schedule();

    }).requires(ignoreColor);



    @Override
    public void onStartButtonPressed() {

        DriveTrain.getInstance().startDrive.schedule();
        Spinner.getInstance().startSpinner().schedule();
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Intake.getInstance().startIntake);
        Gamepads.gamepad1().dpadDown()
                .whenBecomesTrue(Intake.getInstance().stopIntake);

        Gamepads.gamepad1().y()
                .whenBecomesTrue(manualIntake)
                .whenBecomesFalse(stopmanualIntake);


        Gamepads.gamepad2().y()
                .whenBecomesTrue(ShooterNew.getInstance().startShooter());

        Gamepads.gamepad2().a()
                .whenBecomesTrue(ShooterNew.getInstance().stopShooter());

        Gamepads.gamepad2().b()
                .whenBecomesTrue(this.onLifted);

        Gamepads.gamepad2().x()
                .whenBecomesTrue(Lift.getInstance().liftDown());

        Gamepads.gamepad2().dpadUp()
                .whenBecomesTrue(ShooterNew.getInstance().increasePower);
        Gamepads.gamepad2().dpadDown()
                .whenBecomesTrue(ShooterNew.getInstance().decreasePower);


        Gamepads.gamepad2().dpadLeft()
                .whenBecomesTrue(Spinner.getInstance().startSpinner());

        Gamepads.gamepad2().dpadRight()
                .whenBecomesTrue(Spinner.getInstance().stopSpinner());

    }



}
