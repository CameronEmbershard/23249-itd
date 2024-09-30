package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class VisionHandler {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    DcMotor motorRight;
    DcMotor motorLeft;

    final int leftBorder = 220;
    final int rightBorder = 420;

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

    private List<Recognition> recognitions;

    private static String TFOD_MODEL_ASSET;
    private static String[] LABELS;

    public VisionHandler(WebcamName webcam, boolean isBlue){

        if(isBlue){
            TFOD_MODEL_ASSET = "BlueBlockModel.tflite";
            LABELS = new String[] {"Blue", "BlueBlock"};
        }
        else{
            TFOD_MODEL_ASSET = "RedBlockModel.tflite";
            LABELS = new String[] {"RedBlock"};
        }

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                .setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();


        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(webcam);
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }

    public void updateRecognitions(){
        recognitions = tfod.getRecognitions();
    }

    public String getTargetPosition(){
        // telemetryTfod();

        updateRecognitions();
        int recognitionIndex = findHighestConfidence(recognitions, "BlueBlock");

        if(recognitionIndex != -1){
            return interpretRecognitionsTriSpike(recognitions.get(recognitionIndex));
        }

        return "Null";
    }




    // Default resolution: 640 x 480 px

    // Tri-Spike means that the picture contains all three spikes
    private String interpretRecognitionsTriSpike(Recognition recognition)   {
        double centerX = (recognition.getLeft() + recognition.getRight()) / 2.0;

        if (centerX < 220) {
//            telemetry.addData("Pixel: ", "On Left");
            return "Left";
        } else if (centerX > 420) {
//            telemetry.addData("Pixel: ", "On Right");
            return "Right";
        } else {
//            telemetry.addData("Pixel: ", "In Middle");
            return "Middle";
        }
    }

    // Single-Spike means that the picture contains just one spike
    public boolean interpretRecognitionsSingleSpike(Recognition recognition, String targetLabel)   {
        double centerX = (recognition.getLeft() + recognition.getRight()) / 2.0;

        if (centerX > 220 && centerX < 420) {
//            telemetry.addData("Pixel: ", "Detected");
            return true;
        }

        return false;
    }


    public int findHighestConfidence(List<Recognition> recognitions, String targetLabel){
        int recognitionIndex = -1;
        double highestConfidence = 0.0;

        for(int i = 0; i < recognitions.size(); i++){
            if(!recognitions.get(i).getLabel().equals(targetLabel)){
                continue;
            }

            double currConfidence = recognitions.get(i).getConfidence();

            if(currConfidence > highestConfidence){
                highestConfidence = currConfidence;
                recognitionIndex = i;
            }
        }

        return recognitionIndex;
    }

    public List<Recognition> getRecognitions(){
        return recognitions;
    }
}
