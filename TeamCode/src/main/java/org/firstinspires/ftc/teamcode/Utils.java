package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class Utils {

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

}
