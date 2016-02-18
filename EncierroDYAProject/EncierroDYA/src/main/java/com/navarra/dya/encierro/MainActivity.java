package com.navarra.dya.encierro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_MESSAGE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PASS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_RESOURCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STATUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERID;
import static com.navarra.dya.encierro.CommonUtilities.broadcastMessage;
import static com.navarra.dya.encierro.CommonUtilities.updateGPS;

public class MainActivity extends Activity {
    static TextView lbl_online, lbl_userId;
    static ImageView online;
    IntentFilter gcmFilter;
    ImageButton btnLogOut;
    Intent in;
    private JSONParser jsonParser1 = new JSONParser();
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    String id_injured=null;
    String test="false";
    ImageView libre;
    private LocationManager locManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 15; // 15 minutes
    static String userId, stand, resource, mPhoneNumber, status, gpsPosition, typeList, message;


    // Reminder that the onCreate() method is not just called when an app is first opened,
    // but, among other occasions, is called when the device changes orientation.
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        setContentView(R.layout.ambulance_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        online = (ImageView) findViewById(R.id.online);
        lbl_online = (TextView) findViewById(R.id.lbl_online);
        lbl_userId = (TextView) findViewById(R.id.lbl_userId);
        libre = (ImageView) findViewById(R.id.ok);

        //Getting userId, location, resource from intent
        Intent i= getIntent();
        userId=i.getStringExtra("userId");
        stand=i.getStringExtra("stand");
        resource=i.getStringExtra("resource");
        status=i.getStringExtra("status");
        gpsPosition=i.getStringExtra("gpsPosition");
        message=i.getStringExtra(TAG_MESSAGE);
        typeList=i.getStringExtra(TAG_TYPE_LIST_INJURED);

        TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();
        btnLogOut = (ImageButton) findViewById(R.id.exit);

        if ( (!userId.equals("") || userId!=null)) {
            lbl_online.setText("REGISTRADO");
            online.setImageResource(R.drawable.greenbutton);
            lbl_userId.setText(userId);
        }else{
            lbl_online.setText("NO REGISTRADO");
            online.setImageResource(R.drawable.red_button);
        }

        // Create our IntentFilter, which will be used in conjunction with a
        // broadcast receiver.
        gcmFilter = new IntentFilter();
        gcmFilter.addAction("GCM_RECEIVED_ACTION");


        //Obtenemos una referencia al LocationManager
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled || !isNetworkEnabled) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Servicio de localización no activado");
            builder.setMessage("Por favor, habilite el servicio de localización y GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        if (isGPSEnabled) {
            String provider = LocationManager.GPS_PROVIDER;
            comenzarLocalizacion(provider);
            //comenzarLocalizacionGPS();
        }else  if (isNetworkEnabled) {
            // First get location from Network Provider
            String provider = LocationManager.NETWORK_PROVIDER;
            comenzarLocalizacion(provider);
        }

        status=pref.getString(TAG_STATUS, null);// getting String status
        if (status!=null) {
            if (id_injured==null){
                id_injured=pref.getString(TAG_ID_INJURED,null);
            }
            if (id_injured!=null) {

                if (status.equalsIgnoreCase("libre")) {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("userId", userId));
                    params.add(new BasicNameValuePair("gpsPosition", gpsPosition));
                    params.add(new BasicNameValuePair("status", "urgencia"));
                    jsonParser1.makeHttpRequest(updateGPS, "POST", params);

                    in = new Intent(MainActivity.this, UrgenciaActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                else if (status.equalsIgnoreCase("urgencia")) {
                    in = new Intent(MainActivity.this, UrgenciaActivity.class);
                    in.putExtra("id_injured", id_injured);
                    in.putExtra("status", status);
                    in.putExtra("userId", userId);
                    in.putExtra("stand", stand);
                    in.putExtra("resource", resource);
                    in.putExtra("gpsPosition", gpsPosition);
                    in.putExtra(TAG_TYPE_LIST_INJURED, "oneInjured");
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("test", test);
                    startActivity(in);
                    finish();
                }
                else if (status.equalsIgnoreCase("camino")) {
                    in = new Intent(MainActivity.this, CaminoActivity.class);
                    in.putExtra("id_injured", id_injured);
                    in.putExtra("status", status);
                    in.putExtra("userId", userId);
                    in.putExtra("stand", stand);
                    in.putExtra("resource", resource);
                    in.putExtra("gpsPosition", gpsPosition);
                    in.putExtra(TAG_TYPE_LIST_INJURED, "oneInjured");
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("test", test);
                    startActivity(in);
                    finish();
                } else if (status.equalsIgnoreCase("cargado")) {
                    in = new Intent(MainActivity.this, CargadoActivity.class);
                    in.putExtra("id_injured", id_injured);
                    in.putExtra("status", status);
                    in.putExtra("userId", userId);
                    in.putExtra("stand", stand);
                    in.putExtra("resource", resource);
                    in.putExtra("gpsPosition", gpsPosition);
                    in.putExtra(TAG_TYPE_LIST_INJURED, "oneInjured");
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("test", test);
                    startActivity(in);
                    finish();
                } else if (status.equalsIgnoreCase("hospital")) {
                    in = new Intent(MainActivity.this, HospitalActivity.class);
                    in.putExtra("id_injured", id_injured);
                    in.putExtra("status", status);
                    in.putExtra("userId", userId);
                    in.putExtra("stand", stand);
                    in.putExtra("resource", resource);
                    in.putExtra("gpsPosition", gpsPosition);
                    in.putExtra(TAG_TYPE_LIST_INJURED, "oneInjured");
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("test", test);
                    startActivity(in);
                    finish();
                }
                editor.putString(TAG_STATUS, status);

            } else {
                    in = new Intent(MainActivity.this, UrgenciaActivity.class);
                    status = "urgencia";
                    editor.putString(TAG_STATUS, status);
            }

            in.putExtra("status", status);
            in.putExtra("userId", userId);
            in.putExtra("stand", stand);
            in.putExtra("resource", resource);
            in.putExtra("gpsPosition", gpsPosition);
            in.putExtra(TAG_TYPE_LIST_INJURED, "oneInjured");
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borramos los datos del sharedPreferences
                editor.remove(TAG_USER);
                editor.remove(TAG_PASS);
                editor.remove(TAG_USERID);
                editor.remove(TAG_STAND);
                editor.remove(TAG_RESOURCE);
                editor.remove(TAG_STATUS);
                editor.remove(TAG_ID_INJURED);
                editor.remove(TAG_MESSAGE);
                editor.clear();
                editor.commit();

                ServerUtilities.unregister(MainActivity.this,userId); //Cerrar sesión
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
    }

    // Creamos un menú con 1 opcion: Chat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    //Menu contextual
    //Modos de funcionamiento: Normal y chat
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.chat:
                Intent i = new Intent(MainActivity.this, ChatActivity_old.class);
                // sending id to next activity
                i.putExtra(TAG_TYPE_LIST_INJURED, "stand");
                i.putExtra("stand",stand);
                i.putExtra("userId",userId);
                i.putExtra("resource",resource);
                i.putExtra("status",status);
                i.putExtra("gpsPosition",gpsPosition);
                i.putExtra("message",message);
                i.putExtra("test",test);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Para salir y levantar el dispositivo debe cerrar sesión", Toast.LENGTH_LONG).show();
    }


