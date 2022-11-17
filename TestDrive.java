
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="TestDrive", group="Linear Opmode")

public class TestDrive extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private int precision;
    private Boolean precision_check;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        //motor direction
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        precision = 0;
        precision_check = false;

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            /*
             * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
             * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
             * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
             */

            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
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



            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back Left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Precision mode:", "1/" + precision);
            telemetry.update();
        }
    }}
