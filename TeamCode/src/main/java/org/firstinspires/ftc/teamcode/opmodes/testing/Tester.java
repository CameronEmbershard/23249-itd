package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled//@Autonomous(name = "servo1")
public class Tester extends LinearOpMode{

    Servo servo1;
    ElapsedTime timer;

    @Override
    public void runOpMode() {

        servo1 = hardwareMap.servo.get("servo1");
        timer = new ElapsedTime();

        waitForStart();

        timer.reset();
        while(opModeIsActive()){

            if(timer.seconds() > 2){
                servo1.setPosition(1);
                if(timer.seconds() > 4){
                    timer.reset();
                }
            }
            else {
                servo1.setPosition(0);
            }

        }
    }
}
