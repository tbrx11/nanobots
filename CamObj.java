
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import java.util.concurrent.TimeUnit;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


public class CamObj{

    private static final String TFOD_MODEL_ASSET = "ConeModelOne.tflite";
    private static final String[] LABELS = {
            "2 Dumbbot",
            "1 Wheel",
            "3 Wrench"
    };
    //https://developer.vuforia.com/license-manager.
    private static final String VUFORIA_KEY =
            "AYw4sTz/////AAABmUg3BJkSkEZXrvoQBnWoR/1Hy5VG4NsOPfKRJxlfwXoCX5h+zdXk2xOVYt2bbVD8fyjChBIb6jCP4Ueq+xQ5em5MESAeEUJ9EW8RMApB4w0lpF4dy0pqJIKGGPONn53joK4DTkclEtsfsm3V0KWFaTvOlDw6F9N4hIFwAOjPPECd0/vi3TWxYsyITDlYM6+qwnJcbKRl6DaoUAwZYxX8zwGIYYXpLO0KJFUXSsNdoOwp3VqRpXL+8VcDm9J2zfIQhS3YKlJNKW7N/7rLT02ypAI4/YGsXvne6l0RKRMm/6GpZGlr0Ki+XUtcy9mBGf5gPEVeig+4XJl8vtfLUUUc/gK6/eWZ09IgHZaDECjF3WDz";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private int tfodViewId;
    private CameraName cameraName;
    private ElapsedTime time = new ElapsedTime();


    //parameter1 = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName())
    //parameter2 = hardwareMap.get(WebcamName.class, "Webcam 1")
    public CamObj(int viewId, CameraName camName) {
        tfodViewId = viewId;
        cameraName = camName;

        this.initVuforia();
        ExposureControl expoCont = vuforia.getCamera().getControl(ExposureControl.class);
        expoCont.setMode(ExposureControl.Mode.Manual);
        expoCont.setExposure(15, TimeUnit.MILLISECONDS);

        WhiteBalanceControl white = vuforia.getCamera().getControl(WhiteBalanceControl.class);
        white.setMode(WhiteBalanceControl.Mode.MANUAL);
        white.setWhiteBalanceTemperature(6000);

        this.initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.1, 16.0 / 9.0);
        }
    }

    public String getSide() {
        time.reset();
        while (tfod != null && time.time() < 6) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                for (Recognition recognition : updatedRecognitions) {
                    if ((recognition.getConfidence() * 100) >= .47) {
                        return recognition.getLabel();
                    }
                }
            }
        }
        return "no symbol :(";

    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = cameraName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }


    private void initTfod() {
        int tfodMonitorViewId = tfodViewId;
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.45f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }
}

