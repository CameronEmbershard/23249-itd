package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;

@Autonomous(name = "JustPark")

public class UniversalAutoAndPark extends RobotAuto {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;


    DcMotor motorLiftArm;
    DcMotor motorRotateArm;
    Servo servoGrabber;
    Servo servoGrabber2;
    //Servo servoArm;

    ElapsedTime timer;
    AutoPark autoSystem;

    VisionHandler visionSystem;

    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.Drive.ArmSystem ArmSystem;
    WebcamName webcam;

    @Override
    public void runOpMode()
    {
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        motorRotateArm = hardwareMap.dcMotor.get("motorRotateArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoGrabber2 = hardwareMap.servo.get("servoGrabber2");
        //servoArm = hardwareMap.servo.get("servoArm");

        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        //ArmSystem = new ArmSystem(motorLiftArm, servoGrabber,servoGrabber2, servoArm);
        ArmSystem = new ArmSystem(motorLiftArm, motorRotateArm, servoGrabber,servoGrabber2);
        visionSystem = new VisionHandler();

        timer = new ElapsedTime();

        AutoPark autoSystem = new AutoPark(timer, driveSystem, this);

        telemetry.addData("Completed Init:", true);

        telemetry.update();

        waitForStart();

        autoSystem.driveAutonomously();
    }
}