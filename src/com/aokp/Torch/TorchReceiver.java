package com.aokp.Torch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TorchReceiver extends BroadcastReceiver{
	private TorchApp app;
	
	@Override
	public void onReceive(Context context, Intent intent){
		if(app == null){
			app = ((TorchApp)context.getApplicationContext());
		};

		if(intent.getAction() != null){
			Intent i = new Intent(app, TorchActivity.class);
			i.setAction(intent.getAction());
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			app.startActivity(i);
		}
	}
}