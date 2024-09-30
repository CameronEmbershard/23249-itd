package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@Disabled
@TeleOp (name = "ArmControl")
public class ArmControl extends OpMode {

    DcMotor motorLiftArm;
    DcMotor motorScoringArm;

    Servo servoGrabber;
    Servo servoWrist;
    Servo servoStopper;

    final double liftArmSpeed = 0.5;
    final double liftArmUpSpeed = liftArmSpeed * -1;
    final double liftArmDownSpeed = liftArmSpeed * 2;
    final double liftArmHoverPower = 0.2;

    final int liftArmHighestTicks = -1900;

    final double scoringArmSpeed = 0.5;
    final double scoringArmUpSpeed = scoringArmSpeed * 1;
    final double scoringArmDownSpeed = scoringArmSpeed * 1;

    final double grabberOpenPosition = 0.02;
    final double grabberClosePosition = 0.28;
    final double wristFlatPosition = 0.78;
    final double wristAngledPosition = 1.0;

    final double stopperOpenPosition = 0.65;
    final double stopperClosePosition = 1.0;

    @Override
    public void init() {
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoWrist = hardwareMap.servo.get("servoWrist");
        servoStopper = hardwareMap.servo.get("servoStopper");
    }

    public ArmControl(DcMotor motorLiftArm, Servo servoGrabber, Servo servoWrist, Servo servoStopper){
        this.motorLiftArm = motorLiftArm;
        this.servoGrabber = servoGrabber;
        this.servoWrist = servoWrist;
        this.servoStopper = servoStopper;

        motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void loop() {
        // First parameter: armUp input
        // Second parameter: armDown input
        controlArmLift(gamepad1.left_bumper, gamepad1.left_trigger);

        // First parameter: grabberOpen input
        // Second parameter: grabberClose input
        controlArmGrabber(gamepad1.x, gamepad1.a);

        // First parameter: wristFlat input
        // Second parameter: wristAngled input
        controlArmWrist(gamepad1.left_trigger, gamepad1.left_bumper);

        // First parameter: stopperOpen input
        // Second parameter: stopperClose input
        controlArmStopper(gamepad1.y, gamepad1.b);
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

    // Scrapped code for second arm (only a single arm was used)

//    public void controlScoringArm(boolean moveArmUp, double moveArmDown) {
//        if (moveArmUp) {
//            motorScoringArm.setPower(scoringArmUpSpeed);
//        } else if (moveArmDown > 0) {
//            motorScoringArm.setPower(scoringArmDownSpeed);
//        } else {
//            motorScoringArm.setPower(0);
//        }
//    }

    public void controlArmGrabber(boolean grabberOpen, boolean grabberClose){
        if (grabberOpen) {
            servoGrabber.setPosition(grabberOpenPosition);
        } else if (grabberClose) {
            servoGrabber.setPosition(grabberClosePosition);
        }
    }

    public void controlArmWrist(float wristFlat, boolean wristAngled){
        if (wristFlat > 0) {
            servoWrist.setPosition(wristFlatPosition);
        } else if (wristAngled) {
            servoWrist.setPosition(wristAngledPosition);
        }
    }

    public void controlArmStopper(boolean stopperOpen, boolean stopperClose){
        if (stopperOpen) {
            servoStopper.setPosition(stopperOpenPosition);
        } else if (stopperClose) {
            servoStopper.setPosition(stopperClosePosition);
        }
    }
}
