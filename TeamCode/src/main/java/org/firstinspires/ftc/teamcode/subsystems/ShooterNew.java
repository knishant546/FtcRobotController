package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

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
    private final double kI = 0.000115;
    private final double kD = 0.000115;
    private final double kF = 0.000405;

    private double TARGET_VELOCITY = 0; // ticks/sec

    private DcMotor shooterMotor;

    // PIDF internal state
    private double integral = 0;
    private double lastError = 0;

    // For manual velocity calculation
    private int lastPosition = 0;
    private long lastTime = 0;

    private static final ShooterNew INSTANCE = new ShooterNew();
    private  double powerFactor = 1;

    private  double max_goal = 2600;


    public static ShooterNew getInstance() {
        return INSTANCE;
    }

    private ShooterNew() {
    }

    @Override
    public void initialize() {
        this.shooterMotor = ActiveOpMode.hardwareMap().get(DcMotor.class,"shooter");
        TARGET_VELOCITY = 0;
        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lastPosition = shooterMotor.getCurrentPosition();
        lastTime = System.nanoTime();
        integral =0;
        lastError=0;
        powerFactor = 1;
    }


    @Override
    public void periodic() {
        long now = System.nanoTime();
        double dt = (now - lastTime) / 1e9; // seconds
        // Get current position
        int currentPosition = shooterMotor.getCurrentPosition();
        // Calculate velocity in ticks/sec
        double currentVelocity = (currentPosition - lastPosition) / dt;
        // We dont want to tune more often
        if (dt < 1){
            return;
        }
        lastTime = now;
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
             TARGET_VELOCITY = max_goal * powerFactor;
         }).requires(this);
    }

    public Command increasePower = new InstantCommand(()->{
        TARGET_VELOCITY = max_goal * powerFactor * 1;
    }).requires(this);

    public Command decreasePower = new InstantCommand(()->{
        // 0.65
        TARGET_VELOCITY = max_goal * powerFactor * 0.75;
    }).requires(this);



    public Command stopShooter() {
        return new InstantCommand(() -> {
            TARGET_VELOCITY = 0;
        }).requires(this);
    }
}
