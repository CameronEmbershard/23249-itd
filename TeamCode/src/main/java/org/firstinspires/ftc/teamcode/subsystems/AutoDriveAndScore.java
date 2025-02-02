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

    int startside;


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
        armSystem.setTargetPosArm(Integer.MAX_VALUE); //move up the arm to the max pos
        driveSystem.moveBackward(1,10000); //back-up to a safe distance from the starting area
        driveSystem.rotateLeft(1,10000); //rotate 90 degrees to the left
        driveSystem.moveForward(0.5, 100000); //move forward till we are at the drop off area plus a little margin of error
        driveSystem.moveStop(); //stop for the basket
        armSystem.ControlGripper(true, false); //open gripper for basket
        autoMain.sleep(1000); //wait for the piece to fall out
        driveSystem.rotateRight(0.75, 10000); //do a scanning 75 degree turn right
        while(true){
            startside = vision.getSide()[0];
            driveSystem.stopAllMotors();
            if(startside == 1){
                driveSystem.rotateLeft(1,Integer.MAX_VALUE);
                while(vision.getSide()[0] == 1){
                    autoMain.sleep(100);
                }
                driveSystem.stopAllMotors();
            }
            else if(startside == 2){
                driveSystem.rotateRight(1,Integer.MAX_VALUE);
                while(vision.getSide()[0] == 2){
                    autoMain.sleep(100);
                }
                driveSystem.stopAllMotors();
            }
            else{
                driveSystem.rotateLeft(0.5,Integer.MAX_VALUE);
                while(vision.getSide()[0] == 0){
                    autoMain.sleep(250);
                }
            }
            driveSystem.moveForward(1, 1000);
            autoMain.sleep(500);
        }
    }
}
