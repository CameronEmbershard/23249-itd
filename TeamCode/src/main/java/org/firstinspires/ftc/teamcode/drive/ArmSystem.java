package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp (name = "ArmControl")
public class ArmSystem extends OpMode {

    DcMotor motorLiftArm;
    DcMotor motorLiftArm2;
    Servo servoGrabber;
    Servo servoArm;

    final double liftArmSpeed = 1;
    final double liftArmUpSpeed = liftArmSpeed * -1;
    final double liftArmDownSpeed = liftArmSpeed;
    final double liftArmHoverPower = 0.5;

    private int hoverPoint;

    static final double INCREMENT   = 0.02;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1;     // Maximum rotational position
    static final double MIN_POS     =  0.15;     // Minimum rotational position

    double  positionGrabber = MAX_POS; // Start at max position
    double  positionArm = MAX_POS; // Start at max position


    private int hoverPoint2 = 0;

    final int liftArmHighestTicks = -1900;

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
        return positionGrabber;
    }

    //get the Arm's position so it can be passed to main for telemetry
    public double getPositionArm(){
        return positionArm;
    }

    @Override
    public void init() {
        //if everything explodes do this
        //motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoGrabber.setPosition(MAX_POS);
    }

    //this is called in main and setups all the variables for this script
    public ArmSystem(DcMotor motorLiftArm, Servo servoGrabber, DcMotor motorLiftArm2, Servo servoArm){
        this.motorLiftArm = motorLiftArm;
        this.motorLiftArm2 = motorLiftArm2;
        this.servoGrabber = servoGrabber;
        this.servoArm = servoArm;

        motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftArm2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public ArmSystem(Servo servoGrabber){
        this.servoGrabber = servoGrabber;
    }
    public void setTargetPosArm(int targetPosition)
    {
        motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //set the target hover point to the position found above and move the arm at a set speed to hold it
        motorLiftArm.setTargetPosition(targetPosition);
        motorLiftArm.setPower(liftArmHoverPower);
    }

    //this is so that java doesn't get mad
    @Override
    public void loop() {}


    //controls the gripper takes in a boolean(binded to B on gpad2 in main)
    public void ControlGripper(boolean Close){
        //if the B-button is not being pressed close the gripper
        if (Close) {
            //send grabber to max position
            servoGrabber.setPosition(MAX_POS);
        }
        //if the B-Button IS being pressed open the gripper
        else {
            //send grabber to min position
            servoGrabber.setPosition(MIN_POS);
        }



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
        if(Half){
            servoArm.setPosition(0.63);
        }
        else if(half2){
            servoArm.setPosition(0.15);
        }
        else{
            servoArm.setPosition(positionArm);
        }
    }

    //controls the second viper-slide takes in a move up and down command(binded to right-bumper and right-trigger)
    public void restrictedControlArmLift2(boolean moveArmUp, double moveArmDown) {

        //if the right-bumper being pressed turn the slide on at a set power
        if (moveArmUp) {
            motorLiftArm2.setPower(liftArmUpSpeed);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //get the current position of the motor/viper-slide for the hover code
            hoverPoint2 = motorLiftArm2.getCurrentPosition();
            //if the right-trigger is being pressed turn the slide on at a set power level
            //the right-trigger outputs a double value where 1 is being pressed fully and 0 is not being pressed
        } else if (moveArmDown != 0.0) {
            motorLiftArm2.setPower(liftArmDownSpeed);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //get the current position of the motor/viper-slide for the hover code
            hoverPoint2 = motorLiftArm2.getCurrentPosition();
        } else {
            //set the target hover point to the position found above and move the arm at a set speed to hold it
            motorLiftArm2.setTargetPosition(hoverPoint2);
            motorLiftArm2.setPower(liftArmHoverPower);
            //unlike above we have to use the encoder to be able to run to a set position
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }
    }


    //controls the viper-slide takes in a move up and down command(binded to left-bumper and left-trigger)
    public void restrictedControlArmLift(double moveArmUp, boolean moveArmDown){

        //if the right-trigger is being pressed turn the slide on at a set power level
        //the right-trigger outputs a double value where 1 is being pressed fully and 0 is not being pressed
            if(moveArmUp != 0.0)
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
                motorLiftArm.setTargetPosition(hoverPoint);
                //unlike above we have to use the encoder to be able to run to a set position
                motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorLiftArm.setPower(liftArmHoverPower);
            }
    }
}