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
    Servo servoWrist;
    Servo servoArm;

    final double liftArmSpeed = 0.5;
    final double liftArmUpSpeed = liftArmSpeed * -1;
    final double liftArmDownSpeed = liftArmSpeed;
    final double liftArmHoverPower = 0.5;

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1;     // Maximum rotational position
    static final double MIN_POS     =  0.15;     // Minimum rotational position

    double  positionGrabber = MAX_POS; // Start at max position
    double  positionWrist = MAX_POS; // Start at max position
    double  positionArm = MAX_POS; // Start at max position

    private int hoverPoint = 0;
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

    //get the Wrist's position so it can be passed to main for telemetry
    public double getPositionWrist(){
        return positionWrist;
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
        servoWrist.setPosition(MIN_POS);
        servoGrabber.setPosition(MAX_POS);
    }

    //this is called in main and setups all the variables for this script
    public ArmSystem(DcMotor motorLiftArm, Servo servoGrabber, DcMotor motorLiftArm2, Servo servoWrist, Servo servoArm){
        this.motorLiftArm = motorLiftArm;
        this.motorLiftArm2 = motorLiftArm2;
        this.servoGrabber = servoGrabber;
        this.servoWrist = servoWrist;
        this.servoArm = servoArm;

        motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftArm2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public ArmSystem(Servo servoGrabber){
        this.servoGrabber = servoGrabber;
    }

    //this is so that java doesn't get mad
    @Override
    public void loop() {}


    //controls the gripper takes in a boolean(binded to B on gpad2 in main)
    public void ControlGripper(boolean Close){
        //if the B-button is not being pressed close the gripper
        if (!Close) {
            // Keep stepping up until we hit the max value.
            positionGrabber += INCREMENT ;
            if (positionGrabber >= MAX_POS ) {
                positionGrabber = MAX_POS;
                Close = !Close;   // Switch ramp direction
            }
        }
        //if the B-Button IS being pressed open the gripper
        else {
            // Keep stepping down until we hit the min value.
            positionGrabber -= INCREMENT ;
            if (positionGrabber <= MIN_POS ) {
                positionGrabber = MIN_POS;
                Close = !Close;  // Switch ramp direction
            }
        }
        //send the new position to the grabber
        servoGrabber.setPosition(positionGrabber);


    }


    //controls the wrist. Takes in an up(D-pad Up), down(D-Pad Down), and variable to go to half position(Binded to X-button)
    public void ControlWrist(boolean Up, boolean Down, boolean Half){
        // if D-Pad Up being pressed increses the servo position by increment
        if (Up) {
            // Keep stepping up until we hit the max value.
            positionWrist += INCREMENT ;
        }
        //if D-Pad down being pressed decrease the servo position by increment
        else if(Down){
            // Keep stepping down until we hit the min value.
            positionWrist -= INCREMENT ;
        }
        //if x-button being pushed move to half max position
        if(Half){   positionWrist = MAX_POS/2; }
        //send new position to servo
        servoWrist.setPosition(positionWrist);
    }

    //controls the Arm. Takes in an up(Y button), down(A button), and variable to go to half position(Binded to X-button)
    public void ControlArm(boolean Up, boolean Down, boolean Half){
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
        if(Half){   positionArm = MAX_POS/2; }
        //send new position to servo
        servoArm.setPosition(positionArm);
    }

    public void restrictedControlArmLift2(boolean moveArmUp, double moveArmDown) {

        if (moveArmUp) {
            motorLiftArm2.setPower(liftArmUpSpeed);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            hoverPoint2 = motorLiftArm2.getCurrentPosition();
        } else if (moveArmDown != 0.0) {
            motorLiftArm2.setPower(liftArmDownSpeed);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            hoverPoint2 = motorLiftArm2.getCurrentPosition();
        } else {
            motorLiftArm2.setTargetPosition(hoverPoint2);
            motorLiftArm2.setPower(liftArmHoverPower);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }
    }


    public void restrictedControlArmLift(double moveArmUp, boolean moveArmDown){

            if(moveArmUp != 0.0)
            {
                motorLiftArm.setPower(liftArmUpSpeed);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                hoverPoint = motorLiftArm.getCurrentPosition();
            }
            else if(moveArmDown)
            {
                motorLiftArm.setPower(liftArmDownSpeed);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                hoverPoint = motorLiftArm.getCurrentPosition();
            }
            else
            {
                motorLiftArm.setTargetPosition(hoverPoint);
                motorLiftArm.setPower(liftArmHoverPower);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            }
    }
}