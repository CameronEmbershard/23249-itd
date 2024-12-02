package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.ArmSystem;
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
    VisionHandler visionSystem;
    WebcamName webcam;

    //both arms servos and motors
    DcMotor motorLiftArm;
    DcMotor motorLiftArm2;
    Servo servoGrabber;
    Servo servoWrist;
    Servo servoArm;



    //drive system(the one in subsystems)
    MechanumDrive driveSystem;
    //the arm system(there are two)
    org.firstinspires.ftc.teamcode.drive.ArmSystem ArmSystem;

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
        motorLiftArm2 = hardwareMap.dcMotor.get("motorLiftArm2");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoWrist = hardwareMap.servo.get("servoWrist");
        servoArm = hardwareMap.servo.get("servoArm");
        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        //init the drive and arm system
        driveSystem = new MechanumDrive(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, motorLiftArm2,servoWrist,servoArm);
        //ArmSystem = new ArmSystem(motorLiftArm, servoGrabber, motorLiftArm2, servoArm);

        //init vision
        visionSystem.init();

        //sends a message to driver HUB that all is 'ight
        telemetry.addData("Main","All Systems Online. Here We Go!");
    }

    @Override
    public void loop()
    {
        //run vision
        visionSystem.loop();

        // Drives the robot
        driveSystem.drive(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x, gamepad1.right_trigger);

        //controls both viper-slides and the gripper
        ArmSystem.restrictedControlArmLift(gamepad2.left_trigger, gamepad2.left_bumper);
        ArmSystem.restrictedControlArmLift2(gamepad2.right_bumper, gamepad2.right_trigger);
        ArmSystem.ControlGripper(gamepad2.b);

        // controls the arm
        ArmSystem.ControlArm(gamepad2.y, gamepad2.a, gamepad2.x, gamepad2.dpad_left);

        // controls the wrist
        ArmSystem.ControlWrist(gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.x, gamepad2.dpad_left);

        //sends the data from the arm-system to the driver HUB
        telemetry.addData("Hover",ArmSystem.getHoverPoint());
        telemetry.addData("Hover2",ArmSystem.getHoverPoint2());
        telemetry.addData("Grabber",ArmSystem.getPositionGrabber());
        telemetry.addData("Wrist",ArmSystem.getPositionWrist());
        telemetry.addData("Arm",ArmSystem.getPositionArm());
        //call a telemetry update to push new data to driver HUB
        telemetry.update();
    }

}