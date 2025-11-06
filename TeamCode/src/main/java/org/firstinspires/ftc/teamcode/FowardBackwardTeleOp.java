package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Spinner;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

@TeleOp(name = "Sample Latest: Forward Backward TeleOp Without Components")
public class FowardBackwardTeleOp extends NextFTCOpMode {

    private Limelight3A limelight;

    //public DigitalChannel touchSensor;

    public NormalizedColorSensor colorSensor;

    public  FowardBackwardTeleOp() {
        telemetry.addData("Instr", "Forward Backward Components adding");

        addComponents(
                new SubsystemComponent(
                        Intake.getInstance(),
                Spinner.getInstance()),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    private float getColorType(String colorName) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float threshold = 0.2f;

        float red = colors.red/colors.alpha;
        float green = colors.green/colors.alpha;
        float blue = colors.blue/colors.alpha;

        telemetry.addData("Red", red);
        telemetry.addData("Green", green);
        telemetry.addData("Blue", blue);
        return 0.0f;
//        if(red > green && red > blue) {
//            return 1.0f; // Red
//        } else if (green > red && green > blue) {
//            return 2.0f; // Green
//        } else if (blue > red && blue > green) {
//            return 3.0f; // Blue
//        } else {
//            return 0.2f; // Unknown
//        }

        /*
        if (colors.red < threshold && colors.green < threshold && colors.blue < threshold && colors.alpha < threshold) {
            // Actions to take when black is detected
            telemetry.addData("Detection", "Black Color Detected");
            // Add robot actions here, e.g., stop motors, turn, etc.
        } else {
            telemetry.addData("Detection", "Other Color Detected");
        }
         */
    }

//    @Override
//    public void onInit() {
//        telemetry.addData("Instr", "Creating Limelight instance");
//        telemetry.update();
//
//        //Sample code to get limelight data:
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");
//        //telemetry.setMsTransmissionInterval(11);
//        //8 is purple - 9 is green, 7 is limelight april tag
//        limelight.pipelineSwitch(8);
//    }

    @Override
    public void onInit() {
        telemetry.addData("Instr", "Creating Touch Sensor instance");
        telemetry.update();

        //Creating Touch Sensor instance
        //touchSensor = hardwareMap.get(DigitalChannel.class, "touchsensor");
        //touchSensor.setMode(DigitalChannel.Mode.INPUT);

        //Creating Color Sensor instance
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color_distance");
        colorSensor.setGain(8);

        //
        telemetry.addData("LeftServo curr",Lift.getInstance().getLeftPosition());
        telemetry.addData("RightServo curr",Lift.getInstance().getRightPosition());

        //Lift.getInstance().reset();

        telemetry.addData("LeftServo Init pos",Lift.getInstance().getLeftPosition());
        telemetry.addData("RightServo Init pos",Lift.getInstance().getRightPosition());

        telemetry.update();

    }

    private final MotorEx frontLeftMotor = new MotorEx("frontleft");
    private final MotorEx frontRightMotor = new MotorEx("frontright").reversed();
    private final MotorEx backLeftMotor = new MotorEx("backleft");
    private final MotorEx backRightMotor = new MotorEx("backright").reversed();

    @Override
    public void onUpdate() {

//        if(touchSensor.getState() == false) {
//            telemetry.addData("TouchSensor", "Pressed");
//            //Shooter.getInstance().setPower(0.0);
//        } else {
//            telemetry.addData("TouchSensor", "Not Pressed");
//        }
//        telemetry.update();

        getColorType("color");
        telemetry.update();

        telemetry.addData("LeftServo", Lift.getInstance().getLeftPosition());
        telemetry.addData("RightServo", Lift.getInstance().getRightPosition());

    }

    @Override
    public void onStartButtonPressed() {

        telemetry.addData("Instr", "Forward Backward OnStart Pressed");
        telemetry.addData("Instr", "TestJan");

        //double val = touchSensor.getValue();
        //telemetry.addData("Instr1", "Val"+ val);

        //telemetry.addData("Instr2", "Connection Val " +touchSensor.getConnectionInfo());


        //limelight.start();

        //telemetry.addData("Instr", "Limelight started");

        /*
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {

            Pose3D botpose = result.getBotpose();
            telemetry.addData("Instr", "tx: " + result.getTx());
            telemetry.addData("Instr", "ty: " +result.getTy());
            telemetry.addData("Instr", "ta: " +result.getTa());
            telemetry.addData("Instr", "Botpose: "+ botpose.toString());

        } else {
            telemetry.addData("Instr","Telemetry Data for limelight Missing or is not valid");
        }
        */
        telemetry.update();

        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );

        driverControlled.schedule();
        //Spinner.getInstance().setPower(-1.0);
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Intake.getInstance().startIntake)
                .whenBecomesFalse(Intake.getInstance().stopIntake);
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Spinner.getInstance().startSpinner())
                .whenBecomesFalse(Spinner.getInstance().stopSpinner());

        Gamepads.gamepad1().a()
                .whenBecomesTrue(Shooter.getInstance().startShooter())
                .whenBecomesFalse(Shooter.getInstance().stopShooter());

        Gamepads.gamepad1().b()
                        .whenBecomesTrue(Lift.getInstance().liftUp());
        Gamepads.gamepad1().x()
                        .whenBecomesTrue(Lift.getInstance().liftDown());



        telemetry.update();
    }

}
