/*
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAutoRedScoringSide;


public class AutoDriveForward {

    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;

    // The number of ticks that represent a full rotation
    final double ticksPerRotation = 537.6;
    final double driveTime = 8.0;
    final double drivePower = 0.1;

    ElapsedTime timer;
    MechanumDrive driveSystem;
    RobotAutoRedScoringSide autoMain;

    double ticksPerMillimeter;

    public AutoDriveForward(ElapsedTime timer, MechanumDrive driveSystem, RobotAutoRedScoringSide autoMain){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.autoMain = autoMain;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);
    }

    public void driveAutonomously() {
        driveSystem.setToEncoderMode();
        driveSystem.reverseDirections(true, false, true, false);

        timer.reset();

        driveSystem.resetEncoder();

        driveSystem.setTargetPositions(
                centimetersToTicks(78.74), centimetersToTicks(78.74),
                centimetersToTicks(78.74), centimetersToTicks(78.74));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

//        autoMain.sleep(driveTime * 1000);
//        while (timer.seconds() < driveTime) {
//            autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
//                    driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
//        }

        autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
                driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
        autoMain.addTelemetry("Wowza", 1);

        timer.reset();

        driveSystem.setTargetPositions(
                centimetersToTicks(30), centimetersToTicks(30),
                centimetersToTicks(30), centimetersToTicks(30));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);
        while (timer.seconds() < driveTime / 2) {


            autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
                    driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
        }
    }

    private double millimetersToTicks(double millimeters){
        return millimeters * ticksPerMillimeter;
    }

    private double centimetersToTicks(double centimeters){
        return centimeters * 10 * ticksPerMillimeter;
    }
}
*/