package com.xyz.led;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton tbSwitch;
    boolean hasFlash;
    boolean isFlashOn;
    private Camera camera;
    Camera.Parameters params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbSwitch= (ToggleButton) findViewById(R.id.tbSwitch);

        PackageManager pm=getApplicationContext().getPackageManager();
        hasFlash=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash){
            Toast.makeText(this, "No Flash", Toast.LENGTH_LONG).show();
            return;
        }

        tbSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlashOn)
                    turnOffFlash();
                else
                    turnOnFlash();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(camera==null){
            try{
                camera=Camera.open();
                turnOnFlash();
            } catch (RuntimeException e){
                Toast.makeText(getApplicationContext(), "Camera Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void turnOnFlash(){
        if(!isFlashOn){
            params=camera.getParameters();
            params.setFlashMode(params.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn=true;
        }
    }

    private void turnOffFlash(){
        if(isFlashOn){
            params=camera.getParameters();
            params.setFlashMode(params.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn=false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        turnOnFlash();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onStop() {
        super.onStop();
        camera.release();
        camera=null;
    }
}
