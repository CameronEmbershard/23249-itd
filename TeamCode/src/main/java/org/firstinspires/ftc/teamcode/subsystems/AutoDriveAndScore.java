package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAuto;

import java.util.List;

public class AutoDriveAndScore {
    // GoBilda Mecanum Wheel Diameter in Millimeters
    final double wheelDiameter = 96;

    ElapsedTime timer;
    MechanumDrive driveSystem;
    ArmSystem armSystem;
    RobotAuto autoMain;


    double ticksPerMillimeter;

    public AutoDriveAndScore(ElapsedTime timer, MechanumDrive driveSystem, ArmSystem armSystem, RobotAuto autoMain){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.armSystem = armSystem;
        this.autoMain = autoMain;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);
    }

    public void driveAutonomously() {
        armSystem.setTargetPosArm(4384);
        armSystem.ControlGripper(false);
        driveSystem.moveLeft(57);
    }
}
