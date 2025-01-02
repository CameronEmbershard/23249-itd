package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoDance;

public class TankAutoMain {
    DcMotor motorRight;
    DcMotor motorLeft;
    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;

    // The number of ticks that represent a full rotation
    final double ticksPerRotation = 537.6;

    ElapsedTime timer;

    TankAutoMain autoMain;



    double ticksPerMillimeter;

    public TankAutoMain(ElapsedTime timer, TankAutoMain autoMain, DcMotor motorRight, DcMotor motorLeft){
        this.timer = timer;

        this.autoMain = autoMain;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);

        this.motorRight = motorRight;
        this.motorLeft = motorLeft;

    }

    public void driveAutonomously() {
        timer.reset();

        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Motor goes BRRRRRRRRRRRRRRRR, oh, you don't know what karlson is? (gibberish noises)
        // also this is just a break in the lines for my sanity {;

        motorLeft.setPower(0.5);
        motorRight.setPower(-0.5);
        autoMain.sleep(1800);

        motorLeft.setPower(-0.4);
        motorRight.setPower(-0.4);
        autoMain.sleep(100);

    }

    private void sleep(int i) {
    }

}
