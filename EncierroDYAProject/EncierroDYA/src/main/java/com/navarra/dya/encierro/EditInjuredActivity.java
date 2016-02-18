package com.navarra.dya.encierro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BLOOD_PRESSURE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_CHEST;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BRUISES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONSCIOUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_DEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_FACE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GLASGOW;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_BACK;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_CHEST;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_HEMORRHAGES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_NOTES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PENETRATING_INJURY_ABDOMEN;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PENETRATING_INJURY_TX;
import static com.navarra.dya.encierro.CommonUtilities.TAG_POLICONTUSION;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_SUCCESS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRACHEAL_INTUBATION;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRANSFER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRIAGE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_BACK;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_CHEST;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.url_delete_injured;
import static com.navarra.dya.encierro.CommonUtilities.url_injured_details;
import static com.navarra.dya.encierro.CommonUtilities.url_update_injured;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_DEFORMITY_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_DEFORMITY_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CUTS;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class EditInjuredActivity extends Activity {

    Button  btnAdvancedOptions;
    Button btnSave, btnDelete, btnCancel;

    String conscious="", goringHead="", goringBack="", goringChest="", goringArms="", goringLegs="", breakHead="", breakChest="", breakArms="", breakLegs="", tce="", hemorrhages="", policontusion="", bruises="", face="", dead="", penetratingInjuryTx="", penetratingInjuryAbdomen="", trachealIntubation="", transfer="", contusionHead="", contusionBack="", contusionChest="", contusionArms="", contusionLegs="", contusionDeformityArms="", contusionDeformityLegs="", cuts="";
    String triage = "white", userId;
  //  String glasgow = "", bloodPresure = "";
    Boolean advanced=false;
    String glasgow="", blood_pressure="", notes="";


 //   String gender, old;
    TextView lblGlasgow, lblBlood_Pressure, blank3, lblNotes;
    EditText editTextGlasgow, editTextTA1, editTextTA2, editNotes;
    CheckBox checkboxTrachealIntubation, checkboxFace, checkboxTransfer;
    RadioButton editrbConscious, editrbUnconscious,editrbDead;

    String resource, gpsPosition, typeList, status, test;
    static String id, stand;
    ImageView imgDead, imgTest;
    AlertDialogManager alert = new AlertDialogManager();

    // Progress Dialog
 //   private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParserEdit = new JSONParser();

//    private String txtSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_injured);


        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // getting details from intent
        Intent i = getIntent();
        id = i.getStringExtra(TAG_ID_INJURED); //Numero de herido
        stand=i.getStringExtra("stand");
        userId=i.getStringExtra("userId");
        resource=i.getStringExtra("resource");
        gpsPosition=i.getStringExtra("gpsPosition");
        status=i.getStringExtra("status");
        typeList = i.getStringExtra("TAG_TYPE_LIST_INJURED");
        test = i.getStringExtra("test");

        // save button
        btnSave = (Button) findViewById(R.id.editbtnSave);
        btnDelete = (Button) findViewById(R.id.editbtnDelete);
        btnCancel = (Button) findViewById(R.id.editbtnCancel);
        btnAdvancedOptions = (Button) findViewById(R.id.btnAdvancedOptions);

        if (resource.equalsIgnoreCase("Transmisiones")){
            btnDelete.setVisibility(View.VISIBLE);
        }else{
            btnDelete.setVisibility(View.GONE);
        }

//        ImageButton btnHome = (ImageButton) findViewById(R.id.edit_home_menu);
        imgDead = (ImageView) findViewById(R.id.imgDead);
//        TextView lblHome = (TextView)findViewById(R.id.lbl_home);
        lblGlasgow = (TextView) findViewById(R.id.lblGlasgow2);
        lblBlood_Pressure = (TextView) findViewById(R.id.lblBlood_Pressure2);
        blank3 = (TextView) findViewById(R.id.blank3);
        editTextGlasgow = (EditText) findViewById(R.id.edit2TextGlasgow);
        editTextTA1 = (EditText) findViewById(R.id.edit2TextTA1);
        editTextTA2 = (EditText) findViewById(R.id.edit2TextTA2);
        checkboxTrachealIntubation = (CheckBox) findViewById(R.id.editcheckboxTrachealIntubation);
        checkboxFace = (CheckBox) findViewById(R.id.editcheckboxFace);
        checkboxTransfer = (CheckBox) findViewById(R.id.editcheckboxTransfer);
        editNotes = (EditText) findViewById(R.id.editTextNotes);
        lblNotes = (TextView) findViewById(R.id.lblNotes2);
        editrbConscious=(RadioButton)findViewById(R.id.editrbConscious);
        editrbUnconscious=(RadioButton)findViewById(R.id.editrbUnconscious);
        editrbDead=(RadioButton)findViewById(R.id.editrbDead);
        imgTest = (ImageView) findViewById(R.id.imgTest5);

        if (test.equalsIgnoreCase("true")){
            imgTest.setVisibility(View.VISIBLE);
        }else{
            imgTest.setVisibility(View.GONE);
        }

        // button click event
        btnAdvancedOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!advanced) {
                    lblBlood_Pressure.setVisibility(View.VISIBLE);
                    lblGlasgow.setVisibility(View.VISIBLE);
                    editTextGlasgow.setVisibility(View.VISIBLE);
                    editTextTA1.setVisibility(View.VISIBLE);
                    editTextTA2.setVisibility(View.VISIBLE);
                    blank3.setVisibility(View.VISIBLE);
                    checkboxFace.setVisibility(View.VISIBLE);
                    checkboxTrachealIntubation.setVisibility(View.VISIBLE);
                    editNotes.setVisibility(View.VISIBLE);
                    lblNotes.setVisibility(View.VISIBLE);

                    advanced = true;
                } else {
                    lblBlood_Pressure.setVisibility(View.GONE);
                    lblGlasgow.setVisibility(View.GONE);
                    editTextGlasgow.setVisibility(View.GONE);
                    editTextTA1.setVisibility(View.GONE);
                    editTextTA2.setVisibility(View.GONE);
                    blank3.setVisibility(View.GONE);
                    checkboxFace.setVisibility(View.GONE);
                    checkboxTrachealIntubation.setVisibility(View.GONE);
                    editNotes.setVisibility(View.GONE);
                    lblNotes.setVisibility(View.GONE);

                    advanced = false;
                }
            }
        });


        // Getting complete product details in background thread
        new GetInjuredDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //checkTriage();
                new SaveInjuredDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteInjured(EditInjuredActivity.this).execute();
            }
        });

        // button click event
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i;

                if (resource.equalsIgnoreCase("Transmisiones")) {
                    i = new Intent(EditInjuredActivity.this, MenuActivity.class);
                }else{
                    if (status.equalsIgnoreCase("cargado"))
                        i = new Intent(EditInjuredActivity.this, CargadoActivity.class);
                    else if (status.equalsIgnoreCase("camino"))
                        i = new Intent(EditInjuredActivity.this, CaminoActivity.class);
                    else if (status.equalsIgnoreCase("hospital"))
                        i = new Intent(EditInjuredActivity.this, HospitalActivity.class);
                    else
                        i = new Intent(EditInjuredActivity.this, MainActivity.class);
                    // sending id to next activity
                    i.putExtra(TAG_ID_INJURED, id);
                }
                i.putExtra(TAG_STAND, stand);
                i.putExtra("userId", userId);
                i.putExtra("resource", resource);
                i.putExtra("gpsPosition", gpsPosition);
                i.putExtra("status", status);
                i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                i.putExtra("test", test);
                startActivity(i);
                finish();
            }
        });

        new GetInjuredDetails().execute();

    }


    public String checkTriage() {
        /*
        0: Negro
        1: Rojo
        2: Naranja
        3: Amarillo
        4: Verde
        5: Azul
        6: blanca
          */
        triage="6"; //Blanco (Sin valorar)
        //Si inconsciente --> rojo

        if (dead.equals("true")){
            triage = "0"; //Negro (muerto)
        }else{
            if (!conscious.equals("true")) {
                triage = "1";//Rojo
            } else { //Está consciente y tiene cornada --> rojo
                if (hemorrhages.equals("true") || tce.equals("true") || penetratingInjuryTx.equals("true") || penetratingInjuryAbdomen.equals("true") || trachealIntubation.equals("true") || goringBack.equals("true") || goringArms.equals("true") || goringLegs.equals("true") || goringHead.equals("true")) {
                    triage = "1";//rojo (grandes hemorragias, TCE, cornada en torax o abdomen, insuficiencia respiratoria)
                } else if (breakArms.equals("true") || breakHead.equals("true") || breakLegs.equals("true")) {
                    triage = "2"; //Naranja (cornada brazos y piernas y fracturas abiertas)
                } else if (contusionDeformityArms.equals("true") || contusionDeformityLegs.equals("true") || contusionHead.equals("true") || contusionChest.equals("true") || contusionBack.equals("true")) {
                    triage = "3"; //Amarillo (Contusiones con deformidad, contusion en cabeza)
                } else if (cuts.equals("true") || (contusionArms.equals("true") && contusionLegs.equals("true"))) {
                    triage = "4"; //verde (cortes, contusiones en piernes y brazos)
                } else if (bruises.equals("true") || contusionArms.equals("true") || contusionLegs.equals("true"))
                    triage = "5"; //azul (moratones, contusiones en piernes o brazos)
            }
        }
        return triage;
    }


    //HEMORRHAGES RadioButton
    public void onHemorrhagesCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxHemorrhages:
                if (checked)
                    hemorrhages = "true";
                else
                    hemorrhages = "false";
                break;
        }
    }

    //PENETRATING INJURY TX RadioButton
    public void onPenetratingInjuryTxCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxPenetratingInjuryTx:
                if (checked)
                    penetratingInjuryTx = "true";
                else
                    penetratingInjuryTx = "false";
                break;
        }
    }


    //PENETRATING INJURY ABDOMEN RadioButton
    public void onPenetratingInjuryAbdomenCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxPenetratingInjuryAbdomen:
                if (checked)
                    penetratingInjuryAbdomen = "true";
                else
                    penetratingInjuryAbdomen = "false";
                break;
        }
    }

    //BRUISES RadioButton
    public void onBruisesCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxBruises:
                if (checked)
                    bruises = "true";
                else
                    bruises = "false";
                break;
        }
    }

    //TRACHEAL INTUBATION RadioButton
    public void onTrachealIntubationCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxTrachealIntubation:
                if (checked)
                    trachealIntubation = "true";
                else
                    trachealIntubation = "false";
                break;
        }
    }

    //FACE RadioButton
    public void onFaceCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxFace:
                if (checked)
                    face = "true";
                else
                    face = "false";
                break;
        }
    }


    //CONCIOUS RadioButton
    public void onConsciousRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editrbConscious:
                if (checked) {
                    conscious = "true";
                    dead = "false";
                }
                break;
        }
        editrbUnconscious.setChecked(false);
        editrbDead.setChecked(false);
    }

    //UNCONCIOUS RadioButton
    public void onUnconsciousRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

       // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editrbUnconscious:
                if (checked)
                    conscious = "false";
                break;
        }
        editrbConscious.setChecked(false);
        editrbDead.setChecked(false);
    }


    //DEAD RadioButton
    public void onDeadRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editrbDead:
                if (checked) {
                    dead = "true";
                    conscious = "false";
                }else {
                    dead = "false";
                }
                break;
        }
        editrbUnconscious.setChecked(false);
        editrbConscious.setChecked(false);
    }

    //GORING CheckedBox
    public void onGoringCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxGoringHead:
                if (checked)
                    goringHead = "true";
                else
                    goringHead = "false";
                break;
            case R.id.editcheckboxGoringBack:
                if (checked)
                    goringBack = "true";
                else
                    goringBack = "false";
                break;
            case R.id.editcheckboxGoringArms:
                if (checked)
                    goringArms = "true";
                else
                    goringArms = "false";
                break;
            case R.id.editcheckboxGoringLegs:
                if (checked)
                    goringLegs = "true";
                else
                    goringLegs = "false";
                break;
        }
    }

    //DEFORMITY RadioButton
    public void onContusionDeformityArmsCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.editcheckboxContusionDeformityArms:
                if (checked)
                    contusionDeformityArms="true";
                else
                    contusionDeformityArms="false";
                break;
        }
    }


    //DEFORMITY RadioButton
    public void onContusionDeformityLegsCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.editcheckboxContusionDeformityLegs:
                if (checked)
                    contusionDeformityLegs="true";
                else
                    contusionDeformityLegs="false";
                break;
        }
    }

    //CUTS RadioButton
    public void onCutsCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.editcheckboxCuts:
                if (checked)
                    cuts="true";
                else
                    cuts="false";
                break;
        }
    }

    //CONTUSIONS CheckedBox
    public void onContusionCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.editcheckboxContusionHead:
                if (checked)
                    contusionHead="true";
                else
                    contusionHead="false";
                break;
            case R.id.editcheckboxContusionBack:
                if (checked)
                    contusionBack="true";
                else
                    contusionBack="false";
                break;
            case R.id.editcheckboxContusionChest:
                if (checked)
                    contusionChest="true";
                else
                    contusionChest="false";
                break;
            case R.id.editcheckboxContusionArms:
                if (checked)
                    contusionArms="true";
                else
                    contusionArms="false";
                break;
            case R.id.editcheckboxContusionLegs:
                if (checked)
                    contusionLegs="true";
                else
                    contusionLegs="false";
                break;
        }
    }

    //BREAK CheckedBox
    public void onBreakCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxBreakHead:
                if (checked)
                    breakHead = "true";
                else
                    breakHead = "false";
                break;
            case R.id.editcheckboxBreakArms:
                if (checked)
                    breakArms = "true";
                else
                    breakArms = "false";
                break;
            case R.id.editcheckboxBreakLegs:
                if (checked)
                    breakLegs = "true";
                else
                    breakLegs = "false";
                break;
        }
    }

    //TRANSFER RadioButton
    public void onTransferCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxTransfer:
                if (checked)
                    transfer="true";
                else
                    transfer="false";
                break;
        }
    }

    //TCE CheckedBox
    public void onTceCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.editcheckboxTce:
                if (checked)
                    tce = "true";
                else
                    tce = "false";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i;

        if (resource.equalsIgnoreCase("Transmisiones")) {
             i = new Intent(EditInjuredActivity.this, AllInjuredActivity.class);
             //i = new Intent(EditInjuredActivity.this, MenuActivity.class);
        }else{
            if (status.equalsIgnoreCase("cargado"))
                i = new Intent(EditInjuredActivity.this, CargadoActivity.class);
            else if (status.equalsIgnoreCase("camino"))
                i = new Intent(EditInjuredActivity.this, CaminoActivity.class);
            else if (status.equalsIgnoreCase("hospital"))
                i = new Intent(EditInjuredActivity.this, HospitalActivity.class);
            else
                i = new Intent(EditInjuredActivity.this, MainActivity.class);
            // sending id to next activity
            i.putExtra(TAG_ID_INJURED, id);
        }


        i.putExtra(TAG_STAND, stand);
        i.putExtra("userId", userId);
        i.putExtra("resource", resource);
        i.putExtra("gpsPosition", gpsPosition);
        i.putExtra("status", status);
        i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
        i.putExtra("test", test);
        startActivity(i);
        finish();
    }



    /**
     * Background Async Task to Get complete product details
     */
    class GetInjuredDetails extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        Boolean bconscious=false, bgoringHead=false, bgoringBack=false, bgoringChest=false, bgoringArms=false, bgoringLegs=false, bbreakHead=false, bbreakChest=false, bbreakArms=false, bbreakLegs=false, btce=false, bhemorrhages=false, bpolicontusion=false, bbruises=false, bface=false, bdead=false, bpenetrating_injury_tx=false, bpenetrating_injury_abdomen=false, btracheal_intubation=false, btransfer=false, bcontusionHead=false, bcontusionBack=false, bcontusionChest=false, bcontusionArms=false, bcontusionLegs=false, bcontusionDeformityArms=false, bcontusionDeformityLegs=false, bcuts=false;
        JSONParser jsonParser = new JSONParser();

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditInjuredActivity.this);
            pDialog.setMessage("Cargando detalles del herido. Espere por favor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id", id));
                        params.add(new BasicNameValuePair("test", test));

                        // getting details by making HTTP request
                        // Note that details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_injured_details, "GET", params);

                        // check your log for json response
                        Log.d("Injured Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received details
                            JSONArray injuredObj = json.getJSONArray(TAG_INJURED); // JSON Array

                            // get first object from JSON Array
                            JSONObject c = injuredObj.getJSONObject(0);

                            //Mostramos los datos actales del herido
                            id = c.getString(TAG_ID_INJURED);

                            bconscious = Boolean.parseBoolean(c.getString(TAG_CONSCIOUS));
                            bgoringHead = Boolean.parseBoolean(c.getString(TAG_GORING_HEAD));
                            bgoringBack = Boolean.parseBoolean(c.getString(TAG_GORING_BACK));
                            bgoringChest = Boolean.parseBoolean(c.getString(TAG_GORING_CHEST));
                            bgoringArms = Boolean.parseBoolean(c.getString(TAG_GORING_ARMS));
                            bgoringLegs = Boolean.parseBoolean(c.getString(TAG_GORING_LEGS));
                            bbreakHead = Boolean.parseBoolean(c.getString(TAG_BREAK_HEAD));
                            bbreakChest = Boolean.parseBoolean(c.getString(TAG_BREAK_CHEST));
                            bbreakArms = Boolean.parseBoolean(c.getString(TAG_BREAK_ARMS));
                            bbreakLegs = Boolean.parseBoolean(c.getString(TAG_BREAK_LEGS));
                            btce = Boolean.parseBoolean(c.getString(TAG_TCE));
                            bhemorrhages = Boolean.parseBoolean(c.getString(TAG_HEMORRHAGES));
                            bpolicontusion = Boolean.parseBoolean(c.getString(TAG_POLICONTUSION));
                            bbruises = Boolean.parseBoolean(c.getString(TAG_BRUISES));
                            bdead = Boolean.parseBoolean(c.getString(TAG_DEAD));
                            bface = Boolean.parseBoolean(c.getString(TAG_FACE));
                            bpenetrating_injury_abdomen = Boolean.parseBoolean(c.getString(TAG_PENETRATING_INJURY_ABDOMEN));
                            bpenetrating_injury_tx = Boolean.parseBoolean(c.getString(TAG_PENETRATING_INJURY_TX));
                            btracheal_intubation = Boolean.parseBoolean(c.getString(TAG_TRACHEAL_INTUBATION));
                            glasgow = c.getString(TAG_GLASGOW);
                            blood_pressure = c.getString(TAG_BLOOD_PRESSURE);
                            notes = c.getString(TAG_NOTES);
                            btransfer = Boolean.parseBoolean(c.getString(TAG_TRANSFER));
                            bcontusionHead = Boolean.parseBoolean(c.getString(TAG_CONTUSION_HEAD));
                            bcontusionBack = Boolean.parseBoolean(c.getString(TAG_CONTUSION_BACK));
                            bcontusionChest = Boolean.parseBoolean(c.getString(TAG_CONTUSION_CHEST));
                            bcontusionArms = Boolean.parseBoolean(c.getString(TAG_CONTUSION_ARMS));
                            bcontusionLegs = Boolean.parseBoolean(c.getString(TAG_CONTUSION_LEGS));
                            bcontusionDeformityArms = Boolean.parseBoolean(c.getString(TAG_CONTUSION_DEFORMITY_ARMS));
                            bcontusionDeformityLegs = Boolean.parseBoolean(c.getString(TAG_CONTUSION_DEFORMITY_LEGS));
                            bcuts = Boolean.parseBoolean(c.getString(TAG_CUTS));

                            //Conscious
                            RadioButton rbConscious = (RadioButton) findViewById(R.id.editrbConscious);
                            RadioButton rbUnconscious = (RadioButton) findViewById(R.id.editrbUnconscious);
                            RadioButton rbDead = (RadioButton) findViewById(R.id.editrbDead);
                            if (bconscious) {
                                rbConscious.setChecked(true);
                                //rbUnconscious.setChecked(false);
                                //rbDead.setChecked(false);
                                conscious="true";
                                dead="false";
                            }else{
                                //rbConscious.setChecked(false);
                                rbUnconscious.setChecked(true);
                                //rbDead.setChecked(false);
                                conscious="false";
                            }
                            //Dead
                            if (bdead) {
                                rbDead.setChecked(true);
                                //rbConscious.setChecked(false);
                                //rbUnconscious.setChecked(false);
                                dead="true";
                                conscious="false";
                            }/*else{
                                rbDead.setChecked(false);
                                rbConscious.setChecked(false);
                                rbUnconscious.setChecked(false);
                                dead="false";
                            }*/
                            //Goring
                            CheckBox rbGoringHead = (CheckBox) findViewById(R.id.editcheckboxGoringHead);
                            if (bgoringHead) {
                                rbGoringHead.setChecked(true);
                                goringHead="true";
                            }else{
                                rbGoringHead.setChecked(false);
                                goringHead="false";
                            }
                            CheckBox rbGoringBack = (CheckBox) findViewById(R.id.editcheckboxGoringBack);
                            if (bgoringBack) {
                                rbGoringBack.setChecked(true);
                                goringBack="true";
                            }else{
                                rbGoringBack.setChecked(false);
                                goringBack="false";
                            }
                            CheckBox rbGoringArms = (CheckBox) findViewById(R.id.editcheckboxGoringArms);
                            if (bgoringArms) {
                                rbGoringArms.setChecked(true);
                                goringArms="true";
                            }else{
                                rbGoringArms.setChecked(false);
                                goringArms="false";
                            }
                            CheckBox rbGoringLegs = (CheckBox) findViewById(R.id.editcheckboxGoringLegs);
                            if (bgoringLegs) {
                                rbGoringLegs.setChecked(true);
                                goringLegs="true";
                            }else{
                                rbGoringLegs.setChecked(false);
                                goringLegs="false";
                            }
                            //Break
                            CheckBox rbBreakHead = (CheckBox) findViewById(R.id.editcheckboxBreakHead);
                            if (bbreakHead) {
                                rbBreakHead.setChecked(true);
                                breakHead="true";
                            }else{
                                rbBreakHead.setChecked(false);
                                breakHead="false";
                            }
                            CheckBox rbBreakArms = (CheckBox) findViewById(R.id.editcheckboxBreakArms);
                            if (bbreakArms) {
                                rbBreakArms.setChecked(true);
                                breakArms="true";
                            }else{
                                rbBreakArms.setChecked(false);
                                breakArms="false";
                            }
                            CheckBox rbBreakLegs = (CheckBox) findViewById(R.id.editcheckboxBreakLegs);
                            if (bbreakLegs) {
                                rbBreakLegs.setChecked(true);
                                breakLegs="true";
                            }else{
                                rbBreakLegs.setChecked(false);
                                breakLegs="false";
                            }
                            //TCE
                            CheckBox rbTce = (CheckBox) findViewById(R.id.editcheckboxTce);
                            if (btce) {
                                rbTce.setChecked(true);
                                tce="true";
                            }else{
                                rbTce.setChecked(false);
                                tce="false";
                            }
                            //Hemorrhges
                            CheckBox rbHemorrhages = (CheckBox) findViewById(R.id.editcheckboxHemorrhages);
                            if (bhemorrhages) {
                                rbHemorrhages.setChecked(true);
                                hemorrhages="true";
                            }else{
                                rbHemorrhages.setChecked(false);
                                hemorrhages="false";
                            }
                            //Bruises
                            CheckBox rbBruises = (CheckBox) findViewById(R.id.editcheckboxBruises);
                            if (bbruises) {
                                rbBruises.setChecked(true);
                                bruises="true";
                            }else{
                                rbBruises.setChecked(false);
                                bruises="false";
                            }
                            //Face
                            CheckBox rbFace = (CheckBox) findViewById(R.id.editcheckboxFace);
                            if (bface) {
                                rbFace.setChecked(true);
                                face="true";
                            }else{
                                rbFace.setChecked(false);
                                face="false";
                            }
                            //Penetrating injury abdomen
                            CheckBox rbPenetratingInjuryAbdomen = (CheckBox) findViewById(R.id.editcheckboxPenetratingInjuryAbdomen);
                            if (bpenetrating_injury_abdomen) {
                                rbPenetratingInjuryAbdomen.setChecked(true);
                                penetratingInjuryAbdomen="true";
                            }else{
                                rbPenetratingInjuryAbdomen.setChecked(false);
                                penetratingInjuryAbdomen="false";
                            }
                            //Penetrating injury tx
                            CheckBox rbPenetratingInjuryTx = (CheckBox) findViewById(R.id.editcheckboxPenetratingInjuryTx);
                            if (bpenetrating_injury_tx) {
                                rbPenetratingInjuryTx.setChecked(true);
                                penetratingInjuryTx="true";
                            }else{
                                rbPenetratingInjuryTx.setChecked(false);
                                penetratingInjuryTx="false";
                            }
                            //Tracheal intubation
                            CheckBox rbTrachealIntubation = (CheckBox) findViewById(R.id.editcheckboxTrachealIntubation);
                            if (btracheal_intubation) {
                                rbTrachealIntubation.setChecked(true);
                                trachealIntubation="true";
                            }else{
                                rbTrachealIntubation.setChecked(false);
                                trachealIntubation="false";
                            }
                            //Glasgow
                            EditText txtGlasgow = (EditText) findViewById(R.id.edit2TextGlasgow);
                            txtGlasgow.setText(glasgow);
                            //Blood pressure
                            if (!blood_pressure.equalsIgnoreCase("") && !blood_pressure.equalsIgnoreCase("-")) {
                                String[] bloodPressure = blood_pressure.split("-");
                                String bTA1 = bloodPressure[0];
                                String bTA2 = bloodPressure[1];
                                EditText txtBloodPressure = (EditText) findViewById(R.id.edit2TextTA1);
                                txtBloodPressure.setText(bTA1);
                                EditText txtBloodPressure2 = (EditText) findViewById(R.id.edit2TextTA2);
                                txtBloodPressure2.setText(bTA2);
                            }
                            //Transfer
                            CheckBox rbTransfer = (CheckBox) findViewById(R.id.editcheckboxTransfer);
                            if (btransfer) {
                                rbTransfer.setChecked(true);
                                transfer="true";
                            }else{
                                rbTransfer.setChecked(false);
                                transfer="false";
                            }
                            //Notes
                            EditText txtNotes = (EditText) findViewById(R.id.editTextNotes);
                            if (!notes.equalsIgnoreCase("")) {
                                txtNotes.setText(notes);
                            }
                            //Contusions
                            CheckBox rbContusionHead = (CheckBox) findViewById(R.id.editcheckboxContusionHead);
                            if (bcontusionHead) {
                                rbContusionHead.setChecked(true);
                                contusionHead="true";
                            }else{
                                rbContusionHead.setChecked(false);
                                contusionHead="false";
                            }
                            CheckBox rbContusionChest = (CheckBox) findViewById(R.id.editcheckboxContusionChest);
                            if (bcontusionChest) {
                                rbContusionChest.setChecked(true);
                                contusionChest="true";
                            }else{
                                rbContusionChest.setChecked(false);
                                contusionChest="false";
                            }
                            CheckBox rbContusionBack = (CheckBox) findViewById(R.id.editcheckboxContusionBack);
                            if (bcontusionBack) {
                                rbContusionBack.setChecked(true);
                                contusionBack="true";
                            }else{
                                rbContusionBack.setChecked(false);
                                contusionBack="false";
                            }
                            CheckBox rbContusionArms = (CheckBox) findViewById(R.id.editcheckboxContusionArms);
                            if (bcontusionArms) {
                                rbContusionArms.setChecked(true);
                                contusionArms="true";
                            }else{
                                rbContusionArms.setChecked(false);
                                contusionArms="false";
                            }
                            CheckBox rbContusionLegs = (CheckBox) findViewById(R.id.editcheckboxContusionLegs);
                            if (bcontusionLegs) {
                                rbContusionLegs.setChecked(true);
                                contusionLegs="true";
                            }else{
                                rbContusionLegs.setChecked(false);
                                contusionLegs="false";
                            }
                            //Contusion Deformity
                            CheckBox rbContusionDeformityArms = (CheckBox) findViewById(R.id.editcheckboxContusionDeformityArms);
                            if (bcontusionDeformityArms) {
                                rbContusionDeformityArms.setChecked(true);
                                contusionDeformityArms="true";
                            }else{
                                rbContusionDeformityArms.setChecked(false);
                                contusionDeformityArms="false";
                            }
                            //Contusion Deformity
                            CheckBox rbContusionDeformityLegs = (CheckBox) findViewById(R.id.editcheckboxContusionDeformityLegs);
                            if (bcontusionDeformityLegs) {
                                rbContusionDeformityLegs.setChecked(true);
                                contusionDeformityLegs="true";
                            }else{
                                rbContusionDeformityLegs.setChecked(false);
                                contusionDeformityLegs="false";
                            }
                            //Contusion Deformity
                            CheckBox rbCuts = (CheckBox) findViewById(R.id.editcheckboxCuts);
                            if (bcuts) {
                                rbCuts.setChecked(true);
                                cuts="true";
                            }else{
                                rbCuts.setChecked(false);
                                cuts="false";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     */
    class SaveInjuredDetails extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditInjuredActivity.this);
            pDialog.setMessage("Guardando datos del herido ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         */
        protected String doInBackground(String... args) {

            RadioButton rbConscious = (RadioButton) findViewById(R.id.editrbConscious);
            RadioButton rbUnconscious = (RadioButton) findViewById(R.id.editrbUnconscious);



            //Conscious
            if (rbConscious.isChecked()) {
                conscious="true";
                dead="false";
            }else{
                conscious="false";
            }
            //Conscious
            if (rbUnconscious.isChecked()) {
                conscious="false";
            }else{
                conscious="true";
            }
            //Dead
            if (editrbDead.isChecked()) {
                dead="true";
                conscious="false";
            }else{
                dead="false";
            }
            //Goring
            CheckBox rbGoringHead = (CheckBox) findViewById(R.id.editcheckboxGoringHead);
            if (rbGoringHead.isChecked()) {
                goringHead="true";
            }else{
                goringHead="false";
            }
            CheckBox rbGoringBack = (CheckBox) findViewById(R.id.editcheckboxGoringBack);
            if (rbGoringBack.isChecked()) {
                goringBack="true";
            }else{
                goringBack="false";
            }
            CheckBox rbGoringArms = (CheckBox) findViewById(R.id.editcheckboxGoringArms);
            if (rbGoringArms.isChecked()) {
                goringArms="true";
            }else{
                goringArms="false";
            }
            CheckBox rbGoringLegs = (CheckBox) findViewById(R.id.editcheckboxGoringLegs);
            if (rbGoringLegs.isChecked()) {
                goringLegs="true";
            }else{
                goringLegs="false";
            }
            //Break
            CheckBox rbBreakHead = (CheckBox) findViewById(R.id.editcheckboxBreakHead);
            if (rbBreakHead.isChecked()) {
                breakHead="true";
            }else{
                breakHead="false";
            }
            CheckBox rbBreakArms = (CheckBox) findViewById(R.id.editcheckboxBreakArms);
            if (rbBreakArms.isChecked()) {
                breakArms="true";
            }else{
                breakArms="false";
            }
            CheckBox rbBreakLegs = (CheckBox) findViewById(R.id.editcheckboxBreakLegs);
            if (rbBreakLegs.isChecked()) {
                breakLegs="true";
            }else{
                breakLegs="false";
            }
            //TCE
            CheckBox rbTce = (CheckBox) findViewById(R.id.editcheckboxTce);
            if (rbTce.isChecked()) {
                tce="true";
            }else{
                tce="false";
            }
            //Hemorrhges
            CheckBox rbHemorrhages = (CheckBox) findViewById(R.id.editcheckboxHemorrhages);
            if (rbHemorrhages.isChecked()) {
                hemorrhages="true";
            }else{
                hemorrhages="false";
            }
            //Bruises
            CheckBox rbBruises = (CheckBox) findViewById(R.id.editcheckboxBruises);
            if (rbBruises.isChecked()) {
                bruises="true";
            }else{
                bruises="false";
            }
            //Face
            CheckBox rbFace = (CheckBox) findViewById(R.id.editcheckboxFace);
            if (rbFace.isChecked()) {
                face="true";
            }else{
                face="false";
            }
            //Penetrating injury abdomen
            CheckBox rbPenetratingInjuryAbdomen = (CheckBox) findViewById(R.id.editcheckboxPenetratingInjuryAbdomen);
            if (rbPenetratingInjuryAbdomen.isChecked()) {
                penetratingInjuryAbdomen="true";
            }else{
                penetratingInjuryAbdomen="false";
            }
            //Penetrating injury tx
            CheckBox rbPenetratingInjuryTx = (CheckBox) findViewById(R.id.editcheckboxPenetratingInjuryTx);
            if (rbPenetratingInjuryTx.isChecked()) {
                penetratingInjuryTx="true";
            }else{
                penetratingInjuryTx="false";
            }
            //Tracheal intubation
            CheckBox rbTrachealIntubation = (CheckBox) findViewById(R.id.editcheckboxTrachealIntubation);
            if (rbTrachealIntubation.isChecked()) {
                trachealIntubation="true";
            }else{
                trachealIntubation="false";
            }
            //Glasgow
            EditText txtGlasgow = (EditText) findViewById(R.id.edit2TextGlasgow);
            glasgow=txtGlasgow.getText().toString();
            //Blood pressure
            EditText txtBloodPressure = (EditText) findViewById(R.id.edit2TextTA1);
            EditText txtBloodPressure2 = (EditText) findViewById(R.id.edit2TextTA2);
            if (!txtBloodPressure.getText().toString().equalsIgnoreCase("") && !txtBloodPressure2.getText().toString().equalsIgnoreCase("")) {
                blood_pressure=txtBloodPressure.getText().toString()+"-"+txtBloodPressure2.getText().toString();
            }else{
                blood_pressure="";
            }
            //Transfer
            CheckBox rbTransfer = (CheckBox) findViewById(R.id.editcheckboxTransfer);
            if (rbTransfer.isChecked()) {
                transfer="true";
            }else{
                transfer="false";
            }
            //Notes
            EditText txtNotes = (EditText) findViewById(R.id.editTextNotes);
            notes=txtNotes.getText().toString();

            // getting updated data from EditTexts
            if (!String.valueOf(editTextTA1.getText()).equals("") && !String.valueOf(editTextTA2.getText()).equals("")) {
                blood_pressure = editTextTA1.getText() + "-" + editTextTA2.getText();
            }else{
                blood_pressure = "";
            }
            glasgow = String.valueOf(editTextGlasgow.getText());

            notes=editNotes.getText().toString();

            //Contusions
            CheckBox rbContusionHead = (CheckBox) findViewById(R.id.editcheckboxContusionHead);
            if (rbContusionHead.isChecked()) {
                contusionHead="true";
            }else{
                contusionHead="false";
            }
            CheckBox rbContusionChest = (CheckBox) findViewById(R.id.editcheckboxContusionChest);
            if (rbContusionChest.isChecked()) {
                contusionChest="true";
            }else{
                contusionChest="false";
            }
            CheckBox rbContusionBack = (CheckBox) findViewById(R.id.editcheckboxContusionBack);
            if (rbContusionBack.isChecked()) {
                contusionBack="true";
            }else{
                contusionBack="false";
            }
            CheckBox rbContusionArms = (CheckBox) findViewById(R.id.editcheckboxContusionArms);
            if (rbContusionArms.isChecked()) {
                contusionArms="true";
            }else{
                contusionArms="false";
            }
            CheckBox rbContusionLegs = (CheckBox) findViewById(R.id.editcheckboxContusionLegs);
            if (rbContusionLegs.isChecked()) {
                contusionLegs="true";
            }else{
                contusionLegs="false";
            }
            //Contusion deformity
            CheckBox rbContusionDeformityArms = (CheckBox) findViewById(R.id.editcheckboxContusionDeformityArms);
            if (rbContusionDeformityArms.isChecked()) {
                contusionDeformityArms="true";
            }else{
                contusionDeformityArms="false";
            }
            //Contusion deformity
            CheckBox rbContusionDeformityLegs = (CheckBox) findViewById(R.id.editcheckboxContusionDeformityLegs);
            if (rbContusionDeformityLegs.isChecked()) {
                contusionDeformityLegs="true";
            }else{
                contusionDeformityLegs="false";
            }
            //Contusion deformity
            CheckBox rbCuts = (CheckBox) findViewById(R.id.editcheckboxCuts);
            if (rbCuts.isChecked()) {
                cuts="true";
            }else{
                cuts="false";
            }

            checkTriage();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID_INJURED, id));
            params.add(new BasicNameValuePair(TAG_CONSCIOUS, conscious));
            params.add(new BasicNameValuePair(TAG_GORING_HEAD, goringHead));
            params.add(new BasicNameValuePair(TAG_GORING_BACK, goringBack));
            params.add(new BasicNameValuePair(TAG_GORING_CHEST, goringChest));
            params.add(new BasicNameValuePair(TAG_GORING_ARMS, goringArms));
            params.add(new BasicNameValuePair(TAG_GORING_LEGS, goringLegs));
            params.add(new BasicNameValuePair(TAG_BREAK_HEAD, breakHead));
            params.add(new BasicNameValuePair(TAG_BREAK_CHEST, breakChest));
            params.add(new BasicNameValuePair(TAG_BREAK_ARMS, breakArms));
            params.add(new BasicNameValuePair(TAG_BREAK_LEGS, breakLegs));
            params.add(new BasicNameValuePair(TAG_TCE, tce));
            params.add(new BasicNameValuePair(TAG_HEMORRHAGES, hemorrhages));
            params.add(new BasicNameValuePair(TAG_POLICONTUSION, policontusion));
            params.add(new BasicNameValuePair(TAG_BRUISES, bruises));
            params.add(new BasicNameValuePair(TAG_FACE, face));
            params.add(new BasicNameValuePair(TAG_DEAD, dead));
            params.add(new BasicNameValuePair(TAG_TRIAGE, triage));
            params.add(new BasicNameValuePair(TAG_PENETRATING_INJURY_TX, penetratingInjuryTx));
            params.add(new BasicNameValuePair(TAG_PENETRATING_INJURY_ABDOMEN, penetratingInjuryAbdomen));
            params.add(new BasicNameValuePair(TAG_TRACHEAL_INTUBATION, trachealIntubation));
            params.add(new BasicNameValuePair(TAG_BLOOD_PRESSURE, blood_pressure));
            params.add(new BasicNameValuePair(TAG_GLASGOW, glasgow));
            params.add(new BasicNameValuePair(TAG_NOTES, notes));
            params.add(new BasicNameValuePair(TAG_TRANSFER, transfer));
            params.add(new BasicNameValuePair(TAG_CONTUSION_HEAD, contusionHead));
            params.add(new BasicNameValuePair(TAG_CONTUSION_BACK, contusionBack));
            params.add(new BasicNameValuePair(TAG_CONTUSION_CHEST, contusionChest));
            params.add(new BasicNameValuePair(TAG_CONTUSION_ARMS, contusionArms));
            params.add(new BasicNameValuePair(TAG_CONTUSION_LEGS, contusionLegs));
            params.add(new BasicNameValuePair(TAG_CONTUSION_DEFORMITY_ARMS, contusionDeformityArms));
            params.add(new BasicNameValuePair(TAG_CONTUSION_DEFORMITY_LEGS, contusionDeformityLegs));
            params.add(new BasicNameValuePair(TAG_CUTS, cuts));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject jsonEdit = jsonParserEdit.makeHttpRequest(url_update_injured, "POST", params);

            // check json success tag
            try {
                int success = jsonEdit.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i;

                    if (resource.equalsIgnoreCase("Transmisiones")) {
                        i = new Intent(EditInjuredActivity.this, MenuActivity.class);
                    }else{
                        if (status.equalsIgnoreCase("urgencia"))
                            i = new Intent(EditInjuredActivity.this, UrgenciaActivity.class);
                        else if (status.equalsIgnoreCase("cargado"))
                            i = new Intent(EditInjuredActivity.this, CargadoActivity.class);
                        else if (status.equalsIgnoreCase("camino"))
                            i = new Intent(EditInjuredActivity.this, CaminoActivity.class);
                        else if (status.equalsIgnoreCase("hospital"))
                            i = new Intent(EditInjuredActivity.this, HospitalActivity.class);
                        else
                            i = new Intent(EditInjuredActivity.this, MainActivity.class);
                        // sending id to next activity
                        i.putExtra(TAG_ID_INJURED, id);
                    }

                    i.putExtra(TAG_STAND, stand);
                    i.putExtra("userId", userId);
                    i.putExtra("resource", resource);
                    i.putExtra("gpsPosition", gpsPosition);
                    i.putExtra("status", status);
                    i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                    i.putExtra("test", test);
                    startActivity(i);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

    /**
     * **************************************************************
     * Background Async Task to Delete Product
     */
    class DeleteInjured extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();

        private EditInjuredActivity parent;

        public DeleteInjured(EditInjuredActivity parent){
            this.parent = parent;
        }

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditInjuredActivity.this);
            pDialog.setMessage("Borrando herido...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_injured, "POST", params);

                // check your log for json response
                Log.d("Borrar heridoo", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i ;

                    if (resource.equalsIgnoreCase("Transmisiones")) {
                        i = new Intent(EditInjuredActivity.this, AllInjuredActivity.class);
                    }else{
                        if (status.equalsIgnoreCase("cargado"))
                            i = new Intent(EditInjuredActivity.this, CargadoActivity.class);
                        else if (status.equalsIgnoreCase("camino"))
                            i = new Intent(EditInjuredActivity.this, CaminoActivity.class);
                        else if (status.equalsIgnoreCase("hospital"))
                            i = new Intent(EditInjuredActivity.this, HospitalActivity.class);
                        else
                            i = new Intent(EditInjuredActivity.this, MainActivity.class);
                        // sending id to next activity
                        i.putExtra(TAG_ID_INJURED, id);
                    }

                    i.putExtra(TAG_STAND, stand);
                    i.putExtra("userId", userId);
                    i.putExtra("resource", resource);
                    i.putExtra("gpsPosition", gpsPosition);
                    i.putExtra("status", status);
                    i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                    i.putExtra("test", test);

                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            alert.showAlertDialogWithoutOK(EditInjuredActivity.this, "Herido borrado", "Eliminado herido ID: "+id, true);
                        }
                    });

                    startActivity(i);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }
}

