package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled//@Autonomous(name = "AutoMoveForwardAndBack")
public class AutoMoveForward extends LinearOpMode {
    DcMotor motorRight;
    DcMotor motorLeft;

    final int motorRightForward = 1;
    final int motorLeftForward = -1;

    ElapsedTime timer;

    final double driveTime = 3.5;
    final double drivePower = 0.2;

    @Override
    public void runOpMode()
    {
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");

        timer = new ElapsedTime();

        waitForStart();

        timer.reset();

        while(timer.seconds() < driveTime)
        {
            motorRight.setPower(drivePower * motorRightForward);
            motorLeft.setPower(drivePower * motorLeftForward);
        }

        timer.reset();

        while(timer.seconds() < driveTime)
        {
            motorRight.setPower(-drivePower * motorRightForward);
            motorLeft.setPower(-drivePower * motorLeftForward);
        }

        motorRight.setPower(0);
        motorLeft.setPower(0);
    }

}
