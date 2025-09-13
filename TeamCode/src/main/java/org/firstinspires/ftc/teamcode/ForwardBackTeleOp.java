package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "Sample: Forward Back TeleOp")
public class ForwardBackTeleOp extends NextFTCOpMode {

    public ForwardBackTeleOp() {
        addComponents(
                new SubsystemComponent(MecanumDriveSubsystem.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        MecanumDriveSubsystem.getInstance().drive();
    }

}