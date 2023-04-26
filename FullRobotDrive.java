
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Thread;


@TeleOp(name="1P robot drive", group="Linear Opmode")

public class FullRobotDrive extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
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
    private int precision;
    private Boolean precision_check;

    @Override
    public void runOpMode() throws InterruptedException{

        // Initialize the hardware variables
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        clawX = new Claw(hardwareMap.get(Servo.class, "clawX"),hardwareMap.get(Servo.class, "spinnerX"));
        clawY = new Claw(hardwareMap.get(Servo.class, "clawY"),hardwareMap.get(Servo.class, "spinnerY"));
        slideY = hardwareMap.get(DcMotor.class, "slideY");
        linActX = hardwareMap.get(DcMotor.class, "linActX");

        slideY.setDirection(DcMotor.Direction.REVERSE);
        slideY.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clawY.clawYinit();
        Thread.sleep(500);
        clawX.clawCloseX();
        Thread.sleep(300);
        clawX.spinDownX();
        Thread.sleep(500);
        clawX.clawOpenX();

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
        linActX.setDirection(DcMotor.Direction.FORWARD);

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


        //artificial time and one button rotates
        spinXpos = 0;
        spinYpos = 0;
        spinXcheck = false;
        spinYcheck = false;
        clawXpos = 1;
        clawYpos = 1;
        clawXcheck = false;
        clawYcheck = false;
        precision = 0;
        precision_check = false;


        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //driving code
            double max;

            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            /*
             * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
             * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
             * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
             */
            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            //prec = 1 is 1/2 speed / prec= 2 is 1/3 speed
            if (gamepad1.left_stick_button) {
                if (!precision_check) {
                    if (precision == 2) {
                        precision = 0;
                    } else {
                        precision = 2;
                    }
                }
                precision_check = true;
            } else if (gamepad1.right_stick_button) {
                if (!precision_check) {
                    if (precision == 3) {
                        precision = 0;
                    } else {
                        precision = 3;
                    }
                }
                precision_check = true;
            } else {
                precision_check = false;
            }

            //Send calculated power to wheels
            if(precision == 2) {
                leftFront.setPower(leftFrontPower/ 2);
                rightFront.setPower(rightFrontPower/ 2);
                leftBack.setPower(leftBackPower/ 2);
                rightBack.setPower(rightBackPower/ 2);
            }
            else if(precision == 3){
                leftFront.setPower(leftFrontPower/ 3);
                rightFront.setPower(rightFrontPower/ 3);
                leftBack.setPower(leftBackPower/ 3);
                rightBack.setPower(rightBackPower/ 3);
            }
            else if(precision == 0){
                leftFront.setPower(leftFrontPower);
                rightFront.setPower(rightFrontPower);
                leftBack.setPower(leftBackPower);
                rightBack.setPower(rightBackPower);
            }

            //claw x spin if clawX is closed
            if(gamepad1.a){
                if(!spinXcheck && clawXpos ==0) {
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
                if(!spinYcheck && clawYpos == 0) {
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
                slideY.setPower(.5);
            }
            else if(gamepad1.left_bumper) {
                slideY.setPower(-.5);
            }
            else if(!slideY.isBusy()){
                if(!(slideY.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER)){
                    slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                slideY.setPower(0);
            }

            if (gamepad1.dpad_up){
                clawX.clawCloseX();
                Thread.sleep(680);
                clawX.spinUpX();
                Thread.sleep(1000);
                clawY.clawCloseY();
                Thread.sleep(680);
                clawX.clawOpenX();
                Thread.sleep(680);
                slideY.setTargetPosition(3000);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideY.setPower(.7);
                while(slideY.isBusy()){

                }
                slideY.setPower(0);
                slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                Thread.sleep(680);
                clawY.spinUpY();
                clawYpos =0;

            }

            if(gamepad1.dpad_down){
                clawY.clawCloseY();
                clawX.clawCloseX();
                clawX.spinDownX();
                Thread.sleep(100);
                clawY.spinDownY();
                slideY.setTargetPosition(0);
                slideY.setPower(-.7);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                clawX.clawOpenX();
                Thread.sleep(500);
                clawY.clawOpenY();

                //slideY.setPower(0);
            }


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back Left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Precision mode:", "1/" + precision);
            //telemetry.addData("Claw opening/closing  x/y:", "%4.2f, %4.2f", clawXpos, clawYpos);
            //telemetry.addData("Claw spinning  x/y:", "%4.2f, %4.2f", spinXpos, spinYpos);
            //telemetry.addData("Slide Power: ", gamepad1.right_trigger);
            telemetry.update();
        }
    }
}
