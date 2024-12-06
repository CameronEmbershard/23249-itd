package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;

@Autonomous(name = "AutoMoveAndScore")
public class RobotAutoBluePixelSide extends RobotAuto {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;


    DcMotor motorLiftArm;
    DcMotor motorLiftArm2;
    Servo servoGrabber;
    Servo servoArm;

    ElapsedTime timer;
    AutoDriveAndScore autoSystem;

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
        servoArm = hardwareMap.servo.get("servoArm");

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, motorLiftArm2,servoArm);

        timer = new ElapsedTime();

        AutoDriveAndScore autoSystem = new AutoDriveAndScore(timer, driveSystem, ArmSystem,this);

        addTelemetry("Completed Init:", true);

        waitForStart();

        autoSystem.driveAutonomously();
    }
}