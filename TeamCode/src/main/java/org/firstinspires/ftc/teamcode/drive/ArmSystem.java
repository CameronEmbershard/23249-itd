package org.firstinspires.ftc.teamcode.Drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp (name = "ArmControl")
public class ArmSystem extends OpMode {

    DcMotor motorLiftArm;
    DcMotor motorRotateArm;
    Servo servoGrabber;
    Servo servoGrabber2;

    //Servo servoArm;

    final double liftArmSpeed = 1;
    final double liftArmUpSpeed = liftArmSpeed * -1;
    final double liftArmDownSpeed = liftArmSpeed;
    final double liftArmHoverPower = 0.5;

    final double rotateArmSpeed = 0.09;
    final double rotateArmUpSpeed = rotateArmSpeed;
    final double rotateArmDownSpeed = -0.07;
    final double liftArmHoverPower2 = 0.01;

    private int hoverPoint;
    private int hoverPoint2 = 0;

    static final double INCREMENT   = 0.02;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1;     // Maximum rotational position
    static final double MIN_POS     =  0;     // Minimum rotational position

    static final double MED_POS = 0.5; // Medium rotational pos CIC

    double  positionArm = MAX_POS; // Start at max position
    double grabber = MIN_POS;
    double grabber2 = MAX_POS;



    //final int liftArmHighestTicks = -1900;

    //get the hoverPoint so it can be passed to main for telemetry
    public int getHoverPoint(){
        return hoverPoint;
    }

    //get the second hoverPoint so it can be passed to main for telemetry
    public int getHoverPoint2(){
        return hoverPoint2;
    }

    //get the Grabber's position so it can be passed to main for telemetry
    public double getPositionGrabber(){
        return grabber;
    }

    //get the Grabber's position so it can be passed to main for telemetry
    public double getPositionGrabber2(){
        return grabber2;
    }

    //get the Arm's position so it can be passed to main for telemetry
    public double getPositionArm(){
        return positionArm;
    }

    public double getPositionRotateArm(){ return motorRotateArm.getCurrentPosition(); }

    @Override
    public void init() {
        //if everything explodes do this
        //motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoGrabber2 = hardwareMap.servo.get("servoGrabber2");
        servoGrabber.setPosition(MAX_POS);
        servoGrabber2.setPosition(MIN_POS);
    }

    //this is called in main and setups all the variables for this script
    public ArmSystem(DcMotor motorLiftArm, DcMotor motorRotateArm, Servo servoGrabber, Servo servoGrabber2){
        this.motorLiftArm = motorLiftArm;
        this.motorRotateArm = motorRotateArm;
        this.servoGrabber = servoGrabber;
        this.servoGrabber2 = servoGrabber2;
        //this.servoArm = servoArm;

        motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRotateArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRotateArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public ArmSystem(Servo servoGrabber){
        this.servoGrabber = servoGrabber;
    }
    public void setTargetPosArm(int targetPosition)
    {
        //set the target hover point to the position found above and move the arm at a set speed to hold it
        motorRotateArm.setTargetPosition(targetPosition);
        motorRotateArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorRotateArm.setPower(liftArmHoverPower);
    }

    //this is so that java doesn't get mad
    @Override
    public void loop() {}


    //controls the gripper takes in a boolean(binded to B on gpad2 in main)
    public void ControlGripper(boolean Close, boolean Open){
        //if the B-button is not being pressed close the gripper
        if (Close) {
            grabber -= 0.05;
            grabber2 += 0.05;
        }
        if (Open){
            grabber += 0.05;
            grabber2 -= 0.05;
        }
        servoGrabber.setPosition(grabber);
        servoGrabber2.setPosition(grabber2);


    }

    //controls the Arm. Takes in an up(Y button), down(A button), and variable to go to half position(Binded to X-button)
    public void ControlArm(boolean Up, boolean Down, boolean Half, boolean half2){
        // if Y button being pushed add increment to the arm position if not at max position
        if (Up) {
            // Keep stepping up until we hit the max value.
            if(positionArm < MAX_POS){
                positionArm += INCREMENT ;
            }
        }
        // if Y button being pushed subtract increment to the arm position if not at min position
        else if(Down){
            // Keep stepping down until we hit the min value.
            if(positionArm > MIN_POS){
                positionArm -= INCREMENT;
            }
        }
        //if x-button being pushed move to half max position
        //send new position to servo
        /*if(Half){
            servoArm.setPosition(0.63);
        }
        else if(half2){
            servoArm.setPosition(1);
        }
        else{
            servoArm.setPosition(positionArm);
        }*/

    }

    //controls the rotating arm in a rotate up and down command(binded to right-bumper and right-trigger)
    public void restrictedControlArmRotate(double moveArmUp, double moveArmDown) {
        int hoverPoint2Chk;
        //if the right-trigger being pressed turn the slide on at a set power
        if (moveArmUp != 0.0) {
            //motorRotateArm.setMode(DcMotor.RunMode.RUN_WITH_ENCODER);
            motorRotateArm.setTargetPosition(220);
            motorRotateArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorRotateArm.setPower(rotateArmUpSpeed);
            //sleep(10);
            //get the current position of the motor/viper-slide for the hover code
            hoverPoint2 = motorRotateArm.getCurrentPosition();
            //if the right-trigger is being pressed turn the rotate arm motor on at a set power level
            //the right-trigger outputs a double value where 1 is being pressed fully and 0 is not being pressed
        } else if (moveArmDown != 0.0) { // right bumper pressed
           motorRotateArm.setTargetPosition(10);
            motorRotateArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorRotateArm.setPower(rotateArmDownSpeed);

            //get the current position of the rotating arm for the hover code
            hoverPoint2 = motorRotateArm.getCurrentPosition();
        } else if(motorRotateArm.getCurrentPosition() > 1) {
            //set the target hover point to the position found above and move the arm at a set speed to hold it
            hoverPoint2Chk = motorRotateArm.getCurrentPosition();
            if(hoverPoint2Chk != hoverPoint2) {
                motorRotateArm.setTargetPosition(hoverPoint2);
                //unlike above we have to use the encoder to be able to run to a set position
                motorRotateArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorRotateArm.setPower(liftArmHoverPower2);
            }

        }
        else {
            motorRotateArm.setPower(0);
        }
    }


    //controls the viper-slide takes in a move up and down command(binded to left-bumper and left-trigger)
    public void restrictedControlArmLift(boolean moveArmUp, boolean moveArmDown){

        //if the right-trigger is being pressed turn the slide on at a set power level
        //the right-trigger outputs a double value where 1 is being pressed fully and 0 is not being pressed
            if(moveArmUp)
            {
                motorLiftArm.setPower(liftArmUpSpeed);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                //get the current position of the motor/viper-slide for the hover code
                hoverPoint = motorLiftArm.getCurrentPosition();
            }
            //if the left-bumper being pressed turn the slide on at a set power
            else if(moveArmDown)
            {
                motorLiftArm.setPower(liftArmDownSpeed);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                hoverPoint = motorLiftArm.getCurrentPosition();
            }
            else
            {
                //set the target hover point to the position found above and move the arm at a set speed to hold it
                if (hoverPoint < 300){
                    motorLiftArm.setTargetPosition(hoverPoint);
                    //unlike above we have to use the encoder to be able to run to a set position
                    motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorLiftArm.setPower(liftArmHoverPower);
                }
                else{
                    motorLiftArm.setPower(0);
                }
            }
    }
}