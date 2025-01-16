package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Drive.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAuto;

import java.util.List;

public class AutoDriveAndScore {

    ElapsedTime timer;
    MechanumDrive driveSystem;
    ArmSystem armSystem;
    RobotAuto autoMain;

    public AutoDriveAndScore(ElapsedTime timer, MechanumDrive driveSystem, ArmSystem armSystem, RobotAuto autoMain){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.armSystem = armSystem;
        this.autoMain = autoMain;

    }

    public void driveAutonomously() {
        armSystem.setTargetPosArm(4384);
        armSystem.ControlGripper(true, false);
        driveSystem.moveLeft(1447);
        driveSystem.rotateLeft(100);
        armSystem.setTargetPosArm(0);
        armSystem.ControlGripper(false, true);
    }
}
