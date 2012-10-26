
package com.aokp.Torch;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;

public class TorchActivity extends Activity implements SurfaceHolder.Callback {
    public static final String TAG = "AOKPTorchAct";
    public static final String TORCH_ON = "com.aokp.torch.INTENT_TORCH_ON";
    public static final String TORCH_OFF = "com.aokp.torch.INTENT_TORCH_OFF";
    public static final String TORCH_TOGGLE = "com.aokp.torch.INTENT_TORCH_TOGGLE";

    private TorchApp mApplication;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private String mAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mApplication = (TorchApp) getApplicationContext();

        mAction = getIntent().getAction();
        if (mAction == null) {
            mAction = TORCH_TOGGLE;
        }
        // Log.i(TAG, act);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mAction.equals(TORCH_ON)) {
            mApplication.mCamManager.turnOn(holder);
        } else if (mAction.equals(TORCH_OFF)) {
            mApplication.mCamManager.turnOff();
        } else {
            mApplication.mCamManager.toggle(holder);
        }
        finish();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }
}
