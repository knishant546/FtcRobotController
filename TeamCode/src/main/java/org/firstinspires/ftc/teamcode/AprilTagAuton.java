//package org.firstinspires.ftc.teamcode.autonomous;
//
//import static dev.nextftc.extensions.pedro.PedroComponent.follower;
//
//
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.geometry.Pose;
//import com.pedropathing.paths.PathChain;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
//
//import org.firstinspires.ftc.teamcode.subsystems.Intake;
//import org.firstinspires.ftc.teamcode.subsystems.Lift;
//import org.firstinspires.ftc.teamcode.subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.subsystems.Spinner;
//
//import dev.nextftc.core.commands.Command;
//import dev.nextftc.core.components.SubsystemComponent;
//import dev.nextftc.ftc.NextFTCOpMode;
//import dev.nextftc.ftc.components.BulkReadComponent;
//
//@Autonomous(name="AprilTag Detect Autonomous")
//public class AprilTagAuton extends NextFTCOpMode {
//
 // 1 inch = 0.625
//    //Starting position
//    // 56,7 inches
//    //Ending position
//    // 70,20 inches
//    private NormalizedColorSensor colorSensor;
//
//    private Limelight3A limelight;
//    private Pose currPose;
//
//    public AprilTagAuton() {
//        addComponents(
//                new SubsystemComponent(
//                        Intake.getInstance(),
//                        Spinner.getInstance(),
//                        Lift.getInstance(),
//                        Shooter.getInstance()
//                ),
//                BulkReadComponent.INSTANCE
//        );
//    }
//
//    @Override
//    public void onInit() {
//
//        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color_distance");
//        colorSensor.setGain(10);
//        Lift.getInstance().initialize();
//        Spinner.getInstance().initialize();
//        Shooter.getInstance().initialize();
//        Intake.getInstance().initialize();
//
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");
//        telemetry.setMsTransmissionInterval(11);
//        //pinpoint = new PinpointLocalizer(hardwareMap, localizerConstants);
//
//        //telemetry.setMsTransmissionInterval(11);
//        //8 is purple - 9 is green, 7 is limelight april tag
//
//        limelight.pipelineSwitch(7);
//        limelight.start();
//    }
//
//    @Override
//    public void onUpdate() {
//        follower().update();
//        currPose = follower().getPose();
//
//
//    }
//
//    private Command autonomousRoutine() {
////        return new SequentialGroup(
////                Lift.INSTANCE.toHigh,
////                new ParallelGroup(
////                        Lift.INSTANCE.toMiddle,
////                        Claw.INSTANCE.close
////                ),
////                new Delay(0.5),
////                new ParallelGroup(
////                        Claw.INSTANCE.open,
////                        Lift.INSTANCE.toLow
////                )
////        );
//    }
//
//    @Override
//    public void onStartButtonPressed() {
//        autonomousRoutine().schedule();
//    }
//
//
//}
