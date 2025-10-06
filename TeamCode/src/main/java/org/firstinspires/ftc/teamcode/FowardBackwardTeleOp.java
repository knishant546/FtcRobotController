package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Sample: Forward Backward TeleOp Without Components")
public class FowardBackwardTeleOp extends NextFTCOpMode {

    public  FowardBackwardTeleOp() {
        telemetry.addData("Instr", "Forward Backward Components adding");

        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    private final MotorEx frontLeftMotor = new MotorEx("frontleft");
    private final MotorEx frontRightMotor = new MotorEx("frontright").reversed();
    private final MotorEx backLeftMotor = new MotorEx("backleft");
    private final MotorEx backRightMotor = new MotorEx("backright").reversed();

    @Override
    public void onStartButtonPressed() {

        telemetry.addData("Instr", "Forward Backward OnStart Pressed");

        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
        driverControlled.schedule();
    }

}
