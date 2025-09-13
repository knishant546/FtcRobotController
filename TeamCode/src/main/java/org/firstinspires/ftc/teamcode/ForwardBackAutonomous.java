package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "ForwardBackAutonomous")
public class ForwardBackAutonomous  extends NextFTCOpMode {

    public ForwardBackAutonomous() {
        addComponents(
                new SubsystemComponent(MecanumDriveSubsystem.getInstance()),
                BulkReadComponent.INSTANCE
        );
    }

    private Command autonomousRoutine() {

        return null;
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }
}
