package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.opmodes.teleop.RobotTeleopMain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class MechanumDrive {
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackLeft;
    DcMotor motorBackRight;

    final double speedMultiplier = 0.8;
    final double slowSpeedMultiplier = 0.2;

    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;
    final double ticksPerRotation = 2200;
    double ticksPerMillimeter;

    public MechanumDrive(DcMotor motorFrontLeft, DcMotor motorFrontRight,
                         DcMotor motorBackLeft, DcMotor motorBackRight) {
        this.motorFrontLeft = motorFrontLeft;
        this.motorFrontRight = motorFrontRight;
        this.motorBackLeft = motorBackLeft;
        this.motorBackRight = motorBackRight;
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);
    }

    // Code taken from https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
    public void drive(double strafeX, double strafeY, double rotationX, double moveSlow) {
        double y = strafeY; // Remember, Y stick value is reversed
        double x = strafeX * 1.1; // Counteract imperfect strafing
        double rx = rotationX;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        double overallPower = (moveSlow > 0) ? slowSpeedMultiplier : speedMultiplier;

        motorFrontLeft.setPower(frontLeftPower * overallPower);
        motorBackLeft.setPower(backLeftPower * overallPower);
        motorFrontRight.setPower(frontRightPower * overallPower);
        motorBackRight.setPower(backRightPower * overallPower);
    }

    public void setToEncoderMode(){
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void reverseDirections(boolean reverseFrontLeft, boolean reverseFrontRight, boolean reverseBackLeft, boolean reverseBackRight){
        if(reverseFrontLeft) motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        else motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        if(reverseFrontRight) motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        else motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);

        if(reverseBackLeft) motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        else motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        if(reverseBackRight) motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        else motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void moveForward(double speed){

        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);
        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
    }

    public void moveBackward(){

        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(-1);
        motorFrontRight.setPower(-1);
        motorBackLeft.setPower(-1);
        motorBackRight.setPower(-1);
    }
    public void moveLeft(){

        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(-1);
        motorFrontRight.setPower(-1);
        motorBackLeft.setPower(1);
        motorBackRight.setPower(1);
    }

    public void moveRight(){

        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(1);
        motorFrontRight.setPower(1);
        motorBackLeft.setPower(-1);
        motorBackRight.setPower(-1);
    }

    public void moveStop(){
        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    public void rotateRight(){
        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(-1);
        motorFrontRight.setPower(1);
        motorBackLeft.setPower(-1);
        motorBackRight.setPower(1);
    }

    public void rotateLeft(){
        //to be able to use run to position mode it has to be declared after setting a position
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setPower(1);
        motorFrontRight.setPower(-1);
        motorBackLeft.setPower(1);
        motorBackRight.setPower(-1);
    }

    public void resetEncoder(){
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setTargetPositions(double frontLeft, double frontRight, double backLeft, double backRight){
        motorFrontLeft.setTargetPosition((int) frontLeft);
        motorFrontRight.setTargetPosition((int) frontRight);
        motorBackLeft.setTargetPosition((int) backLeft);
        motorBackRight.setTargetPosition((int) backRight);
    }

    public void moveToTargetPositions(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower){
        motorFrontLeft.setPower(frontLeftPower);
        motorFrontRight.setPower(frontRightPower);
        motorBackLeft.setPower(backLeftPower);
        motorBackRight.setPower(backRightPower);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public int getMotorTicks(int index){
        if(index == 0){
            return motorFrontLeft.getCurrentPosition();
        } else if(index == 1){
            return motorFrontRight.getCurrentPosition();
        } else if(index == 2){
            return motorBackLeft.getCurrentPosition();
        } else {
            return motorBackRight.getCurrentPosition();
        }
    }

    public void stopAllMotors(){
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    public boolean allMotorsStopped(){
        return motorFrontLeft.getPower() == 0 && motorFrontRight.getPower() == 0 && motorBackLeft.getPower() == 0 && motorBackRight.getPower() == 0;
    }
}