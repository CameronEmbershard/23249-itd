package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.VisionHandler;

@TeleOp(name = "TeleopMain")
public class RobotTeleopMain extends OpMode {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    VisionHandler visionSystem;
    //WebcamName webcam;

    DcMotor motorLiftArm;
    DcMotor motorLiftArm2;
    Servo servoGrabber;
    Servo servoWrist;
    Servo servoArm;



    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.drive.ArmSystem ArmSystem;


    //private VisionPortal visionPortal;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        motorLiftArm2 = hardwareMap.dcMotor.get("motorLiftArm2");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoWrist = hardwareMap.servo.get("servoWrist");
        servoArm = hardwareMap.servo.get("servoArm");
        //webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, motorLiftArm2,servoWrist,servoArm);
        //ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, motorLiftArm2, servoArm);


        //visionPortal = VisionPortal.easyCreateWithDefaults(
        //        hardwareMap.get(WebcamName.class, "Webcam 1"))
        telemetry.addData("Main","All Systems Online. Here We Go!");
    }

    @Override
    public void loop()
    {
        // First parameter: Strafe x input
        // Second parameter: Strafe y input
        // Third parameter: Rotation x input
        // Fourth parameter: Slow Button
        driveSystem.drive(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x, gamepad1.right_trigger);

        // First parameter: armUp input
        // Second parameter: armDown input
        ArmSystem.restrictedControlArmLift(gamepad2.left_trigger, gamepad2.left_bumper);
        ArmSystem.restrictedControlArmLift2(gamepad2.right_bumper, gamepad2.right_trigger);
        ArmSystem.ControlGripper(gamepad2.b);

        // First parameter: grabberOpen input
        // Second parameter: grabberClose input
        ArmSystem.ControlArm(gamepad2.y, gamepad2.a, gamepad2.x);

        // First parameter: wristFlat input
        // Second parameter: wristAngled input
        ArmSystem.ControlWrist(gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.x);

        telemetry.addData("Hover",ArmSystem.getHoverPoint());
        telemetry.addData("Hover2",ArmSystem.getHoverPoint2());
        telemetry.addData("Grabber",ArmSystem.getPositionGrabber());
        telemetry.addData("Wrist",ArmSystem.getPositionWrist());
        telemetry.addData("Arm",ArmSystem.getPositionArm());
        telemetry.update();
    }

}