package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.ArmControl;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.TwoMotorTank;

@Disabled
@TeleOp(name = "TeleopArm")
public class RobotTeleopArm extends OpMode {

//    DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
//    DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
//    DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
//    DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");


    DcMotor motorLiftArm;
    Servo servoGrabber;
    Servo servoWrist;
    Servo servoStopper;

//    MechanumDrive driveSystem;
    ArmControl armSystem;

    @Override
    public void init() {
//        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
//        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
//        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
//        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        //motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
//        motorScoringArm = hardwareMap.dcMotor.get("motorScoringArm");
        //servoGrabber = hardwareMap.servo.get("servoGrabber");
        //servoWrist = hardwareMap.servo.get("servoWrist");
        //servoStopper = hardwareMap.servo.get("servoStopper");

//        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        //armSystem = new ArmControl(motorLiftArm, servoGrabber, servoWrist, servoStopper);
    }

    @Override
    public void loop()
    {
        // First parameter: Strafe x input
        // Second parameter: Strafe y input
        // Third parameter: Rotation x input
        //driveSystem.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // First parameter: armUp input
        // Second parameter: armDown input
        //armSystem.controlArmLift(gamepad1.left_bumper, gamepad1.left_trigger);

        // First parameter: grabberOpen input
        // Second parameter: grabberClose input
        //armSystem.controlArmGrabber(gamepad1.x, gamepad1.a);

        // First parameter: wristFlat input
        // Second parameter: wristAngled input
        //armSystem.controlArmWrist(gamepad1.left_trigger, gamepad1.left_bumper);

        // First parameter: stopperOpen input
        // Second parameter: stopperClose input
        //armSystem.controlArmStopper(gamepad1.y, gamepad1.b);
    }
}