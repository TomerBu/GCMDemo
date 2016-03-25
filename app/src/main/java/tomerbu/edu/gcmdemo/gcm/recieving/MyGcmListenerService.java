/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tomerbu.edu.gcmdemo.gcm.recieving;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import tomerbu.edu.gcmdemo.MainActivity;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Bundle notification = data.getBundle("notification");
        if (notification != null) {
            String message = notification.getString("message");
            if (from.startsWith("/topics/")) {
                // message received from some topic.
                Log.d(TAG, "message received from some topic: " + from);
            } else {
                // regular message (not topic related, token Related.
                Log.d(TAG, "regular message senderID: " + from); //from == Sender ID (Our application Sender ID, received from the google api console)
            }

            sendNotification(notification);

        } else /* It's a Data notification */ {
            String body = data.getString("body");
            String title = data.getString("title");
            String icon = data.getString("icon");
            if (body != null && title != null && icon != null) {
                sendNotification(data);
            }
        }
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    private int getDrawableResourceIdByName(String name) {
        String packageName = getPackageName();
        return getResources().getIdentifier(name, "drawable", packageName);
    }


    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notification Bundle.
     */
    private void sendNotification(Bundle notification) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getString("title"))
                .setContentText(notification.getString("body"))
                .setSmallIcon(getDrawableResourceIdByName(notification.getString("icon")))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
