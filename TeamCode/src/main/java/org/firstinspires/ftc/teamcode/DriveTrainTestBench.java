package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

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
