package com.navarra.dya.encierro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.PROJECT_ID;
import static com.navarra.dya.encierro.CommonUtilities.TAG_RESOURCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GPS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STATUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERID;
import static com.navarra.dya.encierro.CommonUtilities.broadcastMessage;
import static com.navarra.dya.encierro.CommonUtilities.registrationStatus;
import static com.navarra.dya.encierro.CommonUtilities.getStands;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class RegisterActivity extends Activity {
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

    private AsyncTask<Void,Void,Void> mRegisterTask;
	
	// Internet detector
	ConnectionDetector cd;
	
	// UI elements
	EditText txtUserId;
	
	// Register button
	Button btnRegister;
    String txtSpinner;
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Spinner spinner;
    static String resource, stand, userId, status="libre", gpsPosition, mPhoneNumber;
    private TextView lblUserId, lblPlace;

    // This intent filter will be set to filter on the string "GCM_RECEIVED_ACTION"
    IntentFilter gcmFilter;
    // array list for spinner adapter
    private ArrayList<Stands> categoriesList;
    JSONParser jsonParser = new JSONParser();


    private LocationManager locManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 15; // 1 minute
    private String gps_position;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            String provider = LocationManager.GPS_PROVIDER;
            comenzarLocalizacion(provider);
        }else  if (isNetworkEnabled) {
            // First get location from Network Provider
            String provider = LocationManager.NETWORK_PROVIDER;
            comenzarLocalizacion(provider);
        }

        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        // Create our IntentFilter, which will be used in conjunction with a
        // broadcast receiver.
        gcmFilter = new IntentFilter();
        gcmFilter.addAction("GCM_RECEIVED_ACTION");

        TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();

        userId=pref.getString(TAG_USERID, null);
        stand=pref.getString(TAG_STAND, null);
        resource=pref.getString(TAG_RESOURCE, null);
        status=pref.getString(TAG_STATUS, null);

        if (status==null)
            status="libre";

        txtUserId = (EditText) findViewById(R.id.txtUserId);
		btnRegister = (Button) findViewById(R.id.register);
        lblUserId = (TextView) findViewById(R.id.lblUserId);
        lblPlace = (TextView) findViewById(R.id.lblPlace);
        spinner = (Spinner) findViewById(R.id.spinnerPlace);

        categoriesList = new ArrayList<Stands>();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
                txtSpinner =(String)parent.getSelectedItem();
                stand = position+"";
                lblUserId.setVisibility(View.VISIBLE);
                txtUserId.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing Selected
            }
        });


 		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                userId = txtUserId.getText().toString();
               if (userId.equals("") || userId==null)
                    alert.showAlertDialog(RegisterActivity.this,"Error en proceso de registro","ID de usuario incorrecto", false);
                else
                    new Register().execute();
                  }
		});

        if (userId!=null && !userId.equals("")) {
            new Register().execute();
        }
    }

    private void comenzarLocalizacion(String provider)
    {
        locManager.getLastKnownLocation(provider);

        //Nos registramos para recibir actualizaciones de la posición
        LocationListener locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                gps_position = location(location);
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

    private String location(Location location){
        gps_position=String.valueOf(location.getLatitude())+"," + String.valueOf(location.getLongitude());
        return gps_position;
    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        //txtCategory.setText("");

        for (Stands aCategoriesList : categoriesList) {
            lables.add("Puesto " + aCategoriesList.getId() + ": " + aCategoriesList.getDescription());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);
    }


    //RESOURCE
    public void onResourceRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.rbResourceTransmisiones:
                if (checked)
                    resource="Transmisiones";
                else
                    resource="";
                break;
            case R.id.rbResourceConvencional:
                if (checked)
                    resource="Convencional";
                else
                    resource="";
                break;
            case R.id.rbResourceMedicalizada:
                if (checked)
                    resource="Medicalizada";
                else
                    resource="";
                break;
        }
        lblPlace.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        new GetCategories().execute();
    }

    /**
     * Background Async Task to Get complete product details
     * */
    class Register extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Espere por favor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... args) {
            gpsPosition=gps_position;

            if (gpsPosition==null)
                gpsPosition = "42.818526, -1.657795"; //Central DYA

                editor.putString(TAG_USERID, userId);// Storing string userId
                editor.putString(TAG_STAND, stand);// Storing string stand
                editor.putString(TAG_RESOURCE, resource);// Storing string stand
                editor.putString(TAG_GPS, gpsPosition);// Storing string stand
                editor.putString(TAG_STATUS, status);// Storing string stand
                editor.commit(); // commit changes


            if (resource.equalsIgnoreCase("Transmisiones")) {

                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        registrar();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);

                Intent i = new Intent(RegisterActivity.this, MenuActivity.class);
                // Registering user on our server
                // Sending registraiton details to MainActivity
                i.putExtra("userId", userId);
                i.putExtra("stand", stand);
                i.putExtra("resource", resource);
                i.putExtra("status", status);
                i.putExtra("gpsPosition", gpsPosition);
                startActivity(i);
                finish();
            } else {

                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        registrar();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        populateSpinner();
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                // Registering user on our server
                // Sending registraiton details to MainActivity
                i.putExtra("userId", userId);
                i.putExtra("stand", stand);
                i.putExtra("resource", resource);
                i.putExtra("status", status);
                i.putExtra("gps_position", gpsPosition);
                startActivity(i);
                finish();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    private void registrar(){
        try {
            // Check that the device supports GCM (should be in a try / catch)
            GCMRegistrar.checkDevice(this);

            // Check the manifest to be sure this app has all the required
            // permissions.
            GCMRegistrar.checkManifest(this);

            // Get the existing registration id, if it exists.
            String regId = GCMRegistrar.getRegistrationId(this);

            if (regId.equals("")) {
                registrationStatus = "Registrando...";
                // register this device for this project
                GCMRegistrar.register(this, PROJECT_ID);
                regId = GCMRegistrar.getRegistrationId(this);
                registrationStatus = "Registro realizado";
            } else {
                // Device is already registered on GCM
                if (!GCMRegistrar.isRegisteredOnServer(this)) {
                    ServerUtilities.register(this, userId, regId,stand,resource, mPhoneNumber,status, gpsPosition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            registrationStatus = e.getMessage();
        }
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

    // NOTE the call to GCMRegistrar.onDestroy()
    @Override
    public void onDestroy() {
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }

    // This broadcastreceiver instance will receive messages broadcast
    // with the action "GCM_RECEIVED_ACTION" via the gcmFilter
    // A BroadcastReceiver must override the onReceive() event.
    private BroadcastReceiver gcmReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getExtras().getString("message");
            String id_injured = intent.getExtras().getString("id_injured");
            String health_center = intent.getExtras().getString("health_center");
            broadcastMessage = "";

            if (!message.equals("")) {
                broadcastMessage = "AVISO: " + message;
            } else {
                broadcastMessage = "NO mensaje";
            }
            if (!id_injured.equalsIgnoreCase("--Id.herido--")) {
                //in.putExtra(TAG_ID_INJURED, id_injured);
                broadcastMessage = broadcastMessage + " - Id: " + id_injured;
            }
            if (!health_center.equalsIgnoreCase("--Centro hospitalario--")) {
                broadcastMessage = broadcastMessage + " - Hospital: " + health_center;
            }


            if (!id_injured.equalsIgnoreCase("--Id.herido--") || message.equals("") || !health_center.equalsIgnoreCase("--Centro hospitalario--")) {
                // Waking up mobile if it is sleeping
                WakeLocker.acquire(getApplicationContext());
                // display our received message
                Toast.makeText(getApplicationContext(), "Nuevo mensaje: " + broadcastMessage, Toast.LENGTH_LONG).show();
                // Releasing wake lock
                WakeLocker.release();

            }
        }
    };

    /**
     * Async task to get all food categories
     * */
    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Obteniendo puestos disponibles...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONParser jsonParser = new JSONParser();

            Log.e("Response: ", "> " + jsonParser);

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonObj = jsonParser.makeHttpRequest(getStands, "POST", params);

                if (jsonObj != null) {
                    JSONArray categories = jsonObj.getJSONArray("stands");

                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Stands cat = new Stands(catObj.getInt("id_stand"), catObj.getString("description"));
                        categoriesList.add(cat);
                    }
                }
            } catch (JSONException e) {
                e.getMessage();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }

    }
}

