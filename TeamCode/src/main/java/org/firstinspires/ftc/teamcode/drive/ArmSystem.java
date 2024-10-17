package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp (name = "ArmControl")
public class ArmSystem extends OpMode {

    DcMotor motorLiftArm;

    final double liftArmSpeed = 0.5;
    final double liftArmUpSpeed = liftArmSpeed * -1;
    final double liftArmDownSpeed = liftArmSpeed * 2;
    final double liftArmHoverPower = 0.2;

    final int liftArmHighestTicks = -1900;

    @Override
    public void init() {
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
    }

    public ArmSystem(DcMotor motorLiftArm){
        this.motorLiftArm = motorLiftArm;

        motorLiftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void loop() {
        // First parameter: armUp input
        // Second parameter: armDown input
        controlArmLift(gamepad1.left_bumper, gamepad1.left_trigger);
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