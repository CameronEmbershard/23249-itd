package org.firstinspires.ftc.teamcode.opmodes.auto;


import org.firstinspires.ftc.teamcode.subsystems.AutoDriveForward;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoDance")
public class AutoDance extends RobotAuto {
    DcMotor motorRight;
    DcMotor motorLeft;
    ElapsedTime timer;
    @Override
    public void runOpMode() {

        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");

        timer = new ElapsedTime();

        waitForStart();

        AutoDriveForward AutoDance = new AutoDriveForward(timer, this, motorRight, motorLeft);
        AutoDance.driveAutonomously();
    }

}
