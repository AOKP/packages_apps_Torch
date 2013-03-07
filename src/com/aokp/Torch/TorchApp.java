
package com.aokp.Torch;

import android.app.Application;
import android.hardware.Camera;
import android.graphics.SurfaceTexture;
import android.provider.Settings;

public class TorchApp extends Application {
    public static final String TAG = "AOKPTorchApp";

    public CameraManager mCamManager = new CameraManager();

    public boolean actStarted;

    public class CameraManager {
        public Camera mCamera;

        public void connectToCam() {
            if (mCamera == null) {
                try {
                    mCamera = Camera.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mCamera != null) {
                try {
                    mCamera.reconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void turnOn(SurfaceTexture texture) {
            connectToCam();
            if (mCamera != null) {
                try {
                    mCamera.stopPreview();
                    mCamera.setPreviewTexture(texture);
                    mCamera.startPreview();
                    Camera.Parameters params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(params);
                    Settings.System.putBoolean(getContentResolver(), Settings.System.TORCH_STATE, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void turnOff() {
            connectToCam();
            if (mCamera != null) {
                try {
                    Camera.Parameters params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    mCamera.setParameters(params);
                    mCamera.stopPreview();
                    Settings.System.putBoolean(getContentResolver(), Settings.System.TORCH_STATE, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                releaseCam();
            }
        }

        public void releaseCam() {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }
}
