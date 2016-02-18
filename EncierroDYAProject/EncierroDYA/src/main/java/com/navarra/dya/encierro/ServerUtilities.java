package com.navarra.dya.encierro;

import android.content.Context;
import android.util.Log;
import com.google.android.gcm.GCMRegistrar;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.TAG;
import static com.navarra.dya.encierro.CommonUtilities.displayMessage;
import static com.navarra.dya.encierro.CommonUtilities.unregister_gcm_server;
import static com.navarra.dya.encierro.CommonUtilities.updateUser;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public final class ServerUtilities {
    /**
     * Register this account/device pair within the server.
     *
     */
    static void register(final Context context, String userId, final String regId, String stand, String resource, String telephone, String status, String gps_position) {

        GCMRegistrar.setRegisteredOnServer(context, true);
        String message = context.getString(R.string.server_registered);
        CommonUtilities.displayMessage(context, message);
        displayMessage(context, "Your device registed in the server");

        Log.i(TAG, "registering device (regId = " + regId + ")");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("regId", regId));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("stand", stand));
        params.add(new BasicNameValuePair("resource", resource));
        params.add(new BasicNameValuePair("telephone", telephone));
        params.add(new BasicNameValuePair("status", status));
        params.add(new BasicNameValuePair("gps_position", gps_position));
        params.add(new BasicNameValuePair("online", "true"));
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonInsert = jsonParser.makeHttpRequest(updateUser, "POST", params);

        Log.d("Create Response-New injured", jsonInsert.toString());
    }

    static void unregister(final Context context, String userId) {
        GCMRegistrar.setRegisteredOnServer(context, false);
        String message = context.getString(R.string.server_unregistered);
        CommonUtilities.displayMessage(context, message);

        Log.i(TAG, "unregistering device (regId = " + userId + ")");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userId", userId));
        JSONParser jsonUnregister = new JSONParser();
        JSONObject jsonUnre = jsonUnregister.makeHttpRequest(unregister_gcm_server,"POST", params);
        Log.d("Create Response-New injured", jsonUnre.toString());
    }
}
