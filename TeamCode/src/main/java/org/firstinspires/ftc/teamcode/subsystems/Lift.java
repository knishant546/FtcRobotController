package org.firstinspires.ftc.teamcode.subsystems;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.ftc.ActiveOpMode;
import kotlin.Pair;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;

import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.positionable.SetPositions;
import dev.nextftc.hardware.powerable.SetPower;

public class Lift implements Subsystem {

    private static final Lift INSTANCE = new Lift();
    public static Lift getInstance() {
        return INSTANCE;
    }

    public final ServoEx leftServo = new ServoEx("leftServo");
    public final ServoEx rightServo = new ServoEx("rightServo");

    @Override
    public void initialize() {
        leftServo.getServo().resetDeviceConfigurationForOpMode();
        rightServo.getServo().resetDeviceConfigurationForOpMode();
        leftServo.getServo().setDirection(Servo.Direction.REVERSE);
        this.liftDown().schedule();
       // ActiveOpMode.telemetry().addData("Lift Initialize Completed  :");
    }

    public Command liftUp() {
        return new SetPositions(
                new Pair<> (leftServo, 1.0),
                new Pair<> (rightServo, 1.0)
        );
    }

    public Command liftDown() {
        return new SetPositions(
                new Pair<> (leftServo, 0.55),
                new Pair<> (rightServo, 0.55)
        );
    }

    public Command LiftUpDown() {
        return new SequentialGroup(
                liftUp(),
                liftDown()
        );
    }

    public Double getLeftPosition() {
        return leftServo.getPosition();
    }

    public Double getRightPosition() {
        return rightServo.getPosition();
    }
}

