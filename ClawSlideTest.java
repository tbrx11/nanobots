
package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;




@TeleOp(name="ClawSlideTest", group="Linear Opmode")
public class ClawSlideTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Claw clawX;
    private Claw clawY;
    private DcMotor slideX;
    private DcMotor slideY;
    private int spinXpos;
    private int spinYpos;
    private Boolean spinXcheck;
    private Boolean spinYcheck;
    private int clawXpos;
    private int clawYpos;
    private Boolean clawXcheck;
    private Boolean clawYcheck;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables.
        clawX = new Claw(hardwareMap.get(Servo.class, "clawX"),hardwareMap.get(Servo.class, "spinnerX"));
        clawY = new Claw(hardwareMap.get(Servo.class, "clawY"),hardwareMap.get(Servo.class, "spinnerY"));
        slideX = hardwareMap.get(DcMotor.class, "slideX");
        slideY = hardwareMap.get(DcMotor.class, "slideY");

        //motor direction
        slideX.setDirection(DcMotor.Direction.REVERSE);
        slideY.setDirection(DcMotor.Direction.REVERSE);

        //artifical time and one button rotates
        spinXpos = 0;
        spinYpos = 0;
        spinXcheck = false;
        spinYcheck = false;
        clawXpos = 0;
        clawYpos = 0;
        clawXcheck = false;
        clawYcheck = false;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            /* FOR TESTING PURPOSES
            //claw x
            if(gamepad1.dpad_left){
                clawX.spinDownX();
            }
            if(gamepad1.dpad_right){
                clawX.spinUpX();
            }
            if(gamepad1.x){
                clawX.clawOpenX();
            }
            if(gamepad1.a){
                clawX.clawCloseX();
            }

            // claw y
            if(gamepad1.dpad_up){
                clawY.spinUpY();
            }
            if(gamepad1.dpad_down){
                clawY.spinDownY();
            }
            if(gamepad1.y){
                clawY.clawOpenY();
            }
            if(gamepad1.b){
                clawY.clawCloseY();
            }
             */

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


             //slide motor X
             if(gamepad1.right_trigger > 0) {
                    slideX.setPower(.5);
             }
              else if(gamepad1.left_trigger > 0) {
                    slideX.setPower(-.5);
             }
              else{
                 slideX.setPower(0);
             }

             //slide motor Y
             if(gamepad1.right_bumper) {
                    slideY.setPower(.5);
             }
             else if(gamepad1.left_bumper) {
                    slideY.setPower(-.5);
             }
             else{
                 slideY.setPower(0);
             }


            // Show the elapsed game time
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}