package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled//@TeleOp(name = "TwoMotorAndServo")
public class TwoMotorAndServo_Old extends OpMode{

    DcMotor motorRight;
    DcMotor motorLeft;
    //Servo servoRight;
    //Servo servoLeft;

    final double servoRightOpenPosition = 0;
    final double servoRightClosedPosition = -0.25;

    final double servoLeftOpenPosition = 0;
    final double servoLeftClosedPosition = 0.25;


    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");

        // servoLeft = hardwareMap.servo.get("servoLeft");
    }

    @Override
    public void loop()
    {
        twoMotorDrive();
    }

    private void twoMotorDrive()
    {
        double xDir = gamepad1.right_stick_x; // 0
        double yDir = -gamepad1.left_stick_y; // 0.5

        double overallPower = Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2)); // 0.5
        double maxPowerMultiplier = 1 / overallPower; // 2

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
                leftPower = ((yDir * maxPowerMultiplier) + 0.5) * 2; // 3
            }
            else
            {
                leftPower = ((yDir * maxPowerMultiplier) - 0.5) * 2;
            }
        }

        motorRight.setPower(rightPower * overallPower);
        motorLeft.setPower(leftPower * overallPower * -1);
    }

    boolean readyForToggle = false;
    boolean toggled = false;
//    private void servoSpin() {
//        if (gamepad1.a) {
//            if (!toggled) {
//                readyForToggle = true;
//                servo.setPosition(servoClosedPosition);
//            }
//            else
//            {
//                readyForToggle = false;
//                servo.setPosition(servoOpenPosition);
//            }
//        }
//        else
//        {
//            if (readyForToggle) {
//                toggled = true;
//                servo.setPosition(servoClosedPosition);
//            }
//            else
//            {
//                toggled = false;
//                servo.setPosition(servoOpenPosition);
//            }
//        }
//    }
}
