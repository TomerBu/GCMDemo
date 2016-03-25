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

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import tomerbu.edu.gcmdemo.R;

/**
 * register with GCM.
 * subscribe the user to topics
 * Send the subscription token to our Web server. Optional.
 **/

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String SENT_TOKEN_TO_SERVER = "sent.token.to.server";

    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * Initially this call goes out to the network to retrieve the token, subsequent calls are local. (intent service runs in background)
     * R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
     * See https://developers.google.com/cloud-messaging/android/start for details on this file.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = getSharedPreferences("gcm", MODE_PRIVATE);

        try {


            //getting the instanceID
            InstanceID instanceID = InstanceID.getInstance(this);

            //Get the token from the InstanceId : instanceId.getToken(senderID)
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null); //"1576528730" senderId from google api console
            //(There is only one scope, we don't need to pass any bundle. (this is just not overloaded properly)

            Log.i(TAG, "GCM Registration Token: " + token);

            // optionally send the tokens to our webserver. since we use topics this is not necessary.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // Store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            String iTopic = "/topics/" + topic;
            pubSub.subscribe(token, iTopic, null);
        }
    }
    // [END subscribe_topics]

}
