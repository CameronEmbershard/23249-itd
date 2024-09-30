package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.ArmControl;
import org.firstinspires.ftc.teamcode.subsystems.AutoDriveAndScore;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.VisionHandler;

@Autonomous(name = "AutoRedRight")
public class RobotAutoRedScoringSide extends RobotAuto {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;


    DcMotor motorLiftArm;
    Servo servoGrabber;
    Servo servoWrist;
    Servo servoStopper;

    ElapsedTime timer;

    MechanumDrive driveSystem;
    ArmControl armSystem;
    VisionHandler visionSystem;
    AutoDriveAndScore autoSystem;

    WebcamName webcam;

    @Override
    public void runOpMode()
    {
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        //motorScoringArm = hardwareMap.dcMotor.get("motorScoringArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoWrist = hardwareMap.servo.get("servoWrist");
        servoStopper = hardwareMap.servo.get("servoStopper");

        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        armSystem = new ArmControl(motorLiftArm, servoGrabber, servoWrist, servoStopper);
        visionSystem = new VisionHandler(webcam, false);

        timer = new ElapsedTime();

        autoSystem = new AutoDriveAndScore(timer, driveSystem, armSystem,this, visionSystem);

        addTelemetry("Completed Init:", true);

        waitForStart();

        autoSystem.driveAutonomously(false, true, "RedBlock");
    }
}
