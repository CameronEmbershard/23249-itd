package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAuto;

import java.util.List;

public class AutoDriveAndScore {

    ElapsedTime timer;
    MechanumDrive driveSystem;
    org.firstinspires.ftc.teamcode.drive.ArmSystem armSystem;
    RobotAuto autoMain;
    StageSwitchingPipeline vision;

    int startside;


    public AutoDriveAndScore(ElapsedTime timer, MechanumDrive driveSystem, ArmSystem armSystem, RobotAuto autoMain, StageSwitchingPipeline vision){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.armSystem = armSystem;
        this.autoMain = autoMain;
        this.vision = vision;
    }

    public void driveAutonomously() {
        for(int i = 0; i < 500; i++){
            armSystem.ControlGripper(true, false); //close the gripper to keep the piece
        }
        autoMain.sleep(500); //give a little time for the gripper to put some grip on the piece
        armSystem.setTargetPosArm(110); //move up the arm to the max pos
        driveSystem.moveBackward(1,250); //back-up to a safe distance from the starting area
        autoMain.sleep(1000);
        driveSystem.rotateRight(1,750); //rotate 90 degrees to the left
        autoMain.sleep(1500);
        driveSystem.moveForward(0.5, 500);
        autoMain.sleep(1350);
        driveSystem.rotateLeft(1,750);
        autoMain.sleep(1000);
        armSystem.setTargetPosArm(112);
        driveSystem.moveBackward(0.5, 1400); //move forward till we are at the drop off area plus a little margin of error
        autoMain.sleep(1850);
        armSystem.setTargetPosArm(135);
        autoMain.sleep(1450); //wait for the piece to fall out
        driveSystem.moveForward(0.1,500);
        autoMain.sleep(1000);
        for(int i = 0; i < 500; i++) {
            armSystem.ControlGripper(false, true); //open gripper for basket
        }
        driveSystem.moveStop(); //stop for the basket
        driveSystem.moveForward(0.5,1500);
        autoMain.sleep(1500);
        driveSystem.rotateRight(1,750); //rotate 90 degrees to the left
        autoMain.sleep(1500);
        driveSystem.moveBackward(0.5,2000);
        autoMain.sleep(1500);
        autoMain.stop();
        //driveSystem.rotateRight(0.75, 1000); //do a scanning 75 degree turn right
        //while(true){
        //    startside = vision.getSide();
        //    driveSystem.stopAllMotors();
        //    if(startside == -1){
        //        driveSystem.rotateLeft(1,Integer.MAX_VALUE);
        //        while(vision.getSide() == -1){
        //            autoMain.sleep(100);
        //        }
        //        driveSystem.stopAllMotors();
        //    }
        //    else if(startside == 1){
        //        driveSystem.rotateRight(1,Integer.MAX_VALUE);
        //        while(vision.getSide() == 1){
        //            autoMain.sleep(100);
       //         }
       //         driveSystem.stopAllMotors();
        //    }
        //    else{
        //        driveSystem.rotateLeft(0.5,Integer.MAX_VALUE);
        //        while(vision.getSide() == 0){
        //            autoMain.sleep(250);
        //        }
        //    }
        //    driveSystem.moveForward(1, 1000);
        //    autoMain.sleep(500);
        //}
    }
}
