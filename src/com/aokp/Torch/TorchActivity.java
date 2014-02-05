
package com.aokp.Torch;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import javax.microedition.khronos.opengles.GL10;

public class TorchActivity extends Activity {
    public static final String TAG = "TorchActivity";
    private boolean mUseCameraInterface, mHasNoHardwareFlash;
    private boolean forceWhiteScreen, mWhiteScreen;
    private TorchApp mTorchApp;
    private SurfaceTexture mSurfaceTexture;
    private int brightness = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUseCameraInterface = getResources().getBoolean(R.bool.useCameraInterface);
        mHasNoHardwareFlash = getResources().getBoolean(R.bool.hasNoHardwareFlash);
        forceWhiteScreen = getIntent().getBooleanExtra("whitescreen", false);
        mWhiteScreen = (forceWhiteScreen || mHasNoHardwareFlash);
        boolean torchStatus = Settings.AOKP.getBoolean(getContentResolver(),
                Settings.AOKP.TORCH_STATE, false);

        if (mWhiteScreen) {
            setContentView(R.layout.whitescreen);
            setTheme(R.style.WhiteScreenTheme);
            getActionBar().hide();
            View whiteContent = findViewById(R.id.whitescreen_content);
            whiteContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast info = Toast.makeText(TorchActivity.this, R.string.toast_whitescreen, Toast.LENGTH_SHORT);
                    info.show();
                }
            });
            if (!torchStatus) {
                try {
                    brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);

                View decorView = getWindow().getDecorView();
                decorView.setBackgroundColor(Color.WHITE);
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                Settings.AOKP.putBoolean(getContentResolver(),
                        Settings.AOKP.TORCH_STATE, true);

            } else {
                exitWhite();
            }

        }
        else {
            setContentView(R.layout.main);
            setTheme(R.style.TransparentTheme);
            mTorchApp = (TorchApp) getApplicationContext();
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

    public void exitWhite() {
        if (brightness > 0) {
            Settings.System.putInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
        Settings.AOKP.putBoolean(getContentResolver(),
                Settings.AOKP.TORCH_STATE, false);
        finish();
        android.os.Process.killProcess( android.os.Process.myPid() );
    }

    @Override
    public void onPause () {
        if (mWhiteScreen)
            exitWhite();
    }

    @Override
    public void onStop () {
        if (mWhiteScreen)
            exitWhite();
    }

    @Override
    public void onBackPressed() {
        if (mHasNoHardwareFlash)
            exitWhite();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWhiteScreen) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                    || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                event.startTracking();
                return true;
            }  else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mWhiteScreen) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                    || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                event.startTracking();
                exitWhite();
                return true;
            }  else {
                return super.onKeyUp(keyCode, event);
            }
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

}
