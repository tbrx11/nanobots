package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;


public class MiniClawRun extends Thread{

    private Claw clawX;
    private Claw clawY;
    private DcMotor slideY;
    private int pole;

    public MiniClawRun(Claw x, Claw y, DcMotor sY, int pole) throws InterruptedException {
        clawX = x;
        clawY = y;
        slideY = sY;
        this.pole = pole;
    }

    @Override
    public void run() {
        //claw x spin if clawX is closed
        if (pole == 3) {
            try {
                clawX.clawCloseX();
                Thread.sleep(600);
                clawX.spinUpX();
                Thread.sleep(1050);
                clawY.clawCloseY();
                //Thread.sleep(680);
                clawX.clawOpenX();
                Thread.sleep(200);
                slideY.setTargetPosition(3000);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideY.setPower(1);
                clawY.spinUpY();
                Thread.sleep(1600);
                slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slideY.setPower(0);
            } catch (InterruptedException e) {
                clawX.clawCloseX();
            }
        }
        if (pole == 2) {
            try {
                clawX.clawCloseX();
                Thread.sleep(600);
                clawX.spinUpX();
                Thread.sleep(1050);
                clawY.clawCloseY();
                //Thread.sleep(680);
                clawX.clawOpenX();
                Thread.sleep(200);
                slideY.setTargetPosition(900);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideY.setPower(1);
                clawY.spinUpY();
                Thread.sleep(800);
                slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slideY.setPower(0);
            } catch (InterruptedException e) {
                clawX.clawCloseX();
            }
        }
        if (pole == 1) {
            try {
                clawX.clawCloseX();
                Thread.sleep(600);
                clawX.spinUpX();
                Thread.sleep(1050);
                clawY.clawCloseY();
                //Thread.sleep(680);
                clawX.clawOpenX();
                Thread.sleep(200);
                slideY.setTargetPosition(1900);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideY.setPower(1);
                clawY.spinUpY();
                Thread.sleep(700);
                slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slideY.setPower(0);
            } catch (InterruptedException e) {
                clawX.clawCloseX();
            }
        }
        if (pole == 0) {
            try {
                clawY.clawCloseY();
                clawX.clawCloseX();
                clawX.spinDownX();
                clawY.spinDownY();
                slideY.setTargetPosition(0);
                slideY.setPower(-1);
                slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                clawX.clawOpenX();
                Thread.sleep(1500);
                clawY.clawOpenY();
                slideY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slideY.setPower(0);
            } catch (InterruptedException e) {
                clawX.clawCloseX();
            }
        }
    }

}
