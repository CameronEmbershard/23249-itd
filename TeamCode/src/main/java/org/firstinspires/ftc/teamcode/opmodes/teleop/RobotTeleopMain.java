package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;

@TeleOp(name = "TeleopMain")
public class RobotTeleopMain extends OpMode {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;


    //DcMotor motorLiftArm;
    Servo servoGrabber;
    //Servo servoWrist;
    //Servo servoStopper;


    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.drive.ArmSystem ArmSystem;


    //private VisionPortal visionPortal;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        //motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        //servoWrist = hardwareMap.servo.get("servoWrist");
        //servoStopper = hardwareMap.servo.get("servoStopper");

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(servoGrabber);

        //visionPortal = VisionPortal.easyCreateWithDefaults(
        //        hardwareMap.get(WebcamName.class, "Webcam 1"));
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
        //ArmSystem.restrictedControlArmLift(gamepad2.right_bumper, gamepad2.right_trigger);
        ArmSystem.ControlGripper(gamepad1.right_bumper);

        // First parameter: grabberOpen input
        // Second parameter: grabberClose input
        //armSystem.controlArmGrabber(gamepad2.x, gamepad2.a);

        // First parameter: wristFlat input
        // Second parameter: wristAngled input
        //armSystem.controlArmWrist(gamepad2.left_trigger, gamepad2.left_bumper);

        // First parameter: stopperOpen input
        // Second parameter: stopperClose input
        //armSystem.controlArmStopper(gamepad2.y, gamepad2.b);
    }
}