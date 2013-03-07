
package com.aokp.Torch;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.provider.Settings;
import android.os.Bundle;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

public class TorchActivity extends Activity {
    public static final String TAG = "AOKPTorchAct";

    private TorchApp mApplication;
    private SurfaceTexture mSurfaceTexture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mApplication = (TorchApp) getApplicationContext();

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

        boolean torchOn = Settings.System.getBoolean(getContentResolver(), Settings.System.TORCH_STATE, false);

        if (!torchOn) {
            mApplication.mCamManager.turnOn(mSurfaceTexture);
        } else {
            mApplication.mCamManager.turnOff();
        }
        finish();
    }
}
