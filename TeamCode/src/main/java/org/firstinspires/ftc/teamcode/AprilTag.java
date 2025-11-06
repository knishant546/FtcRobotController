package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class AprilTag {
    private static  AprilTag INSTANCE ;

    private AprilTag(Limelight3A limelight) {
        this.limelight = limelight;
        limelight.pipelineSwitch(7);
        start();
    }

    public static AprilTag getInstance(Limelight3A limelight) {
        if (INSTANCE == null) {
            INSTANCE = new AprilTag(limelight);
        }
        return INSTANCE;
    }

    public static AprilTag getInstance() {
        return INSTANCE;
    }

    private final Limelight3A limelight;

    public void start() {
        limelight.start();
    }

    public void stop() {
        limelight.stop();
    }


    public double getAprilTagDistance() {
        LLResult llResult = limelight.getLatestResult();
        if(llResult != null && llResult.isValid()) {
            return getDistanceFromTag(llResult.getTa());
        }
        return 0;
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
}
