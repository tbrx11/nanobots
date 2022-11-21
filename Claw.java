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
    //for y claw
    private final double spinPosYi;
    private final double spinPosYf;

    //initialize claw
    public Claw(Servo clawServo, Servo clawSpinner) {
        this.clawServo = clawServo;
        this.clawSpinner = clawSpinner;

//open end start close
        //values to move servo
        this.startPosX = .45;
        this.endPosX = .1;

        this.startPosY = .55;
        this.endPosY = .1;

        this.spinPosXi = .6;
        this.spinPosXf = 0;

        this.spinPosYi = .2;
        this.spinPosYf = .875;
    }

    //opens and closes claws
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

}
