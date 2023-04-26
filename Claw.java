package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo clawServo;
    private Servo clawSpinner;
    //open close
    private final double startPosX;
    private final double endPosX;
    private final double startPosY;
    private final double endPosY;
    //for x claw
    private final double spinPosXi;
    private final double spinPosXf;
    private final double spinPosXmid;
    //for y claw
    private final double spinPosYi;
    private final double spinPosYf;

    //initialize claw
    public Claw(Servo clawServo, Servo clawSpinner) {
        this.clawServo = clawServo;
        this.clawSpinner = clawSpinner;

//open end start close
        //values to move servo
        this.startPosX = .7;
        this.endPosX = 0;

        this.startPosY = .63;
        this.endPosY = .3;

        this.spinPosXi = .80;
        this.spinPosXf = .08;
        this.spinPosXmid = .25;

        this.spinPosYi = .15;
        this.spinPosYf = .8;
    }

    //opens and closes claws
    //beep boop
    public void clawXinit(){
        clawServo.setPosition(startPosX);
        clawSpinner.setPosition(spinPosXi);
    }

    public void clawYinit(){
        clawServo.setPosition(startPosY);
        clawSpinner.setPosition(spinPosYi);
    }

    //open close funcs
    public void clawCloseX() {
        clawServo.setPosition(endPosX);
    }
    public void clawOpenX(){
        clawServo.setPosition(startPosX);
    }

    public void clawCloseY(){
        clawServo.setPosition(endPosY);
    }
    public void clawOpenY(){
        clawServo.setPosition(startPosY);
    }


    public void spinUpX(){
        clawSpinner.setPosition(spinPosXf);
    }
    public void spinDownX(){
        clawSpinner.setPosition(spinPosXi);
    }

    public void spinUpY(){
        clawSpinner.setPosition(spinPosYf);
    }
    public void spinDownY(){
        clawSpinner.setPosition(spinPosYi);
    }

    public void spinMidX(){
        clawSpinner.setPosition(spinPosXmid);
    }

}