package org.firstinspires.ftc.teamcode.subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

//detection pipeline
public class StageSwitchingPipeline extends OpenCvPipeline
{

    Mat Coloredframe = new Mat();
    Mat thresholdMat = new Mat();
    Mat all = new Mat();
    int side;
    List<MatOfPoint> contoursList = new ArrayList<>();

    enum Stage
    {//color difference. greyscale
        detection,//includes outlines
        THRESHOLD,//b&w
        RAW_IMAGE,//displays raw view
    }

    private Stage stageToRenderToViewport = Stage.detection;
    private final Stage[] stages = Stage.values();

    public int getSide(){
        return side;
    }

    @Override
    public void onViewportTapped()
    {
        /*
         * Note that this method is invoked from the UI thread
         * so whatever we do here, we must do quickly.
         */

        int currentStageNum = stageToRenderToViewport.ordinal();

        int nextStageNum = currentStageNum + 1;

        if(nextStageNum >= stages.length)
        {
            nextStageNum = 0;
        }

        stageToRenderToViewport = stages[nextStageNum];
    }

    @Override
    public Mat processFrame(Mat frame)
    {
        contoursList.clear();
        /*
         * This pipeline finds the contours of yellow blobs such as the Gold Mineral
         * from the Rover Ruckus game.
         */

        //color diff cb.
        //lower cb = more blue = skystone = white
        //higher cb = less blue = yellow stone = grey
        //Imgproc.cvtColor(frame, yCbCrChan2Mat, Imgproc.COLOR_RGB2GRAY);//converts rgb to gray
        Core.extractChannel(frame, Coloredframe, 1);//takes green frame and adds it in
        Core.extractChannel(frame, Coloredframe, 2);//takes blue frame and adds it in

        //b&w
        Imgproc.threshold(Coloredframe, thresholdMat, 0, 150, Imgproc.THRESH_BINARY_INV);

        //outline/contour
        Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        thresholdMat.copyTo(all);//copies mat object
        Imgproc.drawContours(all, contoursList, -1, new Scalar(255, 0, 0), 3, 8);//draws blue contours

        int numLeft = 0;
        int numRight = 0;
        try{
            List<MatOfPoint> MatOfPoints = contoursList;
            for(MatOfPoint MatPoint: MatOfPoints){
                List<Point> MatPoint2 = MatPoint.toList();
                for(Point point: MatPoint2) {
                    if(point.x > 340){
                        numRight += 1;
                    }
                    else if(point.x < 300){
                        numLeft += 1;
                    }
                }
            }
        }finally {
            if(numLeft > numRight){
                side = -1;
            }
            else if(numRight > numLeft){
                side = 1;
            }
            else{
                side = 0;
            }
        }

        switch (stageToRenderToViewport)
        {
            case THRESHOLD:
            {
                return thresholdMat;
            }

            case detection:
            {
                return all;
            }

            default:
            {
                return frame;
            }
        }
    }

}