    // If the user changes the orientation of his phone, the current activity
    // is destroyed, and then re-created.  This means that our broadcast message
    // will get wiped out during re-orientation.
    // So, we save the broadcastmessage during an onSaveInstanceState()
    // event, which is called prior to the destruction of the activity.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("BroadcastMessage", broadcastMessage);

    }

    // When an activity is re-created, the os generates an onRestoreInstanceState()
    // event, passing it a bundle that contains any values that you may have put
    // in during onSaveInstanceState()
    // We can use this mechanism to re-display our last broadcast message.

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        broadcastMessage = savedInstanceState.getString("BroadcastMessage");
    }


    // If our activity is paused, it is important to UN-register any
    // broadcast receivers.
    @Override
    protected void onPause() {
        unregisterReceiver(gcmReceiver);
        super.onPause();
    }

    // When an activity is resumed, be sure to register any
    // broadcast receivers with the appropriate intent
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gcmReceiver, gcmFilter);
    }


    // This broadcastreceiver instance will receive messages broadcast
    // with the action "GCM_RECEIVED_ACTION" via the gcmFilter

    // A BroadcastReceiver must override the onReceive() event.
    private BroadcastReceiver gcmReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastMessage="";
            String id_injured=intent.getExtras().getString("id_injured");
            String health_center =intent.getExtras().getString("health_center");
            String message=intent.getExtras().getString("message");

            if (!id_injured.equalsIgnoreCase("--Id.herido--")){
                in.putExtra(TAG_ID_INJURED,id_injured);
                broadcastMessage=broadcastMessage+ " - Id: " + id_injured;
            }
            if (!health_center.equalsIgnoreCase("--Centro hospitalario--")){
                broadcastMessage=broadcastMessage+ " - Hospital: " + health_center;
            }


            if (!id_injured.equalsIgnoreCase("--Id.herido--")  || message.equals("") || !health_center.equalsIgnoreCase("--Centro hospitalario--")) {
                // Waking up mobile if it is sleeping
                WakeLocker.acquire(getApplicationContext());

                Toast.makeText(getApplicationContext(), "Nuevo mensaje: " + broadcastMessage, Toast.LENGTH_LONG).show();

                // Releasing wake lock
                WakeLocker.release();
            }
        }
    };

    private void comenzarLocalizacion(String provider)
    {
        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(provider);

        //Mostramos la última posición conocida
        mostrarPosicion(loc);

        //Nos registramos para recibir actualizaciones de la posición
        LocationListener locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            public void onProviderDisabled(String provider) {
                //Toast.makeText(getApplicationContext(), "NETWORK OFF", Toast.LENGTH_LONG).show();
            }

            public void onProviderEnabled(String provider) {
                //Toast.makeText(getApplicationContext(), "NETWORK ON", Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Log.i("", "Provider Status: " + status);
                //Toast.makeText(getApplicationContext(), "GPS status: " + status, Toast.LENGTH_LONG).show();
            }
        };

        locManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locListener);
    }

    private void mostrarPosicion(Location loc) {
        if(loc != null)
        {
            //Toast.makeText(getApplicationContext(), provider+ "# Latitud: " + String.valueOf(loc.getLatitude()) + " - Longitud: " + String.valueOf(loc.getLongitude()), Toast.LENGTH_LONG).show();
            //Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
            actualizarBBDD(loc, userId);
        }

    }

    private void actualizarBBDD(Location location, String userId){
        String gps_position = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("gps_position", gps_position));
        JSONParser jsonParser = new JSONParser();

        jsonParser.makeHttpRequest(updateGPS, "POST", params);
    }

}
