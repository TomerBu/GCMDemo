package tomerbu.edu.gcmdemo.gcm.recieving;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tomerbuzaglo on 23/03/2016.
 */
public class MyBroadcastReciever extends com.google.android.gms.gcm.GcmReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyBroadcastReciever", "Received");
        super.onReceive(context, intent);
    }
}
