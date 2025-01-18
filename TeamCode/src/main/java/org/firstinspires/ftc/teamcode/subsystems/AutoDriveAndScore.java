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
        armSystem.ControlGripper(false, false);
        autoMain.sleep(350);
        armSystem.setTargetPosArm(4384);    
        driveSystem.moveBackward();
        autoMain.sleep(250);
        driveSystem.rotateRight();
        autoMain.sleep(255);
        driveSystem.moveForward(0.5);
        autoMain.sleep(1550);
        driveSystem.moveStop();
        armSystem.ControlGripper(true, false);
    }
}
