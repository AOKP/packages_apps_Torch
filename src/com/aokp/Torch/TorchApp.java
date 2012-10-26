
package com.aokp.Torch;

import android.app.Application;
import android.hardware.Camera;
import android.provider.Settings;
import android.view.SurfaceHolder;

public class TorchApp extends Application {
    public static final String TAG = "AOKPTorchApp";
    public static final String KEY_TORCH_ON = "torch_on";

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

        public void turnOn(SurfaceHolder holder) {
            connectToCam();
            if (mCamera != null) {
                try {
                    mCamera.stopPreview();
                    Camera.Parameters params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(params);
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                    mCamera.unlock();
                    Settings.System.putBoolean(getContentResolver(), KEY_TORCH_ON, true);
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
                    mCamera.unlock();
                    Settings.System.putBoolean(getContentResolver(), KEY_TORCH_ON, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                releaseCam();
            }
        }

        public void toggle(SurfaceHolder holder) {
            connectToCam();
            if (mCamera != null) {
                try {
                    Camera.Parameters params = mCamera.getParameters();
                    if (Camera.Parameters.FLASH_MODE_TORCH.equals(params.getFlashMode())) {
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(params);
                        mCamera.stopPreview();
                        mCamera.unlock();
                        releaseCam();
                        Settings.System.putBoolean(getContentResolver(), KEY_TORCH_ON, false);
                    } else {
                        mCamera.stopPreview();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(params);
                        mCamera.setPreviewDisplay(holder);
                        mCamera.startPreview();
                        mCamera.unlock();
                        Settings.System.putBoolean(getContentResolver(), KEY_TORCH_ON, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
