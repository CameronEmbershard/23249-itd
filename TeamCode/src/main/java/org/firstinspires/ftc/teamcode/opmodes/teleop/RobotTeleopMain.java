package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Drive.ArmSystem;
import org.firstinspires.ftc.teamcode.subsystems.MechanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.VisionHandler;

@TeleOp(name = "TeleopMain")
public class RobotTeleopMain extends OpMode {
    //drive motors
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    //init vision systems
    //VisionHandler visionSystem = new VisionHandler();

    //both arms servos and motors
    DcMotor motorLiftArm;
    DcMotor motorRotateArm;
    Servo servoGrabber;
    Servo servoGrabber2;
    //Servo servoArm;



    //drive system(the one in subsystems)
    MechanumDrive driveSystem;
    //the arm system(there are two)
    org.firstinspires.ftc.teamcode.Drive.ArmSystem ArmSystem;

    //all this is called when the init button is pressed
    @Override
    public void init() {

        //setup the drive motors
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        //setup both arm servos and motors
        motorLiftArm = hardwareMap.dcMotor.get("motorLiftArm");
        motorRotateArm = hardwareMap.dcMotor.get("motorRotateArm");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoGrabber2 = hardwareMap.servo.get("servoGrabber2");
        //servoArm = hardwareMap.servo.get("servoArm");

        //init the drive and arm system
        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(motorLiftArm, motorRotateArm, servoGrabber, servoGrabber2);
        //The previous line here was deprecated :D CIC

        //init visionSystem.init(hardwareMap.get(WebcamName.class, "Webcam 1"));

        //sends a message to driver HUB that all is 'ight
        //yeah man, its radical CIC
        telemetry.addData("Main","All Systems Online. Here We Go!");
    }

    @Override
    public void loop()
    {
        // Drives the robot
        driveSystem.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x, gamepad1.right_trigger);

        //controls both viper-slides and the gripper
        ArmSystem.restrictedControlArmRotate(gamepad2.right_trigger, gamepad2.left_trigger);
        //ArmSystem.ControlGripper(gamepad2.dpad_down,gamepad2.dpad_up );
        ArmSystem.restrictedControlArmLift(gamepad2.left_bumper, gamepad2.right_bumper);

        // controls the arm
        //ArmSystem.ControlArm(gamepad2.a, gamepad2.y, gamepad2.x, gamepad2.dpad_left);

        //sends the data from the arm-system to the driver HUB
        telemetry.addData("SlideArmPos",ArmSystem.getHoverPoint());
        telemetry.addData("RotateArmCurr", ArmSystem.getCurPosRotateArm());
        telemetry.addData("RotateArmPosSet", ArmSystem.getSetPosRotateArm());
        telemetry.addData("hoverPoint2", ArmSystem.getHoverPoint2());
        telemetry.addData("Grabber",ArmSystem.getPositionGrabber());
        telemetry.addData("Grabber2",ArmSystem.getPositionGrabber2());
        telemetry.addData("Arm",ArmSystem.getPositionArm());

        //telemetry.addData("Vision", visionSystem.getSide());
        //call a telemetry update to push new data to driver HUB
        telemetry.update();
    }

}