package com.aokp.Torch;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;

public class WhiteScreen extends Activity {

    private int brightness = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whitescreen);
        setTheme(R.style.Theme_WhiteScreen);

        try {
            brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        Settings.System.putInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);


    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (brightness > 0) {
            Settings.System.putInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (brightness > 0) {
            Settings.System.putInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

}
