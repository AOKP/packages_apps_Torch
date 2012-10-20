package com.aokp.Torch;

import android.app.Application;
import android.hardware.Camera;
import android.provider.Settings;
import android.view.SurfaceHolder;

public class TorchApp extends Application{
	public static final String TAG = "AOKPTorchApp";
    public static final String KEY_TORCH_ON = "torch_on";
	
	public CameraManager camMan = new CameraManager();
	
	public boolean actStarted;
	
	public class CameraManager{
		public Camera cam;
		
		public void Connect(){
	    	if(cam == null){
	    		try{
	    			cam = Camera.open();
	    		}catch(Exception e){
		        	e.printStackTrace();
	    		}
	    	}
	    	if(cam != null){
	    		try {
					cam.reconnect();
				}catch (Exception e) {
		        	e.printStackTrace();
				}
	    	}
		}
		
		public void turnOn(SurfaceHolder holder){
			Connect();
	    	if(cam != null){
		        try {
		            cam.stopPreview();
		        	Camera.Parameters params = cam.getParameters();
		            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		            cam.setParameters(params);
		        	cam.setPreviewDisplay(holder);
		            cam.startPreview();
			        cam.unlock();
			        Settings.System.putInt(getContentResolver(), KEY_TORCH_ON, 1);
		        }catch (Exception e) {
		        	e.printStackTrace();
		        }
	    	}
		}
		
		public void turnOff(){
			Connect();
	    	if(cam != null){
				try{
					Camera.Parameters params = cam.getParameters();
					params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO); 
			        cam.setParameters(params);
			        cam.stopPreview();
			        cam.unlock();
			        Settings.System.putInt(getContentResolver(), KEY_TORCH_ON, 0);
				}catch (Exception e) {
		        	e.printStackTrace();
		        }
				Release();
	    	}
		}
		
		public void toggle(SurfaceHolder holder){
			Connect();
	    	if(cam != null){
		        try {
		        	Camera.Parameters params = cam.getParameters();
					if(Camera.Parameters.FLASH_MODE_TORCH.equals(params.getFlashMode())){
						params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); 
				        cam.setParameters(params);
				        cam.stopPreview();
				        cam.unlock();
						Release();
				        Settings.System.putInt(getContentResolver(), KEY_TORCH_ON, 0);
					}else{
			            cam.stopPreview();
			            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			            cam.setParameters(params);
			        	cam.setPreviewDisplay(holder);
			            cam.startPreview();
				        cam.unlock();
				        Settings.System.putInt(getContentResolver(), KEY_TORCH_ON, 1);
					}
		        }catch (Exception e) {
		        	e.printStackTrace();
		        }
	    	}
		}
		
		public void Release(){
	    	if(cam != null){
	    		cam.release();
	    		cam = null;
	    	}
		}
	}
}