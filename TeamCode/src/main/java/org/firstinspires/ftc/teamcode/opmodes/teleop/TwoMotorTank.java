package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/** @noinspection ALL*/
@TeleOp(name = "TwoMotorTank")
public class TwoMotorTank extends OpMode{

    DcMotor motorRight;
    DcMotor motorLeft;

    boolean orangejuice = false;

    int motorRightForward = 1;
    int motorLeftForward = -1;

    final double speedMultiplier = 0.5;
    final double slowSpeedMultiplier = 0.1;

    final double wheelDiameter = 96;

    // The number of ticks that represent a full rotation
    final double ticksPerRotation = 537.6;



    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
    }

    @Override
    public void loop()
    {
        drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.b, gamepad1.x, gamepad1.y);
    }


    synchronized public void drive(double xDir, double yDir, boolean bPressed, boolean xPressed, boolean yPressed) {

        double overallPower = Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2));
        double maxPowerMultiplier = 1 / overallPower;

        double xSign = Math.signum(xDir);
        double ySign = Math.signum(yDir);


        if(xSign == 0){
            xSign = 1;
        }

        if(ySign == 0){
            ySign = 1;
        }

        boolean stickRight = xDir >= 0;
        // boolean stickUp = yDir >= 0;

        double rightPower;
        double leftPower;

        if(xSign * ySign < 0)
        {
            rightPower = ySign;
        }
        else
        {
            if(stickRight) // true
            {
                rightPower = ((yDir * maxPowerMultiplier) - 0.5) * 2;
            }
            else
            {
                rightPower = ((yDir * maxPowerMultiplier) + 0.5) * 2;
            }
        }

        if(xSign * ySign > 0)
        {
            leftPower = xSign;
        }

        else
        {
            if(stickRight)
            {
                leftPower = ((yDir * maxPowerMultiplier) + 0.5) * 2;
            }
            else
            {
                leftPower = ((yDir * maxPowerMultiplier) - 0.5) * 2;
            }
            if (gamepad1.cross) {
            leftPower = 2;


        }

        }


        if(!bPressed)
        {
            motorLeft.setPower(leftPower * overallPower * speedMultiplier * motorLeftForward);
            motorRight.setPower(rightPower * overallPower * speedMultiplier * motorRightForward);
        }
        else
        {
            motorLeft.setPower(leftPower * overallPower * slowSpeedMultiplier * motorLeftForward);
            motorRight.setPower(rightPower * overallPower * slowSpeedMultiplier * motorRightForward);
        }

        if (yPressed && (orangejuice == false))
        {
            motorLeft.setPower(1);
            motorRight.setPower(-1);
            orangejuice = true;

        }
        if (yPressed && (orangejuice == true)) {
            motorLeft.setPower(0);
            motorRight.setPower(0);
            orangejuice = false;
        }

    }
}