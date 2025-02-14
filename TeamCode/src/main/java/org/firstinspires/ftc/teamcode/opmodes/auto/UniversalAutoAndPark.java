package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;

import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "JustPark/VisionTest")

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


    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.drive.ArmSystem ArmSystem;
    WebcamName webcam;

    private static final float rectHeight = .6f/8f;
    private static final float rectWidth = 1.5f/8f;

    private static final float offsetX = 0f/8f;//changing this moves the three rects and the three circles left or right, range : (-2, 2) not inclusive
    private static final float offsetY = 0f/8f;//changing this moves the three rects and circles up or down, range: (-4, 4) not inclusive

    private static final float[] midPos = {4f/8f+offsetX, 4f/8f+offsetY};//0 = col, 1 = row
    private static final float[] leftPos = {2f/8f+offsetX, 4f/8f+offsetY};
    private static final float[] rightPos = {6f/8f+offsetX, 4f/8f+offsetY};
    //moves all rectangles right or left by amount. units are in ratio to monitor

    private final int rows = 640;
    private final int cols = 480;

    private ElapsedTime runtime = new ElapsedTime();

    OpenCvCamera phoneCam;

    @Override
    public void runOpMode()
    {
        WebcamName cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        phoneCam = OpenCvCameraFactory.getInstance().createWebcam(cameraName, 0);

        StageSwitchingPipeline vision = new StageSwitchingPipeline();

        phoneCam.openCameraDevice();//open camera
        phoneCam.setPipeline(vision);//different stages
        phoneCam.startStreaming(rows, cols, OpenCvCameraRotation.UPRIGHT);//display on RC

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
        //ArmSystem = new ArmSystem(motorLiftArm, servoGrabber,servoGrabber2, servoArm);
        ArmSystem = new org.firstinspires.ftc.teamcode.drive.ArmSystem(motorLiftArm, motorRotateArm, servoGrabber,servoGrabber2);

        timer = new ElapsedTime();

        AutoPark autoSystem = new AutoPark(timer, driveSystem, this);

        telemetry.addData("Completed Init:", true);

        telemetry.update();

        waitForStart();

        //comment for no movement and no stop
        autoSystem.driveAutonomously();

        runtime.reset();
        while (opModeIsActive()) {
            //0 means skystone, 1 means yellow stone
            //-1 for debug, but we can keep it like this because if it works, it should change to either 0 or 255

            telemetry.addData("Vision", vision.getSide());
            telemetry.update();
            sleep(500);
            //call movement functions
//            strafe(0.4, 200);
//            moveDistance(0.4, 700);

        }

        //autoSystem.driveAutonomously();
    }
}