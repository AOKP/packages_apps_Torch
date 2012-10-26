
package com.aokp.Torch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TorchReceiver extends BroadcastReceiver {
    private TorchApp mApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mApplication == null) {
            mApplication = ((TorchApp) context.getApplicationContext());
        }

        if (intent.getAction() != null) {
            Intent i = new Intent(mApplication, TorchActivity.class);
            i.setAction(intent.getAction());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplication.startActivity(i);
        }
    }
}
