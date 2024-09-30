package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@Disabled//@Teleop(name = "TwoMotorTank")
public class TwoMotorTank extends OpMode{

    DcMotor motorRight;
    DcMotor motorLeft;


    DoubleSupplier leftStickY;
    DoubleSupplier rightStickX;
    BooleanSupplier bButton;


    int motorRightForward = 1;
    int motorLeftForward = -1;

    final double speedMultiplier = 0.5;
    final double slowSpeedMultiplier = 0.1;



    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
    }

    @Override
    public void loop()
    {
        drive();
    }

    public TwoMotorTank(DcMotor motorleft, DcMotor motorRight, DoubleSupplier leftStickY, DoubleSupplier rightStickX, BooleanSupplier bButton){
        this.motorLeft = motorleft;
        this.motorRight = motorRight;
        this.leftStickY = leftStickY;
        this.rightStickX = rightStickX;
        this.bButton = bButton;
    }

    public void drive(){

        double xDir = rightStickX.getAsDouble();
        double yDir = -leftStickY.getAsDouble();
        boolean bPressed = bButton.getAsBoolean();

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

    }

    public void drive2(){
        double xDir = rightStickX.getAsDouble();
        double yDir = -leftStickY.getAsDouble();
        boolean bPressed = bButton.getAsBoolean();

        double overallPower = Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2));
        double angle = Math.atan2(yDir, xDir);

        // sin(angle+1/4π) * magnitude
        double leftPower = Math.sin(angle + (1 / (4 * Math.PI)));

        // sin(angle−1/4π) * magnitude
        double rightPower = Math.sin(angle - (1 / (4 * Math.PI)));

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

    }
}
