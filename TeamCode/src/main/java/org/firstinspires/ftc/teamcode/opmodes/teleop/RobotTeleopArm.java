package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;
import org.firstinspires.ftc.teamcode.drive.ArmSystem;

@Disabled
@TeleOp(name = "TeleopArm")
public class RobotTeleopArm extends OpMode {

    DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
    DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
    DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
    DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");


    DcMotor motorLiftArm;

    MechanumDrive driveSystem;
    ArmSystem armSystem;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");

        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        armSystem = new ArmSystem(motorLiftArm);
    }

    @Override
    public void loop()
    {
        // First parameter: Strafe x input
        // Second parameter: Strafe y input
        // Third parameter: Rotation x input
        driveSystem.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 80);

        // First parameter: armUp input
        // Second parameter: armDown input
        armSystem.controlArmLift(gamepad1.left_bumper, gamepad1.left_trigger);
    }
}