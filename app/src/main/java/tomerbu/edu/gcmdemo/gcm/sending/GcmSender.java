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

package tomerbu.edu.gcmdemo.gcm.sending;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tomerbu.edu.gcmdemo.utils.IOUtils;

// NOTE:
// This class emulates a server for the purposes of this sample,
// but it's not meant to serve as an example for a production app server.
// This class should also not be included in the client (Android) application
// since it includes the server's API key. For information on GCM server
// implementation see: https://developers.google.com/cloud-messaging/server
public class GcmSender {

    private static final String API_KEY = "AIza...";

    public static void sendGcmMessage(final String title, final String body, final String icon, final String to) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                send(title, body, icon, to);
                return null;
            }
        }.execute();

    }

    private static void send(String title, String body, String icon, String to) {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jNotification = new JSONObject();
            jNotification.put("title", title);
            jNotification.put("body", body);
            jNotification.put("icon", icon);
            // Where to send GCM message.
            if (to != null) {
                jGcmData.put("to", to);
            } else {
                jGcmData.put("to", "/topics/global");
            }


            // What to send in GCM message.
           // jGcmData.put("notification", jNotification);
            jGcmData.put("data", jNotification);
            jGcmData.put("priority", "high");
            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (Exception e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
    }

}


/*
*
* {
  "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
  "priority" : "normal",
  "notification" : {
    "body" : "This week’s edition is now available.",
    "title" : "NewsMagazine.com",
    "icon" : "new",
  },
  "data" : {
    "volume" : "3.21.15",
    "contents" : "http://www.news-magazine.com/world-week/21659772"
  }
}
*/