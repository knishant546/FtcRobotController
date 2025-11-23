package org.firstinspires.ftc.teamcode.Auton;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Sensors.ColorSensor;
import org.firstinspires.ftc.teamcode.Utils;
import org.firstinspires.ftc.teamcode.VoltageRecord;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeAuto;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterNew;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;
import org.firstinspires.ftc.teamcode.subsystems.SpinnerAuto;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="BlueBack V3.01")
public class BlueBack_Voltage extends NextFTCOpMode {

    Pose startPoseStraight = null;
    Pose adjustPoseToShoot = null;

    Pose moveToPickRow = null;

    Pose moveToPick2Balls = null;

    Pose moveToPickSecondRow = null;

    Pose moveToPick2SecondBalls = null;
    Pose adjustOut = null;

    private final double maxPower = 0.8;

    private ColorSensor colorSensor ;

    public BlueBack_Voltage() {
        addComponents(
                new SubsystemComponent(
                        IntakeAuto.getInstance(),
                        Spinner.getInstance(),
                        Lift.getInstance(),
                        ShooterNew.getInstance()
                ),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );

    }

    private Command buildMoveCommand(Pose fromPosition,Pose toPosition, double maximumPower) {
        PathChain move = follower().pathBuilder()
                .addPath(new BezierLine(fromPosition, toPosition))
                .setLinearHeadingInterpolation(fromPosition.getHeading(), toPosition.getHeading())
                .build();
        return new FollowPath(move, false, maximumPower);
    }

    private void buildPath() {
        startPoseStraight = new Pose(1.5, -32.4, Math.toRadians(180));
        adjustPoseToShoot = new Pose(46, -9, Math.toRadians(220));

        moveToPickRow = new Pose(52, -17, Math.toRadians(270));

        moveToPick2Balls = new Pose(52, -44, Math.toRadians(270));

        moveToPickSecondRow = new Pose(75, -17, Math.toRadians(270));

        moveToPick2SecondBalls = new Pose(75, -52, Math.toRadians(270));
        adjustOut = new Pose(63, -12, Math.toRadians(270));
    }
    private Command shoot() {
        return new SequentialGroup(
                Lift.getInstance().LiftUpDown(),
                IntakeAuto.getInstance().startIntake,
                new Delay(0.5),
                Lift.getInstance().LiftUpDown(),
                new Delay(0.5),
                Lift.getInstance().LiftUpDown()
        );

       /* return Lift.getInstance().LiftUpDown()
            //   .then(Spinner.getInstance().startSpinner())
            //    .then(Intake.getInstance().startIntake)
             //   .then(new Delay(0.5))
                .then(Lift.getInstance().LiftUpDown()
           //     .then(new Delay(0.5))
                .then(Lift.getInstance().LiftUpDown())
             //           .then(Spinner.getInstance().stopSpinner())
                );*/
    }

    private Command stopAll() {
        colorSensor.stopColorSensor();
        IntakeAuto.getInstance().stopIntake.schedule();
        return new SequentialGroup(
              //  IntakeAuto.getInstance().stopIntake,
                ShooterNew.getInstance().stopShooter()
        );
    }

    @Override
    public void onStop() {
        stopAll().schedule();
    }

    private Command autonomousRoutine() {
        Spinner.getInstance().setColorSensor(this.colorSensor);
        ShooterNew.getInstance().startShooter().schedule();
        return new SequentialGroup(
                      //  IntakeAuto.getInstance().startIntake,
             //   Shooter.getInstance().startShooter(),
                buildMoveCommand(startPoseStraight,adjustPoseToShoot,this.maxPower),
                new Delay(3),
                shoot(),
                buildMoveCommand(adjustPoseToShoot,moveToPickRow,this.maxPower),
                buildMoveCommand(moveToPickRow,moveToPick2Balls,0.35),
                IntakeAuto.getInstance().stopIntake,
             //   Shooter.getInstance().startShooter(),
                buildMoveCommand(moveToPick2Balls,adjustPoseToShoot,this.maxPower),
                shoot(),
                buildMoveCommand(adjustPoseToShoot,moveToPickSecondRow,this.maxPower),
                buildMoveCommand(moveToPickSecondRow,moveToPick2SecondBalls,0.35),
                IntakeAuto.getInstance().stopIntake,
                buildMoveCommand(moveToPick2SecondBalls,moveToPickSecondRow,this.maxPower),
                buildMoveCommand(moveToPickSecondRow,adjustPoseToShoot,this.maxPower),
                shoot(),
                stopAll(),
                buildMoveCommand(adjustPoseToShoot,adjustOut,this.maxPower)
        );
    }

    private long loopTime = System.currentTimeMillis();
    @Override
    public void onInit() {
        NormalizedColorSensor colorSensorHardware = ActiveOpMode.hardwareMap().get(NormalizedColorSensor.class,"sensor_color_distance");
        colorSensor = new ColorSensor(colorSensorHardware);
        Thread colorSensorThread = new Thread(colorSensor);
        colorSensor.startColorSensor();
        colorSensorThread.start();
        buildPath();

        follower().setStartingPose(startPoseStraight);
        follower().setPose(startPoseStraight);
        ShooterNew.getInstance().setShooterPowerFactor(0.65);
        Spinner.getInstance().setPower(-0.8);
    }


    @Override
    public void onUpdate() {
    /*    long currentTime = System.currentTimeMillis();
        long dt = currentTime - loopTime;
        telemetry.addData("loop Time", dt);
        loopTime =currentTime;*/

  //      follower().update();
      //  telemetry.addData("x", follower().getPose().getX());
      //  telemetry.addData("y", follower().getPose().getY());
      //  telemetry.addData("heading", follower().getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
    }
}
