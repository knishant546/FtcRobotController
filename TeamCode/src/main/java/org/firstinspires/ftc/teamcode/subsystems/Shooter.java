package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
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
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {

    public  double kP = 0.00023;
    public  double kI = 0.00001;
    public  double kD = 0.0;
    public  double kF = 0.00037; // Start tuning here!

    private static final Shooter INSTANCE = new Shooter();
    public  PIDCoefficients pidCoefficients = new PIDCoefficients(kP , kI, kD);

    private  double powerFactor = 1;

    private  final ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(pidCoefficients)
            .basicFF(kF)
            .build();


    private  double max_goal = 2600;


    public static Shooter getInstance() {
        return INSTANCE;
    }

    //TODO will change this name to pickMotor
    private MotorEx shootermotor = new MotorEx("shooter");


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
        givePower();

   }

    private Command givePower(){
        double powerTarget = controlSystem.calculate(shootermotor.getState());
        ActiveOpMode.telemetry().addData("motor power set to: ", powerTarget);
        ActiveOpMode.telemetry().addData("Motor velocity :",shootermotor.getVelocity());
        ActiveOpMode.telemetry().addData("Raw velocity",shootermotor.getRawTicks());
      //  shootermotor.setPower(powerTarget);
        return new SetPower(shootermotor,powerTarget);
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
             new RunToVelocity(controlSystem, max_goal * powerFactor).then(givePower()).
                     requires(this).schedule();

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
            new RunToVelocity(controlSystem, 0).then(givePower()).requires(this).schedule();
        }).requires(this);
    }
}
