package com.aokp.Torch;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;

public class TorchActivity extends Activity implements SurfaceHolder.Callback {
	public static final String TAG = "AOKPTorchAct";
    public static final String TORCH_ON = "com.android.systemui.INTENT_TORCH_ON";
    public static final String TORCH_OFF = "com.android.systemui.INTENT_TORCH_OFF";
    public static final String TORCH_TOGGLE = "com.android.systemui.INTENT_TORCH_TOGGLE";
    
	private TorchApp app;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private String act;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        app = (TorchApp)getApplicationContext();
        
        act = getIntent().getAction();
        if(act == null){
        	act = TORCH_TOGGLE;
        }
        //Log.i(TAG, act);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if(act.equals(TORCH_ON)){
        	app.camMan.turnOn(holder);
        }else if(act.equals(TORCH_OFF)){
            app.camMan.turnOff();
        }else{
        	app.camMan.toggle(holder);
        }
    	finish();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {}
}
