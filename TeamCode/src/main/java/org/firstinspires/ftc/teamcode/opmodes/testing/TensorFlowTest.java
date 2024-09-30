/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

/*
 * This OpMode illustrates the basics of TensorFlow Object Detection, using
 * the easiest way.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@Disabled//@TeleOp(name = "TensorFlowTest", group = "Camera")
public class TensorFlowTest extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    DcMotor motorRight;
    DcMotor motorLeft;

    int motorRightForward = 1;
    int motorLeftForward = -1;

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {

        initTfod();
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                telemetryTfod();

                List<Recognition> recognitions = tfod.getRecognitions();
                int recognitionIndex = findHighestConfidence(tfod.getRecognitions());
                if(recognitionIndex != -1){
                    String pixelPosition = interpretRecognitions(recognitions, recognitionIndex);

                    moveToPosition(pixelPosition, recognitions, recognitionIndex);

                }



                // Push telemetry to the Driver Station.
                telemetry.update();

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }

                // Share the CPU.
                sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

    }   // end runOpMode()

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor the easy way.
        tfod = TfodProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), tfod);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                BuiltinCameraDirection.BACK, tfod);
        }

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

    }   // end method telemetryTfod()

    // Default resolution: 640 x 480 px

    private String interpretRecognitions(List<Recognition> recognitions, int recognitionIndex){
        double centerX = (recognitions.get(recognitionIndex).getLeft() + recognitions.get(recognitionIndex).getRight()) / 2.0;

        if(centerX < 220){
            telemetry.addData("Pixel: ", "On Left");
            return "Left";
        }
        else if(centerX > 420){
            telemetry.addData("Pixel: ", "On Right");
            return "Right";
        }
        else {
            telemetry.addData("Pixel: ", "In Middle");
            return "Middle";
        }
    }

    private void moveToPosition(String position, List<Recognition> recognitions, int recognitionIndex){
        if(position == "Left"){
            rotateLeft(recognitions,recognitionIndex);
            moveForward();}
        else if (position=="Right"){
            rotateRightUntilFacingPixel(recognitions, recognitionIndex);
            moveForward();}
        else if (position=="Middle"){
            moveForward(); }


    }

    private void rotateRightUntilFacingPixel(List<Recognition> recognitions, int recognitionIndex){
        double centerX = (recognitions.get(recognitionIndex).getLeft() + recognitions.get(recognitionIndex).getRight()) / 2.0;
//        while(false) rotateRightUntilFacingPixel();
        // Rotate Right
        motorRight.setPower(motorRightForward * -1);
        motorLeft.setPower(motorLeftForward * 1);
    }

    private void rotateLeft(List<Recognition> recognitions, int recognitionIndex){
        motorRight.setPower(1);
        motorLeft.setPower(-1);
    }

    private void moveForward(){
        motorRight.setPower(1);
        motorLeft.setPower(1);
    }

    private void dropPixel(){

    }


    private int findHighestConfidence(List<Recognition> recognitions){
        if(recognitions.size() >= 1){
            int recognitionIndex = 0;
            double highestConfidence = recognitions.get(0).getConfidence();

            for(int i = 1; i < recognitions.size(); i++){
                double currConfidence = recognitions.get(i).getConfidence();

                if(currConfidence > highestConfidence){
                    highestConfidence = currConfidence;
                    recognitionIndex = i;
                }
            }

            return recognitionIndex;
        }
        else {
            return -1;
        }

    }

}   // end class
