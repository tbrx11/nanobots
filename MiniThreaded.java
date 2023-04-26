
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Thread;
import java.util.ArrayList;


@TeleOp(name="Thread Test", group="Linear Opmode")
public class MiniThreaded extends LinearOpMode {

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
    private Boolean countCheck;
    private MiniClawRun claws;

    private int actPosInd;
    private int[] actPos = {1000,1500,2000,2500,5000};


    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize the hardware variables
        clawX = new Claw(hardwareMap.get(Servo.class, "clawX"), hardwareMap.get(Servo.class, "spinnerX"));
        clawY = new Claw(hardwareMap.get(Servo.class, "clawY"), hardwareMap.get(Servo.class, "spinnerY"));
        slideY = hardwareMap.get(DcMotor.class, "slideY");
        linActX = hardwareMap.get(DcMotor.class, "linActX");

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        clawX.clawXinit();
        clawY.clawYinit();
        linActX.setDirection(DcMotor.Direction.FORWARD);
        slideY.setDirection(DcMotor.Direction.REVERSE);
        slideY.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideY.setPower(-.4);
        Thread.sleep(600);
        slideY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideY.setPower(0);

        precision = 2;
        precision_check = false;
        //artificial time and one button rotates
        spinXpos = 0;
        spinYpos = 0;
        spinXcheck = false;
        spinYcheck = false;
        clawXpos = 1;
        clawYpos = 1;
        clawXcheck = false;
        clawYcheck = false;
        countCheck= false;
        actPosInd =4;


        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

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

            //prec = 2 is 1/2 speed / prec= 3 is 1/3 speed
            if (gamepad1.left_stick_button) {
                if (!precision_check) {
                    if (precision == 2) {
                        precision = 1;
                    } else {
                        precision = 2;
                    }
                }
                precision_check = true;
            } else if (gamepad1.right_stick_button) {
                if (!precision_check) {
                    if (precision == 3) {
                        precision = 1;
                    } else {
                        precision = 3;
                    }
                }
                precision_check = true;
            } else {
                precision_check = false;
            }

            //Send calculated power to wheels
            leftFront.setPower(leftFrontPower/ precision);
            rightFront.setPower(rightFrontPower/ precision);
            leftBack.setPower(leftBackPower/ precision);
            rightBack.setPower(rightBackPower/ precision);
            if(gamepad2.a||gamepad1.a){
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
            if(gamepad2.b||gamepad1.b){
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
            if(gamepad2.x || gamepad1.x){
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

            if(gamepad1.back ||gamepad2.back){
                clawX.spinMidX();
            }

            if(gamepad1.start ||gamepad2.start){
                clawX.spinDownX();
            }

            //claw Y open/close
            if(gamepad2.y||gamepad2.y){
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


            //slide motor Y
            if(gamepad2.right_bumper||gamepad1.right_bumper) {
                slideY.setPower(1);
            }
            else if(gamepad2.left_bumper||gamepad1.left_bumper) {
                slideY.setPower(-1);
            }
            else if(!slideY.isBusy()){
                if(!(slideY.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER)){
                    slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                slideY.setPower(0);
            }

            //linear Actuator X
            if(gamepad1.right_trigger >0||gamepad2.right_trigger >0) {
                linActX.setPower(1);
            }
            else if(gamepad1.left_trigger >0||gamepad2.left_trigger >0) {
                linActX.setPower(-1);
            }
            else if(!linActX.isBusy()){
                linActX.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                linActX.setPower(0);
            }

            if (gamepad1.dpad_up) {
                linActX.setTargetPosition(actPos[actPosInd]);
                linActX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linActX.setPower(1);
            }

            if(gamepad1.dpad_down){
                linActX.setTargetPosition(0);
                linActX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linActX.setPower(-1);

            }

            if(gamepad1.dpad_right) {
                if (!countCheck) {
                    countCheck = true;
                    if (actPosInd > 0) {
                        actPosInd--;
                    }
                }
            }
            else{
                countCheck = false;
            }

            if(gamepad1.dpad_left){
                if(!countCheck) {
                    countCheck = true;
                    if(actPosInd > 0){
                        actPosInd--;
                    }
                }
            }
            else{
                countCheck = false;
            }
            if (gamepad2.dpad_up) {
                claws = new MiniClawRun(clawX, clawY, slideY,3);
                claws.start();
            }
            if (gamepad2.dpad_right) {
                claws = new MiniClawRun(clawX, clawY, slideY,2);
                claws.start();
            }
            if (gamepad2.dpad_left) {
                claws = new MiniClawRun(clawX, clawY, slideY,1);
                claws.start();
            }
            if (gamepad2.dpad_down) {
                claws = new MiniClawRun(clawX, clawY, slideY,0);
                claws.start();
            }


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back Left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Precision mode:", "1/" + precision);
            //telemetry.addData("Status", "ActPos: "+ actPosInd);
            //telemetry.addData("Status", "slide power: "+ slideY.getPower());
            telemetry.update();
        }

        telemetry.addData("Status", "good job out there soldier");
        telemetry.update();
    }
}
