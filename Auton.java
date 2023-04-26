package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.Thread;
import com.qualcomm.hardware.bosch.BNO055IMU;


//500 is 3 inches forward
//400 is 45 degrees

public class Auton{
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private DcMotor slideY = null;
    private DcMotor linActX = null;
    private Claw clawX;
    private Claw clawY;
    private CamObj cam;
    private BNO055IMU imu;
    private DistanceSensor distSens;
    private int[] actPos = {1000,1500,2000,2500,5000};

    public Auton(DcMotor lF, DcMotor lB, DcMotor rF, DcMotor rB, DcMotor slideY, Claw clawX, Claw clawY, DcMotor linActX, int viewId, CameraName camName, BNO055IMU imu, DistanceSensor dSens) throws InterruptedException {
        this.leftFront = lF;
        this.leftBack = lB;
        this.rightFront = rF;
        this.rightBack = rB;
        this.slideY = slideY;
        this.linActX = linActX;
        this.clawX = clawX;
        this.clawY = clawY;

        this.cam = new CamObj(viewId, camName);
        this.imu = imu;
        this.distSens = dSens;
    }

    public void initIMU() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode

        imu.initialize(parameters);

        // lastAngles = new Orientation();
    }

    public void initMotors() throws InterruptedException{
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        slideY.setDirection(DcMotor.Direction.REVERSE);

        linActX.setDirection(DcMotor.Direction.FORWARD);
        linActX.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        clawY.clawCloseY();
        clawX.spinUpX();
        //clawX.clawCloseX();
        clawY.spinDownY();
        slideY.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //slideY.setPower(0);
        //slideY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //slideY.setTargetPosition(0);
        //slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideY.setPower(-.5);
        Thread.sleep(500);
        slideY.setPower(0);
        slideY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //slideY.setPower(0);
    }

    public float getDeg(){
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public void move(int dist, double power){

        leftFront.setTargetPosition(dist+leftFront.getCurrentPosition());
        leftBack.setTargetPosition(dist+leftBack.getCurrentPosition());
        rightFront.setTargetPosition(dist+rightFront.getCurrentPosition());
        rightBack.setTargetPosition(dist+rightBack.getCurrentPosition());

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);
        while(!isDone()){

        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void strafe(int dist, double power){

        leftFront.setTargetPosition(leftFront.getCurrentPosition()-dist);
        leftBack.setTargetPosition(leftBack.getCurrentPosition()+dist);
        rightFront.setTargetPosition(rightFront.getCurrentPosition()+dist);
        rightBack.setTargetPosition(rightBack.getCurrentPosition()-dist);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(-1*power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(-1*power);
        while(!isDone()){

        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

    }

    //+ counter clockwise
    public void turn(double deg, double power){
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setPower(-1*power);
        leftBack.setPower(-1*power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (Math.abs(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - deg) > 1){

        }

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void scanTurn(double power) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setPower(-1 * power);
        leftBack.setPower(-1 * power);
        rightFront.setPower(power);
        rightBack.setPower(power);
    }

    public void transCone (int slide) throws InterruptedException {
        //clawX.clawCloseX();
        clawX.spinUpX();
        clawY.clawCloseY();
        clawX.clawOpenX();
        Thread.sleep(680);
        slideY.setTargetPosition(slide);
        slideY.setPower(.7);
        slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(slideY.isBusy()){

        }
        Thread.sleep(680);
        clawY.spinUpY();
        Thread.sleep(700);
        //clawY.clawOpenY();
        //Thread.sleep(800);
    }

    public void resetClaws() throws InterruptedException{
        clawY.clawCloseY();
        clawX.clawCloseX();
        //clawX.spinDownX();
        clawY.spinDownY();
        Thread.sleep(800);
        slideY.setPower(-.5);
        slideY.setTargetPosition(0);
        slideY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        clawX.clawOpenX();
        clawY.clawOpenY();
        while(slideY.isBusy()){

        }
    }

    public boolean isDone(){
        if(leftBack.isBusy() || leftBack.isBusy() || rightFront.isBusy() || rightBack.isBusy()){
            return false;
        }
        return true;
    }


    public String getEndLoc(){
        return cam.getSide();
    }

    public void poleScanAC(){
        scanTurn(.3);
        double deg =imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        while(Math.abs(distSens.getDistance(DistanceUnit.CM) - 25) > 3){
            if(Math.abs(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - (deg+15)) < 2){
                scanTurn(-.3);
            }
            else if(Math.abs(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle  - (deg-15)) < 2){
                scanTurn(.3);
            }
        }
        scanTurn(0);
    }

    public void poleScanC(){
        scanTurn(-.3);
        double deg =imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        while(Math.abs(distSens.getDistance(DistanceUnit.CM) - 25) > 3){
            if(Math.abs(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - (deg-30)) < 2){
                scanTurn(.3);
            }
            else if(Math.abs(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - deg) < 2){
                scanTurn(-.3);
            }
        }
        scanTurn(0);
    }

    public void coneStack(int pos){
        linActX.setTargetPosition(actPos[pos]);
        linActX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linActX.setPower(1);
        while(linActX.isBusy()){

        }
        clawX.clawCloseX();
        linActX.setTargetPosition(linActX.getCurrentPosition() + 500);
        linActX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linActX.setPower(1);
        while(linActX.isBusy()){

        }
        linActX.setTargetPosition(0);
        linActX.setPower(-1);
        move(300,.5);


    }

}
