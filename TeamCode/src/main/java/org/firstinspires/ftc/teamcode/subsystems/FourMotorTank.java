package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class FourMotorTank {

    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackLeft;
    DcMotor motorBackRight;

    int motorRightForward = 1;
    int motorLeftForward = -1;

    final double speedMultiplier = 0.5;
    final double slowSpeedMultiplier = 0.1;

    private DoubleSupplier xPower;
    private DoubleSupplier yPower;
    private BooleanSupplier slowMode;

    public FourMotorTank(DcMotor motorFrontLeft, DcMotor motorFrontRight,
                         DcMotor motorBackLeft, DcMotor motorBackRight,
                         DoubleSupplier xPower, DoubleSupplier yPower,
                         BooleanSupplier slowMode){
        this.motorFrontLeft = motorFrontLeft;
        this.motorFrontRight = motorFrontRight;
        this.motorBackLeft = motorBackLeft;
        this.motorBackRight = motorBackRight;
        this.xPower = xPower;
        this.yPower = yPower;
        this.slowMode = slowMode;
    }

    public void drive(){
        double xDir = xPower.getAsDouble(); // 0
        double yDir = yPower.getAsDouble(); // 0.5

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

        boolean stickRight = xDir >= 0; // true
        // boolean stickUp = yDir >= 0;

        double rightPower;
        double leftPower;

        if(xSign * ySign < 0) // false
        {
            rightPower = ySign;
        }
        else
        {
            if(stickRight) // true
            {
                rightPower = ((yDir * maxPowerMultiplier) - 0.5) * 2; // 1
            }
            else
            {
                rightPower = ((yDir * maxPowerMultiplier) + 0.5) * 2;
            }
        }

        if(xSign * ySign > 0) // false
        {
            leftPower = xSign;
        }
        else
        {
            if(stickRight) // true
            {
                leftPower = ((yDir * maxPowerMultiplier) + 0.5) * 2;
            }
            else
            {
                leftPower = ((yDir * maxPowerMultiplier) - 0.5) * 2;
            }
        }

        if(slowMode.getAsBoolean())
        {
            motorFrontLeft.setPower(leftPower * overallPower * speedMultiplier * motorLeftForward);
            motorFrontRight.setPower(rightPower * overallPower * speedMultiplier * motorRightForward);

            motorBackLeft.setPower(leftPower * overallPower * speedMultiplier * motorLeftForward);
            motorBackRight.setPower(rightPower * overallPower * speedMultiplier * motorRightForward);
        }
        else
        {
            motorFrontLeft.setPower(leftPower * overallPower * slowSpeedMultiplier * motorLeftForward);
            motorFrontRight.setPower(rightPower * overallPower * slowSpeedMultiplier * motorRightForward);

            motorBackLeft.setPower(leftPower * overallPower * slowSpeedMultiplier * motorLeftForward);
            motorBackRight.setPower(rightPower * overallPower * slowSpeedMultiplier * motorRightForward);
        }

    }
}
