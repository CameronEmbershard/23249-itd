package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.RobotAutoRedScoringSide;

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
    ArmControl armSystem;
    RobotAuto autoMain;
    VisionHandler vision;


    double ticksPerMillimeter;

    boolean isBlue;
    boolean isOnScoreSide;
    String targetLabel;

    public AutoDriveAndScore(ElapsedTime timer, MechanumDrive driveSystem, ArmControl armSystem, RobotAuto autoMain, VisionHandler vision){
        this.timer = timer;
        this.driveSystem = driveSystem;
        this.armSystem = armSystem;
        this.autoMain = autoMain;
        this.vision = vision;

        ticksPerMillimeter = ticksPerRotation / (wheelDiameter * Math.PI);
    }

    Recognition forwardRecognition;
    Recognition leftRecognition;
    Recognition rightRecognition;

    public void driveAutonomously(boolean isBlue, boolean isOnScoreSide, String targetLabel) {
        this.isBlue = isBlue;
        this.isOnScoreSide = isOnScoreSide;
        this.targetLabel = targetLabel;

        driveSystem.setToEncoderMode();
        driveSystem.reverseDirections(true, false, true, false);

        timer.reset();

        driveSystem.resetEncoder();

        autoMain.sleep(500);

        searchPaths();
        String path = findPath();


        int reverseMultiple;

        if(isBlue){
            reverseMultiple = 1;
        } else {
            reverseMultiple = -1;

            // A REALLY stupid way of fixing a problem where it goes the wrong direction, pls ignore
            if(path.equals("Left")){
                path = "Right";
            }
            else if(path.equals("Right")){
                path = "Left";
            }
        }


        if(isOnScoreSide){
            if(path.equals("Left")){
                autoMain.addTelemetry("Status", "Driving Left Park");

                scoreLeftPath(reverseMultiple);
                reverseLeftPath(reverseMultiple);
                parkForward();
            }
            else if(path.equals("Right")){
                autoMain.addTelemetry("Status", "Driving Right Park");

                scoreRightPath(reverseMultiple);
                reverseRightPath(reverseMultiple);
                parkLeft(reverseMultiple);
            }
            else if(path.equals("Middle")){
                autoMain.addTelemetry("Status", "Driving Middle Park");

                scoreMiddlePath();
                reverseMiddlePath();
                parkLeft(reverseMultiple);
            }
            else {
                autoMain.addTelemetry("Status", "Path failed");
            }

        }
        else {
            if(path.equals("Left")){
                autoMain.addTelemetry("Status", "Driving Left No Park");

                scoreLeftPath(reverseMultiple);
                reverseLeftPath(reverseMultiple);
//            parkForward();
            }
            else if(path.equals("Right")){
                autoMain.addTelemetry("Status", "Driving Right No Park");

                scoreRightPath(reverseMultiple);
                reverseRightPath(reverseMultiple);
//            parkLeft(reverseMultiple);
            }
            else if(path.equals("Middle")){
                autoMain.addTelemetry("Status", "Driving Middle No Park");

                scoreMiddlePath();
                reverseMiddlePath();
//            parkLeft(reverseMultiple);
            }
            else {
                autoMain.addTelemetry("Status", "Path failed");
            }
        }


//        driveSystem.setTargetPositions(
//                centimetersToTicks(78.74), centimetersToTicks(78.74),
//                centimetersToTicks(78.74), centimetersToTicks(78.74));
//
//        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);
//
//        while (timer.seconds() < driveTime) {
//
//
//            autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
//                    driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
//        }
//
//        autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
//                driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
//        autoMain.addTelemetry("Wowza", 1);
//
//        timer.reset();
//
//        driveSystem.setTargetPositions(
//                centimetersToTicks(30), centimetersToTicks(30),
//                centimetersToTicks(30), centimetersToTicks(30));
//
//        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);
//        while (timer.seconds() < driveTime / 2) {
//
//
//            autoMain.addTelemetry("Motor Ticks: ", driveSystem.getMotorTicks(0), driveSystem.getMotorTicks(1),
//                    driveSystem.getMotorTicks(2), driveSystem.getMotorTicks(3));
//        }
    }

    private void searchPaths(){

        armSystem.motorLiftArm.setPower(armSystem.liftArmUpSpeed * 0.83);

        double moveForward = 5;


        driveSystem.setTargetPositions(
                centimetersToTicks(moveForward), centimetersToTicks(moveForward),
                centimetersToTicks(moveForward), centimetersToTicks(moveForward));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

        autoMain.sleep(1200); // DONT CHANGE

        armSystem.motorLiftArm.setPower(0);

        driveSystem.resetEncoder();

        vision.updateRecognitions();
        List<Recognition> recognitions = vision.getRecognitions();
        String str = "";
        for(Recognition recognition : recognitions){
            str += ((recognition.getLeft() + recognition.getRight()) / 2) + " ";
        }
        autoMain.addTelemetry("PropLocations:", str);


        int recognitionIndex = vision.findHighestConfidence(vision.getRecognitions(), targetLabel);
        if(recognitionIndex != -1){
            forwardRecognition = vision.getRecognitions().get(recognitionIndex);
        }


        double rotateLeft = 8;

        driveSystem.setTargetPositions(
                centimetersToTicks(-rotateLeft), centimetersToTicks(rotateLeft),
                centimetersToTicks(-rotateLeft), centimetersToTicks(rotateLeft));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

        autoMain.sleep(1200);

        vision.updateRecognitions();
        recognitions = vision.getRecognitions();
        str = "";
        for(Recognition recognition : recognitions){
            str += ((recognition.getLeft() + recognition.getRight()) / 2) + " ";
        }
        autoMain.addTelemetry("PropLocations:", str);


        recognitionIndex = vision.findHighestConfidence(vision.getRecognitions(), targetLabel);
        if(recognitionIndex != -1){
            leftRecognition = vision.getRecognitions().get(recognitionIndex);
        }


        double rotateRight = 8;

        driveSystem.setTargetPositions(
                centimetersToTicks(rotateRight), centimetersToTicks(-rotateRight),
                centimetersToTicks(rotateRight), centimetersToTicks(-rotateRight));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

        autoMain.sleep(1800);

        vision.updateRecognitions();
        recognitions = vision.getRecognitions();
        str = "";
        for(Recognition recognition : recognitions){
            str += ((recognition.getLeft() + recognition.getRight()) / 2) + " ";
        }
        autoMain.addTelemetry("PropLocations:", str);


        recognitionIndex = vision.findHighestConfidence(vision.getRecognitions(), targetLabel);
        if(recognitionIndex != -1){
            rightRecognition = vision.getRecognitions().get(recognitionIndex);
        }


        double defaultPos = 0;

        driveSystem.setTargetPositions(
                centimetersToTicks(defaultPos), centimetersToTicks(defaultPos),
                centimetersToTicks(defaultPos), centimetersToTicks(defaultPos));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

        autoMain.sleep(1200);
    }

    private String findPath(){
        // forward: 375, *--*, 67
        // left: *146*, --, --
        // right: --, 510, *359*


        /* For left path, check if it appears ONLY when looking left at x < 200
        */

        /* For forward path, check if it is seen
            - inbetween 200 and 400 looking forward
            - x > 200 looking left
            - x < 150 looking right
         */

        /* For right path, check if it is seen
            - x > 400 looking forward
            - x > 150 looking right
         */




        double leftRecognitionX = (leftRecognition == null) ? -1 :
                (leftRecognition.getLeft() + leftRecognition.getRight()) / 2;
        double forwardRecognitionX = (forwardRecognition == null) ? -1 :
                (forwardRecognition.getLeft() + forwardRecognition.getRight()) / 2;
        double rightRecognitionX = (rightRecognition == null) ? -1 :
                (rightRecognition.getLeft() + rightRecognition.getRight()) / 2;

        if(leftRecognitionX > -1 && leftRecognitionX < 200){
            autoMain.addTelemetry("Status", "Left path");
            return "Left";
        }

        if((forwardRecognitionX > -1 && forwardRecognitionX > 400) || (rightRecognitionX > -1 && rightRecognitionX > 150)){
            autoMain.addTelemetry("Status", "Right path");
            return "Right";
        }

        if((forwardRecognitionX > -1 && forwardRecognitionX > 200 && forwardRecognitionX < 400) ||
           (leftRecognitionX > -1 && leftRecognitionX > 200) ||
           (rightRecognitionX > -1 && rightRecognitionX < 150))
        {
            autoMain.addTelemetry("Status", "Middle path");
            return "Middle";
        }


        // Default to left because it is most faulty
        autoMain.addTelemetry("Status", "Default left path");
        return "Left";
    }

    private void scoreLeftPath(int reverseMultiple){
        driveSystem.resetEncoder();

        int moveForward1 = 57;
        driveSystem.setTargetPositions(centimetersToTicks(moveForward1), centimetersToTicks(moveForward1),
                                       centimetersToTicks(moveForward1), centimetersToTicks(moveForward1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(3000);

        driveSystem.resetEncoder();

        int moveRight1 = 3 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(moveRight1), centimetersToTicks(-moveRight1),
                centimetersToTicks(-moveRight1), centimetersToTicks(moveRight1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                drivePower, drivePower);
        autoMain.sleep(500);

        driveSystem.resetEncoder();

        int turnLeft = 60 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(-turnLeft), centimetersToTicks(turnLeft),
                                       centimetersToTicks(-turnLeft), centimetersToTicks(turnLeft));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(3000);

        driveSystem.resetEncoder();

        int moveRight2 = 10 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(moveRight2), centimetersToTicks(-moveRight2),
                                       centimetersToTicks(-moveRight2), centimetersToTicks(moveRight2));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                drivePower, drivePower);
        autoMain.sleep(1200);

        driveSystem.resetEncoder();


        int moveForward2 = 18;
        driveSystem.setTargetPositions(centimetersToTicks(moveForward2), centimetersToTicks(moveForward2),
                                       centimetersToTicks(moveForward2), centimetersToTicks(moveForward2));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(2000);
    }

    private void reverseLeftPath(int reverseMultiple){
        driveSystem.resetEncoder();

        double moveBackward1 = -17.2;
        driveSystem.setTargetPositions(centimetersToTicks(moveBackward1), centimetersToTicks(moveBackward1),
                centimetersToTicks(moveBackward1), centimetersToTicks(moveBackward1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                drivePower, drivePower);
        autoMain.sleep(2000);

        driveSystem.resetEncoder();

        int moveRight1 = 50 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(-moveRight1), centimetersToTicks(moveRight1),
                                       centimetersToTicks(moveRight1), centimetersToTicks(-moveRight1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(3000);

    }


    private void scoreRightPath(int reverseMultiple){
        boolean isRedLeft = !isBlue && !isOnScoreSide;

        driveSystem.resetEncoder();

        int moveForward1 = 58;
        driveSystem.setTargetPositions(centimetersToTicks(moveForward1), centimetersToTicks(moveForward1),
                                       centimetersToTicks(moveForward1), centimetersToTicks(moveForward1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(3000);

        driveSystem.resetEncoder();

        int turnRight = (isRedLeft) ? 60 * reverseMultiple : 55 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(turnRight), centimetersToTicks(-turnRight),
                                       centimetersToTicks(turnRight), centimetersToTicks(-turnRight));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(3000);

        driveSystem.resetEncoder();

        if(isRedLeft){
            int moveRight = 25;
            driveSystem.setTargetPositions(centimetersToTicks(moveRight), centimetersToTicks(-moveRight),
                    centimetersToTicks(-moveRight), centimetersToTicks(moveRight));
            driveSystem.moveToTargetPositions(drivePower, drivePower,
                    drivePower, drivePower);
            autoMain.sleep(1200);
        }

        driveSystem.resetEncoder();

        int moveForward2 = 20;
        driveSystem.setTargetPositions(centimetersToTicks(moveForward2), centimetersToTicks(moveForward2),
                                       centimetersToTicks(moveForward2), centimetersToTicks(moveForward2));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(3000);
    }



    private void reverseRightPath(int reverseMultiple){
        driveSystem.resetEncoder();

        int moveBackward1 = -28;
        driveSystem.setTargetPositions(centimetersToTicks(moveBackward1), centimetersToTicks(moveBackward1),
                                       centimetersToTicks(moveBackward1), centimetersToTicks(moveBackward1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                drivePower, drivePower);
        autoMain.sleep(3000);

        driveSystem.resetEncoder();

        int turnLeft = -55 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(turnLeft), centimetersToTicks(-turnLeft),
                                       centimetersToTicks(turnLeft), centimetersToTicks(-turnLeft));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                drivePower, drivePower);
        autoMain.sleep(3000);
    }

    private void scoreMiddlePath(){
        driveSystem.resetEncoder();

        double moveForward = 77.5;
        driveSystem.setTargetPositions(
                centimetersToTicks(moveForward), centimetersToTicks(moveForward),
                centimetersToTicks(moveForward), centimetersToTicks(moveForward));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

        autoMain.sleep(3500);
    }


    private void reverseMiddlePath(){
        driveSystem.resetEncoder();

        int move1 = -18;
        driveSystem.setTargetPositions(
                centimetersToTicks(move1), centimetersToTicks(move1),
                centimetersToTicks(move1), centimetersToTicks(move1));

        driveSystem.moveToTargetPositions(drivePower, drivePower, drivePower, drivePower);

        autoMain.sleep(3000);
    }

    private void parkLeft(int reverseMultiple){
        driveSystem.resetEncoder();

        int move1 = 85 * reverseMultiple;
        driveSystem.setTargetPositions(centimetersToTicks(-move1), centimetersToTicks(move1),
                                       centimetersToTicks(move1), centimetersToTicks(-move1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(4000);

    }

    private void parkForward(){
        driveSystem.resetEncoder();

        int move1 = 80;
        driveSystem.setTargetPositions(centimetersToTicks(move1), centimetersToTicks(move1),
                centimetersToTicks(move1), centimetersToTicks(move1));
        driveSystem.moveToTargetPositions(drivePower, drivePower,
                                          drivePower, drivePower);
        autoMain.sleep(4000);

    }

    private double millimetersToTicks(double millimeters){
        return millimeters * ticksPerMillimeter;
    }

    private double centimetersToTicks(double centimeters){
        return centimeters * 10 * ticksPerMillimeter;
    }
}
