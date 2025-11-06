package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

@TeleOp(name = "LiftServo Test")
public class LiftServoTestBench extends NextFTCOpMode {

    public LiftServoTestBench() {
        telemetry.addData("Instr", "Forward Backward Components adding");

        addComponents(
                new SubsystemComponent(
                        Lift.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        //
        telemetry.addData("LeftServo curr",Lift.getInstance().getLeftPosition());
        telemetry.addData("RightServo curr",Lift.getInstance().getRightPosition());

        Servo.Direction leftdir =  Lift.getInstance().leftServo.getServo().getDirection();
        Servo.Direction rightdir =  Lift.getInstance().rightServo.getServo().getDirection();


        telemetry.addData("leftDir:",leftdir.name());
        telemetry.addData("rightDir:",rightdir.name());

        Lift.getInstance().initialize();
        Lift.getInstance().liftDown();

        telemetry.addData("LeftServo Init pos",Lift.getInstance().getLeftPosition());
        telemetry.addData("RightServo Init pos",Lift.getInstance().getRightPosition());

        telemetry.update();
    }

    @Override
    public void onUpdate() {

        telemetry.addData("LeftServo", Lift.getInstance().getLeftPosition());
        telemetry.addData("RightServo", Lift.getInstance().getRightPosition());
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {

        Gamepads.gamepad1().b()
                .whenBecomesTrue(Lift.getInstance().liftUp());
        Gamepads.gamepad1().x()
                .whenBecomesTrue(Lift.getInstance().liftDown());

        telemetry.update();
    }
}
