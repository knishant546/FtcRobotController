package org.firstinspires.ftc.teamcode.TestBench;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Utils;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="DriverTrainPositionView")
public class DriverTrainPositionView extends NextFTCOpMode {
    public DriverTrainPositionView() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        //Set up the colorSensor
        telemetry.update();
    }


    @Override
    public void onUpdate() {
        follower().update();
        Pose currentPoss = follower().getPose();
        telemetry.addData("Current X:",currentPoss.getX());
        telemetry.addData("Current Y:",currentPoss.getY());
        telemetry.addData("Current Heading",currentPoss.getHeading());
        telemetry.update();
    }




    @Override
    public void onStartButtonPressed() {
        Pose startPoseStraight = new Pose(0, 0, Math.toRadians(0));
        follower().setStartingPose(startPoseStraight);
        follower().setPose(startPoseStraight);
    }

}
