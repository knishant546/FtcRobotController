package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class IntakeAuto implements Subsystem {

    private static final IntakeAuto INSTANCE = new IntakeAuto();
    public static IntakeAuto getInstance() {
        return INSTANCE;
    }

    private DcMotor intakeMotor ;

    private IntakeAuto() {
    }

    @Override
    public void initialize() {
        this.intakeMotor =  ActiveOpMode.hardwareMap().get(DcMotor.class,"intake");
        this.stopIntake.schedule();
    }

    public Command startIntake = new InstantCommand(() -> {
        this.intakeMotor.setPower(-1.0);
    }).requires(this);

    public Command stopIntake = new InstantCommand(() -> {
        this.intakeMotor.setPower(0.0);
    }).requires(this);
}
