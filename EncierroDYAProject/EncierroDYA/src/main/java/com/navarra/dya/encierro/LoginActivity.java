/**
 * Author: Paula Remirez
 * twitter: http://twitter.com/pauremirez
 * company: DYA Navarra
 * */
package com.navarra.dya.encierro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STATUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERNAME;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PASS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_SUCCESS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERID;
import static com.navarra.dya.encierro.CommonUtilities.TAG_RESOURCE;
import static com.navarra.dya.encierro.CommonUtilities.url_login_details;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class LoginActivity extends Activity {

    EditText loginUser;
    EditText loginPass;
    Button btnLogin;
    ArrayList<HashMap<String, String>> loginList;
    String user, pass, userShared=null, passShared=null;
    AlertDialogManager alert = new AlertDialogManager();
    // Progress Dialog
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public final static int POST = 2;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // save button
        btnLogin= (Button) findViewById(R.id.btnLogin);
        loginUser= (EditText) findViewById(R.id.loginUser);
        loginPass= (EditText) findViewById(R.id.loginPassword);
        loginList = new ArrayList<HashMap<String, String>>();

        // save button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new LoginDetails(LoginActivity.this).execute();
            }
        });

        //get the data from sharedPreferences
        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        // returns stored preference value
        // If value is not present return second param value - In this case null
        userShared=pref.getString(TAG_USER, null);// getting String user
        passShared=pref.getString(TAG_PASS, null);// getting String pass

        if (userShared!=null) {
            new LoginDetails(this).execute();
        }

    }

    /**
     * Background Async Task to Get complete product details
     * */
    class LoginDetails extends AsyncTask<String, String, String>  {

        private LoginActivity parent;

        public LoginDetails(LoginActivity parent) {
            this.parent = parent;
        }
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Espere por favor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
             user = loginUser.getText().toString();
             pass = loginPass.getText().toString();

            if (user.equalsIgnoreCase("")){
                user=userShared;
                pass=passShared;
            }

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_USERNAME, user));
                params.add(new BasicNameValuePair(TAG_PASS, pass));

                // sending modified data through http request
                // Notice that update product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_login_details, "POST", params);

                // check json success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);
                    //Start registerActivity
                    if (success == 1) {

                        editor.putString(TAG_USER, user);// Storing string user
                        editor.putString(TAG_PASS, pass);// Storing string pass
                        editor.commit(); // commit changes

                        // successfully updated
                        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(i);
                        finish();
                    } else {

                        //Eliminamos los datos almacenados de otros accesos correctos
                        editor.remove(TAG_USER);
                        editor.remove(TAG_PASS);
                        editor.remove(TAG_USERID);
                        editor.remove(TAG_STAND);
                        editor.remove(TAG_RESOURCE);
                        editor.remove(TAG_STATUS);
                        editor.remove(TAG_ID_INJURED);
                        editor.clear();
                        editor.commit();

                        //No es el usuario o password correcto.
                        // user is not logged in redirect him to Login Activity
                        // user didn't entered username or password
                        // Show alert asking him to enter the details
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                alert.showAlertDialog(LoginActivity.this, "Login error...", "Usuario o contraseña incorrectos", false);
                            }
                        });

                        //Volver a la pantalla login
                        // Releasing wake lock
                        //WakeLocker.release();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
}