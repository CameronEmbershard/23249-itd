
package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoDance;


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

    AutoDance autoMain;



    double ticksPerMillimeter;

    public AutoDriveForward(ElapsedTime timer, AutoDance autoMain, DcMotor motorRight, DcMotor motorLeft){
        this.timer = timer;

        this.autoMain = autoMain;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);

        this.motorRight = motorRight;
        this.motorLeft = motorLeft;

    }

    public void driveAutonomously() {
        timer.reset();

        motorLeft.setPower(0.5);
        motorRight.setPower(0.5);
        autoMain.sleep(1800);

        motorLeft.setPower(-0.5);
        motorRight.setPower(-0.5);

        autoMain.sleep(1500);

        motorLeft.setPower(-0.4);
        motorRight.setPower(0.4);

        autoMain.sleep(1800);

        motorLeft.setPower(0.4);
        motorRight.setPower(-0.4);
        autoMain.sleep(150);
        motorLeft.setPower(0.4);
        motorRight.setPower(0.4);
        autoMain.sleep(1500);
        motorLeft.setPower(-0.4);
        motorRight.setPower(0.4);

    }
}