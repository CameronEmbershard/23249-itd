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
    VisionHandler vision;

    public AutoDriveAndScore(ElapsedTime timer, MechanumDrive driveSystem, ArmSystem armSystem, RobotAuto autoMain, VisionHandler vision){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.armSystem = armSystem;
        this.autoMain = autoMain;
        this.vision = vision;
    }

    public void driveAutonomously() {
        armSystem.ControlGripper(false, true);
        autoMain.sleep(350);
        armSystem.setTargetPosArm(4384);    
        driveSystem.moveBackward();
        autoMain.sleep(250);
        driveSystem.rotateLeft();
        autoMain.sleep(255);
        driveSystem.moveForward(0.5);
        autoMain.sleep(1550);
        driveSystem.moveStop();
        armSystem.ControlGripper(true, false);
        autoMain.sleep(1000);
        driveSystem.rotateRight();
        autoMain.sleep(750);
        while(true){
            driveSystem.stopAllMotors();
            if(vision.getSide() == 1){
                driveSystem.rotateLeft();
                while(vision.getSide() == 1){
                    autoMain.sleep(100);
                }
                driveSystem.stopAllMotors();
            }
            else{
                driveSystem.rotateRight();
                while(vision.getSide() == 2){
                    autoMain.sleep(100);
                }
                driveSystem.stopAllMotors();
            }
            driveSystem.moveForward(1);
            autoMain.sleep(500);
        }
    }
}
