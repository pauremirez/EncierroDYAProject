package com.navarra.dya.encierro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.navarra.dya.encierro.CommonUtilities.TAG;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_MESSAGE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PASS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_RESOURCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STATUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERID;
import static com.navarra.dya.encierro.CommonUtilities.broadcastMessage;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class MenuActivity extends Activity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    static TextView lbl_online, lbl_userId;
    static ImageView online;
    Button btnViewInjured;
    Button btnNewInjured;
    ImageButton btnLogOut;
    ImageView vallado;
    LinearLayout llTest;
    String test="false";

    static String userId, stand, resource, status, gpsPosition,typeList, message;

    // Reminder that the onCreate() method is not just called when an app is first opened,
    // but, among other occasions, is called when the device changes orientation.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.transmisions_menu);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Getting userId, location, resource from intent
        Intent i= getIntent();
        userId=i.getStringExtra("userId");
        stand=i.getStringExtra("stand");
        resource=i.getStringExtra("resource");
        status=i.getStringExtra("status");
        gpsPosition=i.getStringExtra("gpsPosition");
        message=i.getStringExtra(TAG_MESSAGE);
        typeList=i.getStringExtra(TAG_TYPE_LIST_INJURED);
        test=i.getStringExtra("test");
        if (test==null)
            test="false";

        // Buttons
        btnViewInjured = (Button) findViewById(R.id.btnViewInjured);
        //btnStandInjured = (Button) findViewById(R.id.btnViewStandInjured);
        btnNewInjured = (Button) findViewById(R.id.btnNewInjured);
        btnLogOut = (ImageButton) findViewById(R.id.exit);
        online = (ImageView) findViewById(R.id.online);
        lbl_online = (TextView) findViewById(R.id.lbl_online);
        lbl_userId = (TextView) findViewById(R.id.lbl_userId);
        vallado = (ImageView) findViewById(R.id.vallado);
        vallado.setImageResource(R.drawable.vallado);
        llTest = (LinearLayout) findViewById(R.id.llTest);
        llTest.setVisibility(View.VISIBLE);

        if (test.equalsIgnoreCase("true")) {
            vallado.setImageResource(R.drawable.vallado_practicas);
        }else{
            vallado.setImageResource(R.drawable.vallado);
        }

        if ( (!userId.equals("") || userId!=null)) {
            lbl_online.setText("REGISTRADO");
            online.setImageResource(R.drawable.greenbutton);
            lbl_userId.setText(userId);
        }else{
            lbl_online.setText("NO REGISTRADO");
            online.setImageResource(R.drawable.red_button);
        }

        // view products click event
            btnViewInjured.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching create injured activity
                    Intent i = new Intent(MenuActivity.this, AllInjuredActivity.class);
                    // sending id to next activity
                    i.putExtra(TAG_TYPE_LIST_INJURED, "all");
                    i.putExtra("userId", userId);
                    i.putExtra("stand", stand);
                    i.putExtra("resource", resource);
                    i.putExtra("status","libre");
                    i.putExtra("gpsPosition",gpsPosition);
                    i.putExtra("test",test);
                    startActivity(i);
                    finish();
                }
            });

            // view products click event
            btnNewInjured.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(MenuActivity.this, NewInjuredGenderActivity.class);
                    i.putExtra(TAG_TYPE_LIST_INJURED, "all");
                    i.putExtra("userId", userId);
                    i.putExtra("stand", stand);
                    i.putExtra("resource", resource);
                    i.putExtra("status","libre");
                    i.putExtra("gpsPosition",gpsPosition);
                    i.putExtra("test",test);
                    startActivity(i);
                    finish();
                }
             });

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
                Intent i = getIntent();
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK );
                finish();
                ServerUtilities.unregister(MenuActivity.this,userId); //Cerrar sesión
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

     /*       // view products click event
            btnStandInjured.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching create injured activity
                    Intent i = new Intent(MenuActivity.this, AllInjuredActivity.class);
                    // sending id to next activity
                    i.putExtra(TAG_TYPE_LIST_INJURED, "stand");
                    i.putExtra("stand",stand);
                    i.putExtra("userId",userId);
                    i.putExtra("resource",resource);
                    i.putExtra("status","libre");
                    i.putExtra("gpsPosition",gpsPosition);
                    i.putExtra("test",test);
                    startActivity(i);
                    finish();
                }
            }); */

        // This is part of our CHEAT.  For this demo, you'll need to
        // capture this registration id so it can be used in our demo web
        // service.
        String regId = "";
        Log.d(TAG, regId);
    }

    // Creamos un menú con tres opciones: Modo prueba o modo encierro y el chat_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    //Menu contextual
    //Modos de funcionamiento: TEST y ENCIERRO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.testMode:
                test = "true";
                vallado.setImageResource(R.drawable.vallado_practicas);
                return true;
            case R.id.normalMode:
                test = "false";
                vallado.setImageResource(R.drawable.vallado);
                return true;
            case R.id.chat:
                Intent i = new Intent(MenuActivity.this, ChatActivity_old.class);
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
        //Do nothing
        Toast.makeText(MenuActivity.this, "Para salir y levantar el dispositivo debe cerrar sesión", Toast.LENGTH_LONG).show();
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
    }

    // NOTE the call to GCMRegistrar.onDestroy()
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
