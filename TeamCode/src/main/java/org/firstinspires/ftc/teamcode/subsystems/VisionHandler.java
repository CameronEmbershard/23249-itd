package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import java.util.ArrayList;

public class VisionHandler extends OpMode {
    static final int STREAM_WIDTH = 640; // modify for your camera
    static final int STREAM_HEIGHT = 480; // modify for your camera
    OpenCvWebcam webcam;
    SamplePipeline pipeline;
    public void init(){
    }

    public void init(WebcamName webcamName) {

        //some setup stuff I don't understand
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, 0);
        pipeline = new SamplePipeline();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(STREAM_WIDTH, STREAM_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });

    }

    @Override
    public void loop() {
    }

    public int getSide(){
        return pipeline.getAnalysis();
    }

}

class SamplePipeline extends OpenCvPipeline {


    //create the Images storing variables required for vision
    Mat hsvMat = new Mat();
    Mat binaryMat = new Mat();

    //upper and lower color to be found
    Scalar lower = new Scalar(0,0,0);
    Scalar upper = new Scalar(255,0,0);

    int location = 0; // output

    // Rectangle regions to be scanned
    Point topLeft1 = new Point(0, 0), bottomRight1 = new Point(320, 480);
    Point topLeft2 = new Point(320, 0), bottomRight2 = new Point(640, 480);

    @Override
    public void init(Mat firstFrame) {
        processFrame(firstFrame);
    }

    @Override
    public Mat processFrame(Mat input) {
        //turn the image into a coloring style thats easier to process
        Imgproc.cvtColor(input,hsvMat,Imgproc.COLOR_RGB2HSV);

        //find the colors that go in the range between lower and upper
        //output it to binaryMat
        Core.inRange(hsvMat,lower,upper,binaryMat);

        // Scan both rectangle regions, keeping track of how many
        // pixels meet the threshold value, indicated by the color white
        // in the binary image
        double w1 = 0, w2 = 0;
        // process the pixel value for each rectangle  (255 = W, 0 = B)
        for (int i = (int) topLeft1.x; i <= bottomRight1.x; i++) {
            for (int j = (int) topLeft1.y; j <= bottomRight1.y; j++) {
                if (binaryMat.get(i, j)[0] >= 50) {
                    w1++;
                }
            }
        }

        for (int i = (int) topLeft2.x; i <= bottomRight2.x; i++) {
            for (int j = (int) topLeft2.y; j <= bottomRight2.y; j++) {
                if (binaryMat.get(i, j)[0] >= 50) {
                    w2++;
                }
            }
        }

        // Determine object location
        if (w1 > w2) {
            location = 1;
        } else if (w1 < w2) {
            location = 2;
        }

        return binaryMat;
    }

    public int getAnalysis() {
        return location;
    }
}