
package com.aokp.Torch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TorchReceiver extends BroadcastReceiver {
    public static final String TAG = "TorchReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            Intent i = new Intent(context, TorchActivity.class);
            i.setAction(intent.getAction());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
