package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class RobotAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

    }

    public void sleepMilliseconds(int milliseconds){
        sleep(milliseconds);
    }

    public void addTelemetry(String caption, Object obj){
        telemetry.addData(caption, obj);
        telemetry.update();
    }

    public void addTelemetry(String caption, Object obj1, Object obj2, Object obj3, Object obj4){
        telemetry.addData(caption, obj1 + " " + obj2 + " " + obj3 + " " + obj4);
        telemetry.update();
    }

}
