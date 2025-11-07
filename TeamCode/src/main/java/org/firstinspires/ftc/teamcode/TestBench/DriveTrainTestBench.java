package org.firstinspires.ftc.teamcode.TestBench;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "DriveTrainTestBench")

public class DriveTrainTestBench extends NextFTCOpMode {

    public DriveTrainTestBench() {
        addComponents(
                new SubsystemComponent(DriveTrain.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        //Set up the colorSensor
        DriveTrain.getInstance().initialize();
    }

    @Override
    public void onUpdate() {

        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {

        DriveTrain.getInstance().startDrive.schedule();
    }
}
