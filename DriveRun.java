package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.lang.Math;


public class DriveRun extends Thread{

    private Gamepad gamepad1;
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private DcMotor linActX;
    private int precision;
    private boolean precision_check;
    private boolean running;


    public DriveRun(DcMotor lF, DcMotor lB, DcMotor rF, DcMotor rB, DcMotor linAct, Gamepad gp1){
        this.leftFront = lF;
        this.leftBack = lB;
        this.rightFront = rF;
        this.rightBack = rB;
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
        this.gamepad1 = gp1;
        precision = 1;
        precision_check = false;
        running = true;
        linActX = linAct;
        linActX.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void run() {
        while(running) {
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

            //linear Actuator X
            if(gamepad1.right_trigger >0) {
                linActX.setPower(1);
            }
            else if(gamepad1.left_trigger >0) {
                linActX.setPower(-1);
            }
            else{
                linActX.setPower(0);
            }
        }
    }

    public void end(){
        running = false;
    }
}
