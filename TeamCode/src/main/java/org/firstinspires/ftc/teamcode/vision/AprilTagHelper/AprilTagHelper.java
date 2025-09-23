package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagHelper {
    private final VisionPortal portal;
    private final AprilTagProcessor proc;

    public AprilTagHelper(HardwareMap hw) {
        proc = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .build();

        // Use BUILTIN camera if you don’t have a webcam:
        // portal = new VisionPortal.Builder()
        //         .setCamera(BuiltinCameraDirection.BACK)
        //         .addProcessor(proc).build();

        portal = new VisionPortal.Builder()
                .setCamera(hw.get(WebcamName.class, "Webcam 1"))
                .addProcessor(proc)
                .build();
    }

    public List<AprilTagDetection> detections() {
        return proc.getDetections();
    }

    public void close() {
        portal.close();
    }
}

