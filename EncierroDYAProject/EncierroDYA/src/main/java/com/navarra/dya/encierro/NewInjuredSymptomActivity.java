package com.navarra.dya.encierro;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.url_new_injured;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID;
import static com.navarra.dya.encierro.CommonUtilities.TAG_SUCCESS;


/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class NewInjuredSymptomActivity extends Activity {

    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();

    String conscious="true", transfer="false";
    String goringHead="false",goringBack="false",goringChest="false",goringArms="false",goringLegs="false";
    String breakHead="false",breakChest="false",breakArms="false",breakLegs="false";
    String contusionHead="false",contusionBack="false",contusionChest="false",contusionArms="false",contusionLegs="false", contusionDeformityArms="false", contusionDeformityLegs="false", cuts="false";
    String ambulance="-",health_center="-", triage="4"; //blanco
    String tce="false", hemorrhages="false", policontusion="false", bruises="false", face="false";
    String penetratingInjuryTx="false",penetratingInjuryAbdomen="false",trachealIntubation="false";
    String glasgow="", bloodPressure="", notes="";

    String gender, old, userId, dead="false", stand,resource, typeList, gpsPosition, status, test;
    TextView lblGlasgow,lblBlood_Presure,blank3, lblNotes;
    EditText editTextGlasgow,editTextTA1,editTextTA2, textNotes;
    CheckBox checkboxTrachealIntubation,checkboxFace, checkboxTransfer;
    Boolean advanced=false;
    RadioButton rbConscious,rbUnconscious, rbDead;
    ImageView imgDead, imgTest;
    AlertDialogManager alert = new AlertDialogManager();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.add_injured);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button btnNewInjured = (Button) findViewById(R.id.btnNewInjured);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnAdvancedOptions = (Button) findViewById(R.id.btnAdvancedOptions);
        lblGlasgow = (TextView) findViewById(R.id.lblGlasgow);
        lblBlood_Presure = (TextView) findViewById(R.id.lblBlood_Presure);
        blank3 = (TextView) findViewById(R.id.blank3);
        editTextGlasgow= (EditText)findViewById(R.id.editTextGlasgow);
        editTextTA1=(EditText)findViewById(R.id.editTextTA1);
        editTextTA2=(EditText)findViewById(R.id.editTextTA2);
        checkboxTrachealIntubation=(CheckBox)findViewById(R.id.checkboxTrachealIntubation);
        checkboxFace=(CheckBox)findViewById(R.id.checkboxFace);
        rbConscious=(RadioButton)findViewById(R.id.rbConscious);
        rbUnconscious=(RadioButton)findViewById(R.id.rbUnconscious);
        rbDead=(RadioButton)findViewById(R.id.rbDead);
        lblNotes = (TextView) findViewById(R.id.lblNotes);
        textNotes=(EditText)findViewById(R.id.textNotes);
        checkboxTransfer=(CheckBox)findViewById(R.id.checkboxTransfer);
        imgDead = (ImageView) findViewById(R.id.imgDead);
        imgTest = (ImageView) findViewById(R.id.imgTest3);

        Intent i1= getIntent();
        stand=i1.getStringExtra("stand");
        userId=i1.getStringExtra("userId");
        gender=i1.getStringExtra("gender");
        old=i1.getStringExtra("old");
        resource=i1.getStringExtra("resource");
        gpsPosition=i1.getStringExtra("gpsPosition");
        status=i1.getStringExtra("status");
        typeList = i1.getStringExtra(TAG_TYPE_LIST_INJURED);
        test=i1.getStringExtra("test");

        if (test.equalsIgnoreCase("true"))
            imgTest.setVisibility(View.VISIBLE);
        else
            imgTest.setVisibility(View.GONE);

       // button click event
        btnNewInjured.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                checkTriage();
                new NewInjured(NewInjuredSymptomActivity.this).execute();
            }
        });

        // button click event
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewInjuredSymptomActivity.this, MenuActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("stand", stand);
                i.putExtra("resource", resource);
                i.putExtra("status", status);
                i.putExtra("gpsPosition", gpsPosition);
                i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                i.putExtra("test",test);
                startActivity(i);
                finish();
            }
        });

        // button click event
        btnAdvancedOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!advanced){
                    lblBlood_Presure.setVisibility(View.VISIBLE);
                    lblGlasgow.setVisibility(View.VISIBLE);
                    editTextGlasgow.setVisibility(View.VISIBLE);
                    editTextTA1.setVisibility(View.VISIBLE);
                    editTextTA2.setVisibility(View.VISIBLE);
                    blank3.setVisibility(View.VISIBLE);
                    checkboxFace.setVisibility(View.VISIBLE);
                    checkboxTrachealIntubation.setVisibility(View.VISIBLE);
                    textNotes.setVisibility(View.VISIBLE);
                    lblNotes.setVisibility(View.VISIBLE);
                    advanced=true;
                }else{
                    lblBlood_Presure.setVisibility(View.GONE);
                    lblGlasgow.setVisibility(View.GONE);
                    editTextGlasgow.setVisibility(View.GONE);
                    editTextTA1.setVisibility(View.GONE);
                    editTextTA2.setVisibility(View.GONE);
                    blank3.setVisibility(View.GONE);
                    checkboxFace.setVisibility(View.GONE);
                    checkboxTrachealIntubation.setVisibility(View.GONE);
                    textNotes.setVisibility(View.GONE);
                    lblNotes.setVisibility(View.GONE);
                    advanced=false;
                }
            }
        });

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

    //TCE RadioButton
    public void onTceCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxTce:
                if (checked)
                    tce="true";
                else
                    tce="false";
                break;
        }
    }

    //DEFORMITY RadioButton
    public void onContusionDeformityArmsCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxContusionDeformityArms:
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
            case R.id.checkboxContusionDeformityLegs:
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
            case R.id.checkboxCuts:
                if (checked)
                    cuts="true";
                else
                    cuts="false";
                break;
        }
    }

    //HEMORRHAGES RadioButton
    public void onHemorrhagesCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxHemorrhages:
                if (checked)
                    hemorrhages="true";
                else
                    hemorrhages="false";
                break;
        }
    }

    //PENETRATING INJURY TX RadioButton
    public void onPenetratingInjuryTxCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxPenetratingInjuryTx:
                if (checked)
                    penetratingInjuryTx="true";
                else
                    penetratingInjuryTx="false";
                break;
        }
    }

    //PENETRATING INJURY ABDOMEN RadioButton
    public void onPenetratingInjuryAbdomenCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxPenetratingInjuryAbdomen:
                if (checked)
                    penetratingInjuryAbdomen="true";
                else
                    penetratingInjuryAbdomen="false";
                break;
        }
    }

    //BRUISES RadioButton
    public void onBruisesCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxBruises:
                if (checked)
                    bruises="true";
                else
                    bruises="false";
                break;
        }
    }

    //TRACHEAL INTUBATION RadioButton
    public void onTrachealIntubationCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxTrachealIntubation:
                if (checked)
                    trachealIntubation="true";
                else
                    trachealIntubation="false";
                break;
        }
    }

    //FACE RadioButton
    public void onFaceCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxFace:
                if (checked)
                    face="true";
                else
                    face="false";
                break;
        }
    }

    //CONSCIOUS RadioButton
    public void onConsciousRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.rbConscious:
                if (checked) {
                    conscious = "true";
                    dead = "false";
                }
                break;
        }
        rbUnconscious.setChecked(false);
        rbDead.setChecked(false);
    }

    //UNCONSCIOUS RadioButton
    public void onUnconsciousRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.rbUnconscious:
                if (checked)
                    conscious = "false";
                break;
        }
        rbConscious.setChecked(false);
        rbDead.setChecked(false);
    }

    //DEAD RadioButton
    public void onDeadRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.rbDead:
                if (checked) {
                    dead = "true";
                    conscious = "false";
                }else {
                    dead = "false";
                }
                break;
        }
        rbUnconscious.setChecked(false);
        rbConscious.setChecked(false);
    }

    //GORING CheckedBox
    public void onGoringCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxGoringHead:
                if (checked)
                    goringHead="true";
                else
                    goringHead="false";
                break;
            case R.id.checkboxGoringBack:
                if (checked)
                    goringBack="true";
                else
                    goringBack="false";
                break;
            case R.id.checkboxGoringArms:
                if (checked)
                    goringArms="true";
                else
                    goringArms="false";
                break;
            case R.id.checkboxGoringLegs:
                if (checked)
                    goringLegs="true";
                else
                    goringLegs="false";
                break;
        }
    }

    //BREAK CheckedBox
    public void onBreakCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxBreakHead:
                if (checked)
                    breakHead="true";
                else
                    breakHead="false";
                break;
            case R.id.checkboxBreakArms:
                if (checked)
                    breakArms="true";
                else
                    breakArms="false";
                break;
            case R.id.checkboxBreakLegs:
                if (checked)
                    breakLegs="true";
                else
                    breakLegs="false";
                break;
        }
    }

    //CONTUSIONS CheckedBox
    public void onContusionCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxContusionHead:
                if (checked)
                    contusionHead="true";
                else
                    contusionHead="false";
                break;
            case R.id.checkboxContusionBack:
                if (checked)
                    contusionBack="true";
                else
                    contusionBack="false";
                break;
            case R.id.checkboxContusionChest:
                if (checked)
                    contusionChest="true";
                else
                    contusionChest="false";
                break;
            case R.id.checkboxContusionArms:
                if (checked)
                    contusionArms="true";
                else
                    contusionArms="false";
                break;
            case R.id.checkboxContusionLegs:
                if (checked)
                    contusionLegs="true";
                else
                    contusionLegs="false";
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NewInjuredSymptomActivity.this, NewInjuredOldActivity.class);
        i.putExtra("userId",userId);
        i.putExtra("gender", gender);
        i.putExtra("stand", stand);
        i.putExtra("resource", resource);
        i.putExtra("status", status);
        i.putExtra("gpsPosition", gpsPosition);
        i.putExtra(TAG_TYPE_LIST_INJURED, typeList);
        i.putExtra("test",test);
        startActivity(i);
        finish();
    }


    /**
     * Background Async Task to Create new product
     * */
    class NewInjured extends AsyncTask<String, String, String> {

    String id_injured;

    private NewInjuredSymptomActivity parent;

    public NewInjured(NewInjuredSymptomActivity parent){
        this.parent = parent;
    }
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewInjuredSymptomActivity.this);
            pDialog.setMessage("Creando nuevo herido..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating injured
         * */
        protected String doInBackground(String... args) {

            //Valores de glasgow y de presion arterial
            glasgow=editTextGlasgow.getText().toString();
            if (!editTextTA1.getText().toString().equalsIgnoreCase("") && !editTextTA2.getText().toString().equalsIgnoreCase(""))
                bloodPressure=editTextTA1.getText().toString() + "-" + editTextTA2.getText().toString();
            else
                bloodPressure="";
            //Notas
            notes=textNotes.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("test", test));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("old", old));
            params.add(new BasicNameValuePair("conscious", conscious));
            params.add(new BasicNameValuePair("goringHead", goringHead));
            params.add(new BasicNameValuePair("goringChest", goringChest));
            params.add(new BasicNameValuePair("goringBack", goringBack));
            params.add(new BasicNameValuePair("goringArms", goringArms));
            params.add(new BasicNameValuePair("goringLegs", goringLegs));
            params.add(new BasicNameValuePair("breakHead", breakHead));
            params.add(new BasicNameValuePair("breakChest", breakChest));
            params.add(new BasicNameValuePair("breakArms", breakArms));
            params.add(new BasicNameValuePair("breakLegs", breakLegs));
            params.add(new BasicNameValuePair("penetrating_injury_tx", penetratingInjuryTx));
            params.add(new BasicNameValuePair("penetrating_injury_abdomen", penetratingInjuryAbdomen));
            params.add(new BasicNameValuePair("ambulance", ambulance));
            params.add(new BasicNameValuePair("health_center", health_center));
            params.add(new BasicNameValuePair("tce", tce));
            params.add(new BasicNameValuePair("triage", triage));
            params.add(new BasicNameValuePair("hemorrhages", hemorrhages));
            params.add(new BasicNameValuePair("policontusion", policontusion));
            params.add(new BasicNameValuePair("bruises", bruises));
            params.add(new BasicNameValuePair("face", face));
            params.add(new BasicNameValuePair("tracheal_intubation", trachealIntubation));
            params.add(new BasicNameValuePair("blood_presure", bloodPressure));
            params.add(new BasicNameValuePair("glasgow", glasgow));
            params.add(new BasicNameValuePair("dead", dead));
            params.add(new BasicNameValuePair("userId", MenuActivity.userId));
            params.add(new BasicNameValuePair("transfer", transfer));
            params.add(new BasicNameValuePair("notes", notes));
            params.add(new BasicNameValuePair("contusionHead", contusionHead));
            params.add(new BasicNameValuePair("contusionChest", contusionChest));
            params.add(new BasicNameValuePair("contusionBack", contusionBack));
            params.add(new BasicNameValuePair("contusionArms", contusionArms));
            params.add(new BasicNameValuePair("contusionLegs", contusionLegs));
            params.add(new BasicNameValuePair("contusionDeformityArms", contusionDeformityArms));
            params.add(new BasicNameValuePair("contusionDeformityLegs", contusionDeformityLegs));
            params.add(new BasicNameValuePair("cuts", cuts));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json4 = jsonParser.makeHttpRequest(url_new_injured, "POST", params);

            try {
                    //Log.d("Create Response-New injured", json4.toString());

                    int success4 = json4.getInt(TAG_SUCCESS);
                    if (success4 == 1){

                        id_injured = json4.getString(TAG_ID);

                        Intent i = new Intent(NewInjuredSymptomActivity.this, MenuActivity.class);
                        i.putExtra("userId",userId);
                        i.putExtra("stand",stand);
                        i.putExtra("resource",resource);
                        i.putExtra("status",status);
                        i.putExtra("gpsPosition",gpsPosition);
                        i.putExtra(TAG_TYPE_LIST_INJURED,typeList);
                        i.putExtra("test",test);

                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                alert.showAlertDialogWithoutOK(NewInjuredSymptomActivity.this, "Nuevo herido", "Creado herido ID: "+id_injured, true);
                            }
                        });

                        startActivity(i);
                        // closing this screen
                        finish();

                    }else{
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                alert.showAlertDialogWithoutOK(NewInjuredSymptomActivity.this, "ERROR", "Herido no creado", false);
                            }
                        });
                    }

            } catch (JSONException e2) {
                    e2.printStackTrace();
                    pDialog = new ProgressDialog(NewInjuredSymptomActivity.this);
                    pDialog.setMessage("Error");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}
