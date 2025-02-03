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

    private final int rows = 640;
    private final int cols = 480;

    private static int valMid = -1;
    private static int valLeft = -1;
    private static int valRight = -1;

    private static float rectHeight = 1f;
    private static float rectWidth = 0.75f;

    private static float[] leftPos = {0f, 0.5f};
    private static float[] rightPos = {0.5f, 0.5f};

    Mat yCbCrChan2Mat = new Mat();
    Mat thresholdMat = new Mat();
    Mat all = new Mat();
    List<MatOfPoint> contoursList = new ArrayList<>();

    enum Stage
    {//color difference. greyscale
        detection,//includes outlines
        THRESHOLD,//b&w
        RAW_IMAGE,//displays raw view
    }

    private Stage stageToRenderToViewport = Stage.detection;
    private final Stage[] stages = Stage.values();

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

    Scalar lower = new Scalar(0,0,0);
    Scalar upper = new Scalar(255,0,0);

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
        Core.extractChannel(frame, frame, 0);//takes cb difference and stores

        //b&w
        Imgproc.threshold(frame, thresholdMat, 102, 255, Imgproc.THRESH_BINARY_INV);

        //outline/contour
        Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        frame.copyTo(all);//copies mat object
        //Imgproc.drawContours(all, contoursList, -1, new Scalar(255, 0, 0), 3, 8);//draws blue contours


        //get values from frame
        double[] pixLeft = thresholdMat.get((int)(frame.rows()* leftPos[1]), (int)(frame.cols()* leftPos[0]));//gets value at circle
        valLeft = (int)pixLeft[0];

        double[] pixRight = thresholdMat.get((int)(frame.rows()* rightPos[1]), (int)(frame.cols()* rightPos[0]));//gets value at circle
        valRight = (int)pixRight[0];

        //create three points
        Point pointLeft = new Point((int)(frame.cols()* leftPos[0]), (int)(frame.rows()* leftPos[1]));
        Point pointRight = new Point((int)(frame.cols()* rightPos[0]), (int)(frame.rows()* rightPos[1]));

        //draw circles on those points
        Imgproc.circle(all, pointLeft,5, new Scalar( 255, 0, 0 ),1 );//draws circle
        Imgproc.circle(all, pointRight,5, new Scalar( 255, 0, 0 ),1 );//draws circle

        //draw 3 rectangles
        Imgproc.rectangle(//1-3
                all,
                new Point(
                        frame.cols()*(leftPos[0]-rectWidth/2),
                        frame.rows()*(leftPos[1]-rectHeight/2)),
                new Point(
                        frame.cols()*(leftPos[0]+rectWidth/2),
                        frame.rows()*(leftPos[1]+rectHeight/2)),
                new Scalar(0, 255, 0), 3);
        Imgproc.rectangle(//5-7
                all,
                new Point(
                        frame.cols()*(rightPos[0]-rectWidth/2),
                        frame.rows()*(rightPos[1]-rectHeight/2)),
                new Point(
                        frame.cols()*(rightPos[0]+rectWidth/2),
                        frame.rows()*(rightPos[1]+rectHeight/2)),
                new Scalar(0, 255, 0), 3);

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