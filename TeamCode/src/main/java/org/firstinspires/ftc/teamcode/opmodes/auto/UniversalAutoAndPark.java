package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;

@Autonomous(name = "AutoRedRight")
public class UniversalAutoAndPark extends RobotAuto {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;


    DcMotor motorLiftArm;
    DcMotor motorLiftArm2;
    Servo servoGrabber;
    Servo servoWrist;
    Servo servoArm;

    ElapsedTime timer;
    AutoDriveForward autoSystem;

    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.drive.ArmSystem ArmSystem;

    @Override
    public void runOpMode()
    {
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
        //visionSystem = new VisionHandler(webcam, false);

        timer = new ElapsedTime();

        AutoDriveForward autoSystem = new AutoDriveForward(timer, driveSystem, this, ArmSystem);

        addTelemetry("Completed Init:", true);

        waitForStart();

        autoSystem.driveAutonomously();
    }
}