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
    Servo servoGrabber;

    final double liftArmSpeed = 0.5;
    final double liftArmUpSpeed = liftArmSpeed * -1;
    final double liftArmDownSpeed = liftArmSpeed * 2;
    final double liftArmHoverPower = 0.2;

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  0.5;     // Maximum rotational position
    static final double MIN_POS     =  0.25;     // Minimum rotational position

    double  position = MAX_POS; // Start at max position

    final int liftArmHighestTicks = -1900;

    @Override
    public void init() {
        //motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        telemetry.addData("Servo Pos", "%5.2f", position);
    }

    public ArmSystem(DcMotor motorLiftArm, Servo servoGrabber){
        this.motorLiftArm = motorLiftArm;
        this.servoGrabber = servoGrabber;

        motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
            position += INCREMENT ;
            if (position >= MAX_POS ) {
                position = MAX_POS;
                Close = !Close;   // Switch ramp direction
            }
        }
        else {
            // Keep stepping down until we hit the min value.
            position -= INCREMENT ;
            if (position <= MIN_POS ) {
                position = MIN_POS;
                Close = !Close;  // Switch ramp direction
            }
        }
        servoGrabber.setPosition(position);
        telemetry.addData("Servo Moved To", "%5.2f", position);
    }

    /* The trigger buttons on the controller are represented as a double
     * This means:
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
    public void restrictedControlArmLift(boolean moveArmUp, double moveArmDown){

        if(moveArmUp)
        {
            motorLiftArm.setTargetPosition(liftArmHighestTicks);
            motorLiftArm.setPower(liftArmUpSpeed);
            motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            hoverPoint = motorLiftArm.getCurrentPosition();
        }
        else if(moveArmDown > 0)
        {
            motorLiftArm.setTargetPosition(0);
            motorLiftArm.setPower(liftArmDownSpeed * moveArmDown);
            motorLiftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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