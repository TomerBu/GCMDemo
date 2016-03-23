package tomerbu.edu.gcmdemo.gcm.recieving;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import tomerbu.edu.gcmdemo.MainActivity;
import tomerbu.edu.gcmdemo.R;

/**
 * Created by tomerbuzaglo on 23/03/2016.
 */
public class MyBroadcastReciever extends com.google.android.gms.gcm.GcmReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TomerBu", "Recieved");
        super.onReceive(context, intent);
        //sendNotification("Hello, There", context);
    }


    private void sendNotification(String message, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
