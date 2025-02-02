package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Canvas;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
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



    public void init() {



    }

    @Override
    public void loop() {
    }

    public int[] getSide(){
        return pipeline.getAnalysis();
    }

}

public class VisionProcessor implements org.firstinspires.ftc.vision.VisionProcessor {


    //create the Images storing variables required for vision
    Mat hsvMat = new Mat();
    Mat binaryMat = new Mat();

    //upper and lower color to be found
    Scalar lower = new Scalar(0,0,0);
    Scalar upper = new Scalar(255,0,0);

    int location = 0; // output
    int percentage = 0;

    int height;
    int width;

    // Rectangle regions to be scanned
    Point topLeft1 = new Point(0, 0), bottomRight1 = new Point(height, width/2);
    Point topLeft2 = new Point(0, width/2), bottomRight2 = new Point(height, width);

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        //turn the image into a coloring style thats easier to process
        Imgproc.cvtColor(frame,hsvMat,Imgproc.COLOR_RGB2HSV);

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
                    percentage += 1;
                }
            }
        }

        for (int i = (int) topLeft2.x; i <= bottomRight2.x; i++) {
            for (int j = (int) topLeft2.y; j <= bottomRight2.y; j++) {
                if (binaryMat.get(i, j)[0] >= 50) {
                    w2++;
                    percentage += 1;
                }
            }
        }

        percentage /= (int) ((bottomRight2.x - topLeft1.x) * (bottomRight2.y - topLeft2.y));

        // Determine object location
        if (w1 > w2) {
            location = 1;
        } else if (w1 < w2) {
            location = 2;
        }
        if(w1 <= 50 || w2 <= 50){
            location = 0;
        }

        return binaryMat;
    }

    public int[] getAnalysis() {
        return new int[] {location, percentage};
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}