package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Thread;




@TeleOp(name="ClawSlideTest", group="Linear Opmode")
public class ClawSlideTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Claw clawX;
    private Claw clawY;
    private DcMotor slideY;
    private DcMotor linActX;
    private int spinXpos;
    private int spinYpos;
    private Boolean spinXcheck;
    private Boolean spinYcheck;
    private int clawXpos;
    private int clawYpos;
    private Boolean clawXcheck;
    private Boolean clawYcheck;


    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "WAITTTT");
        telemetry.update();
        // Initialize the hardware variables.
        clawX = new Claw(hardwareMap.get(Servo.class, "clawX"),hardwareMap.get(Servo.class, "spinnerX"));
        clawY = new Claw(hardwareMap.get(Servo.class, "clawY"),hardwareMap.get(Servo.class, "spinnerY"));
        slideY = hardwareMap.get(DcMotor.class, "slideY");
        linActX = hardwareMap.get(DcMotor.class, "linActX");

        //artifical time and one button rotates
        spinXpos = 0;
        spinYpos = 0;
        spinXcheck = false;
        spinYcheck = false;
        clawXpos = 1;
        clawYpos = 1;
        clawXcheck = false;
        clawYcheck = false;

        telemetry.addData("Status", "vairables done");
        telemetry.update();


        //motor direction
        slideY.setDirection(DcMotor.Direction.REVERSE);
        slideY.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clawY.clawYinit();
        Thread.sleep(500);
        clawX.clawCloseX();
        Thread.sleep(300);
        clawX.spinDownX();
        Thread.sleep(500);
        clawX.clawOpenX();


        //offset slide up
        slideY.setPower(-.5);
        Thread.sleep(500);
        slideY.setPower(0);
        /*
        slideY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideY.setTargetPosition(0);
        slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideY.setPower(.5);
        while(slideY.isBusy()){

        }
        slideY.setPower(0);
        */
        slideY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //claw x spin if clawX is closed
            if(gamepad1.a){
                if(!spinXcheck) {
                    spinXcheck = true;
                    if (spinXpos == 0) {
                        clawX.spinUpX();
                        spinXpos = 1;
                    } else if (spinXpos == 1) {
                        clawX.spinDownX();
                        spinXpos = 0;
                    }
                }
            }
            else{
                spinXcheck = false;
            }

            //claw y spin if clawY is closed
            if(gamepad1.b){
                if(!spinYcheck) {
                    spinYcheck = true;
                    if (spinYpos == 0) {
                        clawY.spinUpY();
                        spinYpos = 1;
                    } else if (spinYpos == 1) {
                        clawY.spinDownY();
                        spinYpos = 0;
                    }
                }
            }
            else{
                spinYcheck = false;
            }

            //claw X open/close
            if(gamepad1.x){
                if(!clawXcheck) {
                    clawXcheck = true;
                    if (clawXpos == 0) {
                        clawX.clawOpenX();
                        clawXpos = 1;
                    } else if (clawXpos == 1) {
                        clawX.clawCloseX();
                        clawXpos = 0;
                    }
                }
            }
            else{
                clawXcheck = false;
            }

            //claw Y open/close
            if(gamepad1.y){
                if(!clawYcheck) {
                    clawYcheck = true;
                    if (clawYpos == 0) {
                        clawY.clawOpenY();
                        clawYpos = 1;
                    } else if (clawYpos == 1) {
                        clawY.clawCloseY();
                        clawYpos = 0;
                    }
                }
            }
            else{
                clawYcheck = false;
            }

            //linear Actuator X
            if(gamepad1.right_trigger > 0) {
                linActX.setPower(gamepad1.right_trigger);
            }
            else if(gamepad1.left_trigger >0) {
                linActX.setPower(-gamepad1.left_trigger);
            }
            else{
                linActX.setPower(0);
            }

            //slide motor Y
            if(gamepad1.right_bumper) {
                slideY.setPower(1);
            }
            else if(gamepad1.left_bumper) {
                slideY.setPower(-1);
            }
            else if(!slideY.isBusy()){
                if(!(slideY.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER)){
                    slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                slideY.setPower(0);
            }

            if (gamepad1.dpad_up){
                clawX.clawCloseX();
                Thread.sleep(600);
                clawX.spinUpX();
                Thread.sleep(1000);
                clawY.clawCloseY();
                //Thread.sleep(680);
                clawX.clawOpenX();
                Thread.sleep(200);
                slideY.setTargetPosition(3000);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideY.setPower(1);
                clawY.spinUpY();
                while(slideY.isBusy()){

                }
                slideY.setPower(0);
                slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                //Thread.sleep(680);
                clawYpos =0;

            }

            if(gamepad1.dpad_down){
                clawY.clawCloseY();
                clawX.clawCloseX();
                clawX.spinDownX();
                clawY.spinDownY();
                slideY.setTargetPosition(0);
                slideY.setPower(-1);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                clawX.clawOpenX();
                Thread.sleep(1500);
                clawY.clawOpenY();

                //slideY.setPower(0);
            }




            // Show the elapsed game time
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}