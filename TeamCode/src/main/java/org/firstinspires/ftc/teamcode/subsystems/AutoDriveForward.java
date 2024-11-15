
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.auto.UniversalAutoAndPark;


public class AutoDriveForward {

    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;

    // The number of ticks that represent a full rotation
    final double ticksPerRotation = 537.6;
    final double driveTime = 8.0;
    final double drivePower = 0.1;

    ElapsedTime timer;
    MechanumDrive driveSystem;
    UniversalAutoAndPark autoMain;

    double ticksPerMillimeter;

    public AutoDriveForward(ElapsedTime timer, MechanumDrive driveSystem, UniversalAutoAndPark autoMain){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.autoMain = autoMain;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);
    }

    public void driveAutonomously() {
        driveSystem.reverseDirections(true, false, true, false);

        timer.reset();

        driveSystem.resetEncoder();

        driveSystem.moveRight();
        autoMain.sleep(500);
        driveSystem.stopAllMotors();
        autoMain.stop();

//        autoMain.sleep(driveTime * 1000);
//        while (timer.seconds() < driveTime) {
//            autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
//                    driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
//        }


    }

    private double millimetersToTicks(double millimeters){
        return millimeters * ticksPerMillimeter;
    }

    private double centimetersToTicks(double centimeters){
        return centimeters * 10 * ticksPerMillimeter;
    }
}