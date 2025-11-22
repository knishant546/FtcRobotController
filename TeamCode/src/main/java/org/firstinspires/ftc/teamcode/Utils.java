package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import java.util.HashMap;

import dev.nextftc.ftc.ActiveOpMode;

public class Utils {

    /**
     * Detects the Artifacts near by the color sensor.
     * Alpha tracks the amount of clear light it observes .If there is a closer Object
     * light get reflect and it get captureed higher value.
     * @param colorSensor
     * @return
     */
    public static boolean isObjectDetected(NormalizedColorSensor colorSensor) {
        float alphaValue = colorSensor.getNormalizedColors().alpha;
        ActiveOpMode.telemetry().addData("Alpha value :",alphaValue);
        return (alphaValue >= 0.045);
    }

    public static float[] getRGBA(NormalizedColorSensor colorSensor) {

        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float red = colors.red/colors.alpha;
        float green = colors.green/colors.alpha;
        float blue = colors.blue/colors.alpha;

        float [] rgba = new float[4];
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = colors.alpha;
        ActiveOpMode.telemetry().addData("Alpha Detected",colors.alpha);
        return rgba;
    }

    /*
    10
    Nothing - 0.219, 0.3809, 0.333, 0.016
    Purple - 0.086, 0.106, 0.1646, 0.1233
    green - 0.067,0.2313,0.1752, 0.06



     */
    public static String getColorName(float[] rgba) {
        float threshold = 0.2f;
        float redthreshold = 0.150f;
        float greenthreshold = 0.250f;
        float bluethreshold = 0.250f;
        if(rgba[0] > threshold && rgba[1] > threshold && rgba[2] > threshold) {
            return "Nothing";
        } else if (rgba[2] > rgba[0] && rgba[2] > rgba[1]) {
            return "Purple";
        } else if (rgba[1] > threshold && rgba[0] <= threshold && rgba[2] <= threshold) {
            return "Green";
        }

        return "Unknown";
    }


    /*
        //black - 0.399, 0.7152, 0.632, 0.0182
        //green - 0.1177, 0.3532, 0.2738, 0.1115

     */
    public static String detectColorName(float[] rgba) {

      /*  if(rgba[0] >= 0.3 && rgba[1] >= 0.6 && rgba[2] >= 0.5) {
            return "Nothing";
        }
        */

        // if Alpha is less .1 then there is no close object to reflect the light.
        if (rgba[3] < 0.04) {
            return "Nothing";
        }
        return "Something";
    }

   private static HashMap<Double,VoltageRecord> voltageTable = new HashMap<>();

    public static VoltageRecord getVoltageRecord(){
        VoltageSensor batteryVoltageSensor = ActiveOpMode.hardwareMap().voltageSensor.get("Control Hub");
        ActiveOpMode.telemetry().addData("Battery Voltage", "%.2f V", batteryVoltageSensor.getVoltage());
        double currentVoltage = roundToHalf(batteryVoltageSensor.getVoltage());
        VoltageRecord voltageRecord = voltageTable.get(currentVoltage);
        if (voltageRecord != null) {
            ActiveOpMode.telemetry().addData("Not Found Voltage", currentVoltage);
            return voltageRecord;
        }
       return new VoltageRecord(12.5,-1,-1,0.35,1,0.7);
    }

    public static double roundToHalf(double v) {
        return Math.round(v * 2) / 2.0;
    }
    public static void popuplateTable() {

        voltageTable.put(13.0,new VoltageRecord(13,-1,-1,0.35,1,0.7));
    }

}