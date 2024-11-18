package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAuto;

import java.util.List;

public class AutoDriveAndScore {
    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;

    // The number of ticks that represent a full rotation
    final double ticksPerRotation = 537.6;
    final double driveTime = 8.0;
    final double drivePower = 0.2;

    ElapsedTime timer;
    MechanumDrive driveSystem;
    ArmSystem armSystem;
    RobotAuto autoMain;
    VisionHandler vision;


    double ticksPerMillimeter;

    boolean isBlue;
    boolean isOnScoreSide;
    String targetLabel;

    public AutoDriveAndScore(ElapsedTime timer, MechanumDrive driveSystem, ArmSystem armSystem, RobotAuto autoMain, VisionHandler vision){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.armSystem = armSystem;
        this.autoMain = autoMain;
        this.vision = vision;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);
    }

    public void driveAutonomously() {

    }
}
