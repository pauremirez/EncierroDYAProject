package com.navarra.dya.encierro;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_RESOURCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STATUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_SUCCESS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERID;
import static com.navarra.dya.encierro.CommonUtilities.displayMessage;
import static com.navarra.dya.encierro.CommonUtilities.PROJECT_ID;
import static com.navarra.dya.encierro.CommonUtilities.asignInjuredResource;
import static com.navarra.dya.encierro.CommonUtilities.is_test;
import static com.navarra.dya.encierro.CommonUtilities.updateInjuredHealthCenter;

import android.support.v4.app.NotificationCompat;
/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static String id_injured, health_center, message, status;
    private static String resource,stand,userId;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    String test, sms;

    public GCMIntentService()
	{
		super(PROJECT_ID);
		Log.d(TAG, "GCMIntentService init");
	}


    /**
     * Function on error
     * */
	@Override
	protected void onError(Context ctx, String sError) {
		Log.d(TAG, "Error: " + sError);
        displayMessage(ctx,getString(R.string.gcm_error, sError));
	}

    /**
     * Function onMessage
     * */
	@Override
	protected void onMessage(Context ctx, Intent intent) {
        pref = ctx.getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();
		
		Log.d(TAG, "Message Received");
		
		message = intent.getStringExtra("message");
        id_injured = intent.getStringExtra("id_injured");
        health_center = intent.getStringExtra("health_center");
        status = intent.getStringExtra("status");
        stand = intent.getStringExtra("stand");
        resource = intent.getStringExtra("resource");



        if (id_injured==null && health_center==null && message!= null ){
            //Activar chat y generar notificacion --> GUARDAR CONVERSACION EN BBDD
            sendGCMIntentMessage(ctx, message);
            displayMessage(ctx, message);
            generateNotification(ctx);
        }else {

            //Comprobamos si el herido es de prueba o no
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("id_injured", id_injured));
            JSONParser jsonParserTest = new JSONParser();
            JSONObject jsonTest = jsonParserTest.makeHttpRequest(is_test, "POST", params2);

            try {
                int success = jsonTest.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received details
                    JSONArray injuredObj = jsonTest.getJSONArray("test"); // JSON Array

                    // get first object from JSON Array
                    JSONObject c = injuredObj.getJSONObject(0);

                    // Storing each json item in variable
                    test = c.getString("test");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (id_injured != null || message != null) {
                sendGCMIntent(ctx, message, id_injured, health_center, test);
                displayMessage(ctx, message);
                generateNotification(ctx);
            }
            if (id_injured != null && health_center != null) {
                editor.putString(TAG_ID_INJURED, id_injured);
                asignarHeridoHospital(id_injured, MainActivity.userId, health_center);
            }

        }
	}

    /**
     * Function that assigns a injured person to a health center
     * */
    private void asignarHeridoHospital(String id_injured, String userId, String health_center){
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("id_injured", id_injured));
        params2.add(new BasicNameValuePair("userId", userId));
        params2.add(new BasicNameValuePair("health_center", health_center));
        JSONParser jsonParser = new JSONParser();

        jsonParser.makeHttpRequest(updateInjuredHealthCenter, "POST", params2);
        jsonParser.makeHttpRequest(asignInjuredResource, "POST", params2);
    }

    /**
     * Function that sends a message
     * */
	private void sendGCMIntent(Context ctx, String message, String id_injured, String health_center, String test) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("GCM_RECEIVED_ACTION");
		
		broadcastIntent.putExtra("message", message);
        broadcastIntent.putExtra("id_injured", id_injured);
        broadcastIntent.putExtra("health_center", health_center);
        broadcastIntent.putExtra("test", test);

		ctx.sendBroadcast(broadcastIntent);
	}

    /**
     * Function that sends a message from server
     * */
    private void sendGCMIntentMessage(Context ctx, String message) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("GCM_RECEIVED_ACTION");
        broadcastIntent.putExtra("message", message);
        ctx.sendBroadcast(broadcastIntent);


        displayMessage(ctx, message);
        //generateNotification(ctx);

   }



    /**
     * Function on register
     * */
	@Override
	protected void onRegistered(Context ctx,  String regId) {
		// send regId to your server
		Log.d(TAG, "Device registered: redId="+regId);
		displayMessage(ctx,"Your device registrared with GCM");

        ServerUtilities.register(ctx, RegisterActivity.userId,regId,RegisterActivity.stand,RegisterActivity.resource,RegisterActivity.mPhoneNumber, RegisterActivity.status,RegisterActivity.gpsPosition);
	}

    /**
     * Function on unRegister
     * */
	@Override
	protected void onUnregistered(Context ctx, String regId) {
		// send notification to your server to remove that regId
        displayMessage(ctx,getString(R.string.gcm_registerred));
        ServerUtilities.unregister(ctx, regId);
	}

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context) {

        pref = context.getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent;

       // } else {
            if (resource == null || resource.equals(""))
                resource = pref.getString(TAG_RESOURCE, null);
            if (userId == null || userId.equals(""))
                userId = pref.getString(TAG_USERID, null);
            if (stand == null || stand.equals(""))
                stand = pref.getString(TAG_STAND, null);
            if (resource.equalsIgnoreCase("Transmisiones")) {
                notificationIntent = new Intent(context, MenuActivity.class);
                notificationIntent.putExtra(TAG_TYPE_LIST_INJURED, "all");
                if (message != null) {
                    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    if (componentInfo.getClassName().contains("ChatActivity")){
                        Intent i = new Intent(context, ChatActivity_old.class);
                        i.putExtra("userId",userId);
                        i.putExtra("status",status);
                        i.putExtra("resource",resource);
                        i.putExtra("stand",stand);
                        i.putExtra("test", test);
                        i.putExtra("message", message);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK ); // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);

                    }else {
                        notificationIntent = new Intent(context, ChatActivity_old.class);
                        notificationIntent.putExtra("message", message);
                        status = pref.getString(TAG_STATUS, null);
                        notificationIntent.putExtra("status", status);
                    }
                }else{
                        message = "Nuevo mensaje";
                }
                //message = "Nuevo mensaje";
            } else {
                if (id_injured.equalsIgnoreCase("--Id.herido--") || id_injured == null) {
                    notificationIntent = new Intent(context, MainActivity.class);
                    //message = "Nuevo mensaje";
                } else {
                    if (status.equalsIgnoreCase("cargado"))
                        notificationIntent = new Intent(context, CargadoActivity.class);
                    else if (status.equalsIgnoreCase("camino"))
                        notificationIntent = new Intent(context, CaminoActivity.class);
                    else if (status.equalsIgnoreCase("hospital"))
                        notificationIntent = new Intent(context, HospitalActivity.class);
                    else
                        notificationIntent = new Intent(context, UrgenciaActivity.class);
                    notificationIntent.putExtra("id_injured", id_injured);
                    notificationIntent.putExtra(TAG_TYPE_LIST_INJURED, "oneInjured");
                    editor.putString(TAG_STATUS, status);

                    message = "Urgencia";
                    if (health_center.equalsIgnoreCase("--Centro hospitalario--")) {
                        health_center = "";
                    }
                }
            }
      //  }
        //Notificacion de Chat --> solo se ejecuta se hace falta enviar una notificación y despertar al dispositivo
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (!componentInfo.getClassName().contains("ChatActivity")) {
            notificationIntent.putExtra("resource", resource);
            notificationIntent.putExtra("userId", userId);
            notificationIntent.putExtra("stand", stand);
            //notificationIntent.putExtra("test", test);
//            Notification notification = new Notification(icon, message, when);
            String title = context.getString(R.string.app_name);

            //***
            NotificationManager manager;
            NotificationCompat.Builder notificationCompact;
            notificationCompact = new NotificationCompat.Builder(this);
            notificationCompact.setContentTitle(title);
            notificationCompact.setContentText(message);
            notificationCompact.setTicker("¡Nuevo mensaje!");
            notificationCompact.setSmallIcon(R.drawable.ic_launcher);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 1000, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationCompact.setContentIntent(contentIntent);
            notificationCompact.setAutoCancel(true);
            manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notificationCompact.build());
            // Play default notification sound
            notificationCompact.setDefaults(Notification.DEFAULT_SOUND);
            // Vibrate if vibrate is enabled
            notificationCompact.setDefaults(Notification.DEFAULT_VIBRATE);
            //****

/*            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification.setLatestEventInfo(context, title, message, intent);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;

            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, notification);
*/
        }
    }
}
