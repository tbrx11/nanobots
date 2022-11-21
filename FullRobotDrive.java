
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


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


            //slide motor Y
            slideY.setPower(gamepad1.right_trigger/2);
            slideY.setPower(-gamepad1.left_trigger/2);



            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back Left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Precision mode:", "1/" + precision);
            telemetry.addData("Claw opening/closing  x/y:", "%4.2f, %4.2f", clawXpos, clawYpos);
            telemetry.addData("Claw spinning  x/y:", "%4.2f, %4.2f", spinXpos, spinYpos);
            telemetry.addData("Slide Power: ", gamepad1.right_trigger - gamepad1.left_trigger);
            telemetry.update();
        }
    }}
