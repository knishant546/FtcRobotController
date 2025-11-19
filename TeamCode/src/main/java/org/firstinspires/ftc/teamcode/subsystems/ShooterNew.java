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

public class ShooterNew implements Subsystem {

    private final double kP = 0.00023;
    private final double kI = 0.00001;
    private final double kD = 0;
    private final double kF = 0.00037;

    private double TARGET_VELOCITY = 0; // ticks/sec

    private DcMotor shooterMotor;

    // PIDF internal state
    private double integral = 0;
    private double lastError = 0;

    // For manual velocity calculation
    private int lastPosition = 0;
    private long lastTime = 0;

    private static final ShooterNew INSTANCE = new ShooterNew();
    public  PIDCoefficients pidCoefficients = new PIDCoefficients(kP , kI, kD);

    private  double powerFactor = 1;



    private  final ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(pidCoefficients)
            .basicFF(kF)
            .build();


    private  double max_goal = 2600;


    public static ShooterNew getInstance() {
        return INSTANCE;
    }

    public void setShooterMotor(DcMotor motor) {
        this.shooterMotor = motor;
        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lastPosition = shooterMotor.getCurrentPosition();
        lastTime = System.nanoTime();
    }

    private ShooterNew() {
    }

    @Override
    public void initialize() {

    }


    @Override
    public void periodic() {
        long now = System.nanoTime();
        double dt = (now - lastTime) / 1e9; // seconds
        lastTime = now;

        // Get current position
        int currentPosition = shooterMotor.getCurrentPosition();

        // Calculate velocity in ticks/sec
        double currentVelocity = (currentPosition - lastPosition) / dt;
        lastPosition = currentPosition;

        // PIDF calculation
        double error = TARGET_VELOCITY - currentVelocity;
        integral += error * dt;
        double derivative = (error - lastError) / dt;
        lastError = error;

        double power = kP * error + kI * integral + kD * derivative + kF * TARGET_VELOCITY;
        if (TARGET_VELOCITY ==0){
            power = 0;
        }else{
            // Clamp power between -1 and 1
            power = Math.max(-1.0, Math.min(1.0, power));
        }
        shooterMotor.setPower(power);
        ActiveOpMode.telemetry().addData("Motor velocity :",currentVelocity);
        ActiveOpMode.telemetry().addData("Target velocity",TARGET_VELOCITY);
    }

    public float getShooterPower() {
        return (float) shooterMotor.getPower();
    }

    public void setShooterPowerFactor(double powerFactor) {
        this.powerFactor = powerFactor;
    }

    public double getShooterPowerFactor(){
        return this.powerFactor;
    }

    public Command startShooter() {
         return new InstantCommand(() -> {
             TARGET_VELOCITY = max_goal * 1;
         }).requires(this);
    }

    public Command increasePower = new InstantCommand(()->{
        TARGET_VELOCITY = max_goal * 1;
    }).requires(this);

    public Command decreasePower = new InstantCommand(()->{
           TARGET_VELOCITY = max_goal * .8;
    }).requires(this);



    public Command stopShooter() {
        return new InstantCommand(() -> {
            TARGET_VELOCITY = 0;
        }).requires(this);
    }
}
