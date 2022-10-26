
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;




@TeleOp(name="ClawTest", group="Linear Opmode")
public class ClawTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private Claw claw;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables.
        claw = new Claw(hardwareMap.get(Servo.class, "claw"),hardwareMap.get(Servo.class, "spinner"));
                

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            
            //test claw
            if (gamepad1.right_trigger > 0) {
                claw.clawOpen();
            }
            if(gamepad1.left_trigger > 0) {
                claw.clawClose();
            }
            
            //spins claw up and down
            if (gamepad1.y && claw.isClosed()) {
                claw.spinUp();
            }
            if(gamepad1.x && claw.isClosed()) {
                claw.spinDown();
            }


            // Show the elapsed game time
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
