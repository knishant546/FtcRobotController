package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Shooter implements Subsystem {

    private static final Shooter INSTANCE = new Shooter();
    public static PIDCoefficients pidCoefficients = new PIDCoefficients(0.0003, 0, 0);

    private  double powerFactor = 1;

    private static final ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(pidCoefficients)
            .basicFF(0)
            .build();

    private static double max_goal = 6000;


    public static Shooter getInstance() {
        return INSTANCE;
    }

    //TODO will change this name to pickMotor
    private MotorEx shootermotor = new MotorEx("shooter");


    private double velo;

    private Shooter() {
    }

    @Override
    public void initialize() {
        shootermotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        controlSystem.setGoal(new KineticState());
        this.stopShooter().schedule();
    }

    @Override
    public void periodic() {
        double powerTarget = controlSystem.calculate(shootermotor.getState());
        ActiveOpMode.telemetry().addData("motor power set to: ", powerTarget);
        shootermotor.setPower(powerTarget);
        ActiveOpMode.telemetry().addData("Motor velocity :",shootermotor.getVelocity());
        ActiveOpMode.telemetry().addData("Raw velocity",shootermotor.getRawTicks());
    }

    public float getShooterPower() {
        return (float) shootermotor.getPower();
    }

    public void setShooterPowerFactor(double powerFactor) {
        this.powerFactor = powerFactor;
    }

    public double getShooterPowerFactor(){
        return this.powerFactor;
    }

    public Command startShooter() {
         return new InstantCommand(() -> {
             new RunToVelocity(controlSystem, max_goal * powerFactor).requires(this).schedule();
         }).requires(this);
    }

    public Command increasePower = new InstantCommand(()->{
            setShooterPowerFactor(1.0);
            startShooter().schedule();
    }).requires(this);

    public Command decreasePower = new InstantCommand(()->{
            setShooterPowerFactor(0.8);
            startShooter().schedule();
    }).requires(this);



    public Command stopShooter() {
        return new InstantCommand(() -> {
            new RunToVelocity(controlSystem, 0).requires(this).schedule();
        }).requires(this);
    }
}
