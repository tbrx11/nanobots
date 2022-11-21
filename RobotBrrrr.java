
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="2P robot drive", group="Linear Opmode")

public class RobotBrrrr extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private Claw clawX;
    private Claw clawY;
    private DcMotor slideY;
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
    public void runOpMode() {

        // Initialize the hardware variables
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        clawX = new Claw(hardwareMap.get(Servo.class, "clawX"),hardwareMap.get(Servo.class, "spinnerX"));
        clawY = new Claw(hardwareMap.get(Servo.class, "clawY"),hardwareMap.get(Servo.class, "spinnerY"));
        slideY = hardwareMap.get(DcMotor.class, "slideY");

        clawX.clawXinit();
        clawY.clawYinit();

        //motor direction
        slideY.setDirection(DcMotor.Direction.REVERSE);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

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

            double axial   = -gamepad1.left_stick_y;
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

            //PRECISION MODE
            //prec = 2 is 1/2 speed / prec= 3 is 1/3 speed
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

            //GAMEPAD 1 DRIVING
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

            //maybe need a if to see if robot is not moving
            //GAMEPAD2 MICRO-ADJUSTMENTS DRIVING
            if(gamepad2.dpad_up) {
                leftFront.setPower(.1);
                rightFront.setPower(.1);
                leftBack.setPower(.1);
                rightBack.setPower(.1);
            }
            else if(gamepad2.dpad_down) {
                leftFront.setPower(-.1);
                rightFront.setPower(-.1);
                leftBack.setPower(-.1);
                rightBack.setPower(-.1);
            }
            else if(gamepad2.dpad_right) {
                leftFront.setPower(.1);
                rightFront.setPower(-.1);
                leftBack.setPower(-.1);
                rightBack.setPower(.1);
            }
            else if(gamepad2.dpad_left) {
                leftFront.setPower(-.1);
                rightFront.setPower(.1);
                leftBack.setPower(.1);
                rightBack.setPower(-.1);
            }
            else if(gamepad2.left_bumper) {
                leftFront.setPower(.1);
                rightFront.setPower(-.1);
                leftBack.setPower(.1);
                rightBack.setPower(-.1);
            }
            else if(gamepad2.right_bumper) {
                leftFront.setPower(-.1);
                rightFront.setPower(.1);
                leftBack.setPower(-.1);
                rightBack.setPower(.1);
            }
            else{
                leftFront.setPower(0);
                rightFront.setPower(0);
                leftBack.setPower(0);
                rightBack.setPower(0);
            }


            //GAMEPAD2 CLAW CONTROL
            //claw x spin if clawX is closed
            if(gamepad2.a){
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
            if(gamepad2.b){
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
            if(gamepad2.x){
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
            if(gamepad2.y){
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


            //GAMEPAD 2 SLIDE CONTROL
            //slide motor Y
            slideY.setPower(-gamepad2.right_stick_y/2);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back Left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Precision mode:", "1/" + precision);
            telemetry.addData("Slide Y",  -gamepad2.right_stick_y/2);
            telemetry.addData("Claw opening/closing  x/y:", "%4.2f, %4.2f", clawXpos, clawYpos);
            telemetry.addData("Claw spinning  x/y:", "%4.2f, %4.2f", spinXpos, spinYpos);
            telemetry.update();
        }
    }
}
