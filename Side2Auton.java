package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

@Autonomous(name="Side2 Auton", group="Robot")
public class Side2Auton extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException{
        //Init bot and hardware

        Claw clawX = new Claw(hardwareMap.get(Servo.class, "clawX"),hardwareMap.get(Servo.class, "spinnerX"));
        Claw clawY = new Claw(hardwareMap.get(Servo.class, "clawY"),hardwareMap.get(Servo.class, "spinnerY"));
        DcMotor linActX = hardwareMap.get(DcMotor.class, "linActX");

        Auton driveBot = new Auton(hardwareMap.get(DcMotor.class, "leftFront"), hardwareMap.get(DcMotor.class, "leftBack"),
                hardwareMap.get(DcMotor.class, "rightFront"), hardwareMap.get(DcMotor.class, "rightBack"), hardwareMap.get(DcMotor.class, "slideY"), clawX, clawY, linActX,
                hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()),
                hardwareMap.get(WebcamName.class, "Webcam 1"), hardwareMap.get(BNO055IMU.class, "imu"), hardwareMap.get(DistanceSensor.class, "distSensF"));


        telemetry.addData("Status", "Initial");
        telemetry.update();
        driveBot.initIMU();
        driveBot.initMotors();
        //driveBot.clawCloseX();

        String endLoc = "1 Wheel";

        //telemetry.addData("ConePos", endLoc);
        telemetry.addData("degree", driveBot.getDeg());
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();



        endLoc = driveBot.getEndLoc();
        telemetry.addData("ConePos", endLoc);
        telemetry.update();


        //driveBot.strafe(-150, -.4);
        driveBot.move(2050, .6);
        driveBot.turn(40,.3);
        driveBot.poleScanAC();
        telemetry.addData("Status:", "found it");
        telemetry.update();
        driveBot.transCone(2760);
        driveBot.move(-190,-.4);
        clawY.clawOpenY();
        Thread.sleep(300);
        driveBot.resetClaws();
        driveBot.move(150,.4);


        if(endLoc.equals("1 Wheel")) {
            driveBot.turn(90,0.3);
            driveBot.move(1000,.3);
        }
        else if(endLoc.equals("2 Dumbbot")) {
            driveBot.turn(0, -.3);
            driveBot.move(-500, -.3);
        }
        else if(endLoc.equals("3 Wrench")) {
            driveBot.turn(90,0.3);
            driveBot.move(-1000,-.3);
        }
        else{

            driveBot.turn(0,-.4);
            driveBot.move(150,.3);
            driveBot.strafe(1100,-.5);
            driveBot.move(-2000,-.4);
            driveBot.strafe(500,-.4);
            driveBot.move(-50,-.3);
        }
    }
}
