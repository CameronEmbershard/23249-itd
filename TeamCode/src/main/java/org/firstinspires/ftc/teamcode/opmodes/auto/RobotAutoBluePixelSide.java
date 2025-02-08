package org.firstinspires.ftc.teamcode.opmodes.auto;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name = "AutoMoveAndScore")
public class RobotAutoBluePixelSide extends RobotAuto {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;


    DcMotor motorLiftArm;
    DcMotor motorRotateArm;
    Servo servoGrabber;
    Servo servoGrabber2;
    //Servo servoArm;

    private VisionPortal visionPortal;

    ElapsedTime timer;
    AutoDriveAndScore autoSystem;

    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.Drive.ArmSystem ArmSystem;

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

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(motorLiftArm, motorRotateArm, servoGrabber, servoGrabber2);
        //ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, servoGrabber2, servoArm);

        timer = new ElapsedTime();
        //VisionPortal.Builder builder = new VisionPortal.Builder();
        //builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        //builder.enableLiveView(true);
        //builder.addProcessor(pipeline);
        //builder.setCameraResolution(new Size(640, 480));
        //visionPortal = builder.build();
        //visionPortal.resumeStreaming();

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(motorLiftArm, motorRotateArm, servoGrabber, servoGrabber2);
        //ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, servoGrabber2, servoArm);
        StageSwitchingPipeline visionSystem = new StageSwitchingPipeline();

        timer = new ElapsedTime();

        AutoDriveAndScore autoSystem = new AutoDriveAndScore(timer, driveSystem, ArmSystem, this, visionSystem);

        addTelemetry("Completed Init:", true);

        waitForStart();

        autoSystem.driveAutonomously();
    }
}