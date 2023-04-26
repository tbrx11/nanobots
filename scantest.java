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
                hardwareMap.get(WebcamName.class, "Webcam 1"), hardwareMap.get(BNO055IMU.class, "imu"), hardwareMap.get(DistanceSensor.class, "sensor_range"));


        telemetry.addData("Status", "Initial");
        telemetry.update();
        driveBot.initIMU();
        driveBot.initMotors();
        //clawY.clawOpenY();

        String endLoc = "umm";

        //telemetry.addData("ConePos", endLoc);
        telemetry.addData("degree", driveBot.getDeg());
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();



        //endLoc = driveBot.getEndLoc();
        //telemetry.addData("ConePos", endLoc);
        telemetry.update();

        driveBot.move(2150, .4);
        driveBot.poleScanAC();
        driveBot.transCone(2760);
        clawY.clawOpenY();
        Thread.sleep(300);
        driveBot.resetClaws();



    }
}
