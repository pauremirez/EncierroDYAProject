package com.navarra.dya.encierro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STATUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_SUCCESS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.updateGPS;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class CargadoActivity extends Activity  {

    String userId, resource, stand,id_injured, typeList;
    private String gpsPosition="", test;
    private JSONParser jsonParser1 = new JSONParser();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
//    private LocationManager locManager;
//    String providerAux="";
    // flag for GPS status
//    boolean isGPSEnabled = false;
    // flag for network status
//    boolean isNetworkEnabled = false;
    // The minimum distance to change Updates in meters
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
//    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.status_cargado);

        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent i= getIntent();
        userId=i.getStringExtra("userId");
        stand=i.getStringExtra("stand");
        resource=i.getStringExtra("resource");
        id_injured=i.getStringExtra(TAG_ID_INJURED);
        typeList=i.getStringExtra(TAG_TYPE_LIST_INJURED);
        test=i.getStringExtra("test");

        ImageButton btnCargado = (ImageButton) findViewById(R.id.btnCargado);
        Button btnDatosPaciente = (Button) findViewById(R.id.btnDatosPaciente);

        ImageView cargado = (ImageView) findViewById(R.id.cargado);
        if (test.equalsIgnoreCase("true")){
            cargado.setImageResource(R.drawable.cargado3_practicas);
        }else{
            cargado.setImageResource(R.drawable.cargado3);
        }
/*
        //COORDENADAS: latitud y longitid
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
        }else  if (isNetworkEnabled) {
            // First get location from Network Provider
            String provider = LocationManager.NETWORK_PROVIDER;
            comenzarLocalizacion(provider);
        }
*/
        // button click event
        btnCargado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id_injured", id_injured));
                params.add(new BasicNameValuePair("userId", userId));
                //        params.add(new BasicNameValuePair("gpsPosition", gpsPosition));
                params.add(new BasicNameValuePair("status", "camino"));
                params.add(new BasicNameValuePair("complete","false"));
                JSONObject json = jsonParser1.makeHttpRequest(updateGPS, "POST", params);

                // check json success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {


                        String status="camino";
                        editor.putString(TAG_STATUS, status);// Storing string status
                        editor.commit(); // commit changes

                        Intent i = new Intent(CargadoActivity.this, CaminoActivity.class);
                        i.putExtra("userId",userId);
                        i.putExtra("status",status);
                        i.putExtra("resource",resource);
                        i.putExtra("stand",stand);
                //        i.putExtra("gps_position",gpsPosition);
                        i.putExtra(TAG_ID_INJURED, id_injured);
                        i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                        i.putExtra("test", test);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

        // button click event
        btnDatosPaciente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String status="cargado";
                editor.putString(TAG_STATUS, status);// Storing string status
                editor.commit(); // commit changes
                Intent i = new Intent(CargadoActivity.this, AllInjuredActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("status","cargado");
                i.putExtra("resource",resource);
                i.putExtra("stand",stand);
                i.putExtra(TAG_ID_INJURED, id_injured);
                i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                i.putExtra("test", test);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Function on back Pressed
     * */
    @Override
    public void onBackPressed() {

    }

    /**
     * Function on back Pressed
     * @param provider - Network provider
     * */
 /*   private void comenzarLocalizacion(String provider)
    {
        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(provider);

        providerAux="";
        if (provider.equalsIgnoreCase(LocationManager.NETWORK_PROVIDER)) providerAux="NETWORK";
        else providerAux = "GPS";

        //Mostramos la última posición conocida
        mostrarPosicion(loc, providerAux);

        //Nos registramos para recibir actualizaciones de la posición
        LocationListener locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location, providerAux);
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
    }*/

    /**
     * Function that gets the current GPS location
     * @param loc - location
     * @param provider - provider
     * */
 /*   private void mostrarPosicion(Location loc, String provider) {
        if(loc != null)
        {
            Toast.makeText(getApplicationContext(), provider+ "# Latitud: " + String.valueOf(loc.getLatitude()) + " - Longitud: " + String.valueOf(loc.getLongitude()), Toast.LENGTH_LONG).show();
            //Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
            actualizarBBDD(loc, userId);
        }
    }
*/
    /**
     * Function that gets the current GPS location
     * @param location - location
     * @param userId - user identification
     * */
 /*   private void actualizarBBDD(Location location, String userId){
        String gps_position = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        if (gps_position.equalsIgnoreCase("")) gps_position="42.818430, -1.657445";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("status", "cargado"));
        params.add(new BasicNameValuePair("id_injured", id_injured));
        params.add(new BasicNameValuePair("complete","false"));
        params.add(new BasicNameValuePair("gps_position", gps_position));
        JSONParser jsonParser = new JSONParser();

        jsonParser.makeHttpRequest(updateGPS, "POST", params);
    }
*/
}