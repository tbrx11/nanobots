package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo clawServo;
    private Servo clawSpinner;
    private double startPos;
    private double endPos;
    private double spinnerPosDown;
    private double spinnerPosUp;

    //initialize claw
    public Claw(Servo clawServo, Servo clawSpinner) {
        this.clawServo = clawServo;
        this.clawSpinner = clawSpinner;

   //values to move servo
        this.startPos = 0;
        this.endPos = .5;

        this.spinnerPosDown = .5;
        this.spinnerPosUp = .1;
        
    //initial position of servos
        this.clawServo.setPosition(startPos);
        this.clawSpinner.setPosition(spinnerPosDown);
    }

    //opens and closes claw
    public void clawOpen(){
        clawServo.setPosition(endPos);
    } 
    public void clawClose(){
        clawServo.setPosition(startPos);
    }
    
    // spins claw up and down
    public void spinUp(){
        clawSpinner.setPosition(spinnerPosUp);
    }
    public void spinDown(){
        clawSpinner.setPosition(spinnerPosDown);
    }

     

}
