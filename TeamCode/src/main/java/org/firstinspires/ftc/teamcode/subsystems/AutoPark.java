package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Drive.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAuto;

import java.util.List;
public class AutoPark {
    ElapsedTime timer;
    MechanumDrive driveSystem;
    RobotAuto autoMain;

    public AutoPark(ElapsedTime timer, MechanumDrive driveSystem, RobotAuto autoMain){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.autoMain = autoMain;
    }

    public void driveAutonomously() {
        driveSystem.moveRight(1,1000);
        driveSystem.stopAllMotors();
    }
}
