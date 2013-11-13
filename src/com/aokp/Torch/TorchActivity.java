
package com.aokp.Torch;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.provider.Settings;
import android.util.Log;
import android.os.Bundle;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

public class TorchActivity extends Activity {
    public static final String TAG = "TorchActivity";
    private boolean mUseCameraInterface, mHasNoHardwareFlash;
    private TorchApp mTorchApp;
    private SurfaceTexture mSurfaceTexture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mUseCameraInterface = getResources().getBoolean(R.bool.useCameraInterface);
        mHasNoHardwareFlash = getResources().getBoolean(R.bool.hasNoHardwareFlash);
        boolean torchStatus = Settings.System.getBoolean(getContentResolver(),
                Settings.System.TORCH_STATE, false);
        mTorchApp = (TorchApp) getApplicationContext();
        if (mHasNoHardwareFlash) {
            Intent intent = new Intent(this, WhiteScreen.class);
            startActivity(intent);
        }
        else {
            if (mUseCameraInterface) {
                int[] textures = new int[1];
                GLES20.glGenTextures(1, textures, 0);
                GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                        textures[0]);
                GLES20.glTexParameterf(
                        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
                GLES20.glTexParameterf(
                        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                GLES20.glTexParameteri(
                        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(
                        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
                mSurfaceTexture = new SurfaceTexture(textures[0]);
                if (torchStatus) {
                    mTorchApp.getCameraManager().turnOff();
                } else {
                    mTorchApp.getCameraManager().turnOn(mSurfaceTexture);
                }
            } else {
                // handle writing torch settings in one method
                mTorchApp.handleTorchStatusSwitching(torchStatus);
            }
            finish();


        }
    }
}
