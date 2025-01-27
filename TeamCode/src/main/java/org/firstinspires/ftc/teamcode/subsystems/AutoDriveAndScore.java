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
        armSystem.ControlGripper(false, true); //close the gripper to keep the piece
        autoMain.sleep(350); //give a little time for the gripper to put some grip on the piece
        armSystem.setTargetPosArm(15); //move up the arm to the max pos
        driveSystem.moveBackward(1,1000); //back-up to a safe distance from the starting area
        driveSystem.rotateLeft(1,1000); //rotate 90 degrees to the left
        driveSystem.moveForward(0.5, 1000); //move forward till we are at the drop off area plus a little margin of error
        driveSystem.moveStop(); //stop for the basket
        armSystem.ControlGripper(true, false); //open gripper for basket
        autoMain.sleep(1000); //wait for the piece to fall out
        driveSystem.rotateRight(0.75, 1000); //do a scanning 75 degree turn right
        while(true){
            driveSystem.stopAllMotors();
            if(vision.getSide() == 1){
                driveSystem.rotateLeft(1,Integer.MAX_VALUE);
                while(vision.getSide() == 1){
                    autoMain.sleep(100);
                }
                driveSystem.stopAllMotors();
            }
            else{
                driveSystem.rotateRight(1,Integer.MAX_VALUE);
                while(vision.getSide() == 2){
                    autoMain.sleep(100);
                }
                driveSystem.stopAllMotors();
            }
            driveSystem.moveForward(1, 1000);
            autoMain.sleep(500);
        }
    }
}
