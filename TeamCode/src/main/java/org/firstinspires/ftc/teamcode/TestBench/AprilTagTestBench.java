package org.firstinspires.ftc.teamcode.TestBench;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.List;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="AprilTag TestBench 1 Teleop")
public class AprilTagTestBench extends NextFTCOpMode {

    private Limelight3A limelight;

    //private PinpointLocalizer pinpoint;

    private static final int ppgTagID = 23;
    private static final int pgpTagID = 22;
    private static final int gppTagID = 21;

    public AprilTagTestBench() {

        addComponents(
                new SubsystemComponent(
                        Intake.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );

    }

    @Override
    public void onInit() {
        telemetry.addData("Limelight", "Creating Limelight instance");

        //Sample code to get limelight data:
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        //pinpoint = new PinpointLocalizer(hardwareMap, localizerConstants);

        //telemetry.setMsTransmissionInterval(11);
        //8 is purple - 9 is green, 7 is limelight april tag

        limelight.pipelineSwitch(7);
        limelight.start();

        telemetry.update();
    }

    @Override
    public void onUpdate() {

        LLResult llResult = limelight.getLatestResult();
        if(llResult != null && llResult.isValid()) {
            Pose3D robotPosition = llResult.getBotpose();
            telemetry.addData("Robot Position:",robotPosition.toString());
            telemetry.addData("Target X:", llResult.getTx());
            telemetry.addData("Target Area:", llResult.getTa());

            double dist1 = getDistanceFromTag(llResult.getTa());
            telemetry.addData("distance in inches", dist1);

            telemetry.addData("Capture Latency", llResult.getCaptureLatency());
            telemetry.addData("Targeting Latency", llResult.getTargetingLatency());

            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
                        fr.getFiducialId(),
                        fr.getFamily(),
                        fr.getTargetXDegrees(),
                        fr.getTargetYDegrees());
            }

        }

        telemetry.update();
    }

    private double getDistanceFromTag(double ta) {

        //Ta = 29633.2 * Math.pow(distance, -1.970707);
        //The abive equation comes from a power curve fit of Ta vs distance (in cms)
        if (ta <= 0) return Double.POSITIVE_INFINITY; // invalid / no detection
        final double A = 29633.2;
        final double b = 1.970707;
        double distance =  Math.pow(A / ta, 1.0 / b);
        //convert it to inches
        distance = distance / 2.54;
        return distance;
    }

    @Override
    public void onStartButtonPressed() {
        DriveTrain.getInstance().startDrive.schedule();
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Intake.getInstance().startIntake);
        Gamepads.gamepad1().dpadDown()
                .whenBecomesTrue(Intake.getInstance().stopIntake);
    }

    @Override
    public void onStop() {
        limelight.stop();
    }

}
