
package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.UniversalAutoAndPark;


public class AutoDriveForward {
    DcMotor motorRight;
    DcMotor motorLeft;
    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;

    // The number of ticks that represent a full rotation
    final double ticksPerRotation = 537.6;
    final double driveTime = 8.0;
    final double drivePower = 0.1;
    int motorRightForward = 1;
    int motorLeftForward = -1;

    final double speedMultiplier = 0.5;
    final double slowSpeedMultiplier = 0.1;


    ElapsedTime timer;

    UniversalAutoAndPark autoMain;



    double ticksPerMillimeter;

    public AutoDriveForward(ElapsedTime timer, MechanumDrive driveSystem, UniversalAutoAndPark autoMain, ArmSystem armSystem){
        this.timer = timer;

        this.autoMain = autoMain;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);

        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");

    }

    public void driveAutonomously() {
        drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.b, gamepad1.x);
        timer.reset();


        motorLeft.setPower(1);
        motorRight.setPower(1);

        autoMain.sleep(1800);


//        autoMain.sleep(driveTime * 1000);
//        while (timer.seconds() < driveTime) {
//            autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
//                    driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
//        }


    }

    synchronized private void drive(double xDir, double yDir, boolean bPressed, boolean xPressed) {
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
        if (xPressed){
            motorLeft.setPower(1);
            motorRight.setPower(1);
            autoMain.sleep(1);

            motorLeft.setPower(-1);
            motorRight.setPower(-1);

            autoMain.sleep(1);

            motorLeft.setPower(1);
            motorRight.setPower(1);

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


    private double millimetersToTicks(double millimeters){
        return millimeters * ticksPerMillimeter;
    }

    private double centimetersToTicks(double centimeters){
        return centimeters * 10 * ticksPerMillimeter;
    }
}