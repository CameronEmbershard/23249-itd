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
    final double liftArmHoverPower = 1;

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1;     // Maximum rotational position
    static final double MIN_POS     =  0;     // Minimum rotational position

    double  positionGrabber = MAX_POS; // Start at max position
    double  positionWrist = MAX_POS; // Start at max position
    double  positionArm = MAX_POS; // Start at max position

    final int liftArmHighestTicks = -1900;

    @Override
    public void init() {
        //motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoWrist.setPosition(MIN_POS);
        servoGrabber.setPosition(MAX_POS);
    }

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


    @Override
    public void loop() {
        // First parameter: armUp input
        // Second parameter: armDown input
        controlArmLift(gamepad1.left_bumper, gamepad1.left_trigger);
        ControlGripper(gamepad1.right_bumper);

    }

    public void ControlGripper(boolean Close){
        // slew the servo, according to the rampUp (direction) variable.
        if (Close) {
            // Keep stepping up until we hit the max value.
            positionGrabber += INCREMENT ;
            if (positionGrabber >= MAX_POS ) {
                positionGrabber = MAX_POS;
                Close = !Close;   // Switch ramp direction
            }
        }
        else {
            // Keep stepping down until we hit the min value.
            positionGrabber -= INCREMENT ;
            if (positionGrabber <= MIN_POS ) {
                positionGrabber = MIN_POS;
                Close = !Close;  // Switch ramp direction
            }
        }
        servoGrabber.setPosition(positionGrabber);
    }

    public void ControlWrist(boolean Up, boolean Down){
        // slew the servo, according to the rampUp (direction) variable.
        if (Up) {
            // Keep stepping up until we hit the max value.
            positionWrist += INCREMENT ;
        }
        else if(Down){
            // Keep stepping down until we hit the min value.
            positionWrist -= INCREMENT ;
        }
        servoWrist.setPosition(positionWrist);
    }

    public void ControlArm(boolean Up, boolean Down){
        // slew the servo, according to the rampUp (direction) variable.
        if (Up) {
            // Keep stepping up until we hit the max value.
            if(positionArm < MAX_POS){
                positionArm += INCREMENT ;
            }
        }
        else if(Down){
            // Keep stepping down until we hit the min value.
            if(positionArm > MIN_POS){
                positionArm -= INCREMENT;
            }
        }
        servoArm.setPosition(positionArm);
    }

    /* The trigger buttons on the controller are represented as a double
     * This means:
     *
     *                              not pressed = 0.0;
     *                              half pressed = 0.5;
     *                              full pressed = 1.0;
     * and everything in between
     */

    public void controlArmLift(boolean moveArmUp, double moveArmDown){
        motorLiftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if(moveArmUp)
        {
            motorLiftArm.setPower(liftArmUpSpeed);
        }
        else if(moveArmDown > 0)
        {
            motorLiftArm.setPower(liftArmDownSpeed * moveArmDown);
        }
        else
        {
            motorLiftArm.setPower(0);
        }
    }

    // The height that you want to stay at when you let go of the arm buttons
    private int hoverPoint = 0;
    public void restrictedControlArmLift2(boolean moveArmUp, double moveArmDown) {

        if (moveArmUp) {
            motorLiftArm2.setPower(liftArmUpSpeed);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            hoverPoint = motorLiftArm2.getCurrentPosition();
        } else if (moveArmDown != 0.0) {
            motorLiftArm2.setPower(liftArmDownSpeed);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            hoverPoint = motorLiftArm2.getCurrentPosition();
        } else {
            motorLiftArm2.setTargetPosition(hoverPoint);
            motorLiftArm2.setPower(liftArmHoverPower);
            motorLiftArm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }
    }



    public void restrictedControlArmLift(boolean moveArmUp, double moveArmDown){

            if(moveArmUp)
            {
                motorLiftArm.setPower(-liftArmUpSpeed);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                hoverPoint = motorLiftArm.getCurrentPosition();
            }
            else if(moveArmDown != 0.0)
            {
                motorLiftArm.setPower(-liftArmDownSpeed);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                hoverPoint = motorLiftArm.getCurrentPosition();
            }
            else
            {
                motorLiftArm.setTargetPosition(hoverPoint);
                motorLiftArm.setPower(-liftArmHoverPower);
                motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            }
    }
}