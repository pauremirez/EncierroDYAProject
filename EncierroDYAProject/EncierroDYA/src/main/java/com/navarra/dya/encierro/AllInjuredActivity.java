package com.navarra.dya.encierro;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.StrictMode;
import static com.navarra.dya.encierro.CommonUtilities.TAG_AMBULANCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONSCIOUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GENDER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_BACK;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_HEALTH_CENTER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_OLD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_SUCCESS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_HEMORRHAGES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BRUISES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_FACE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRIAGE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_DEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GLASGOW;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BLOOD_PRESSURE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PENETRATING_INJURY_ABDOMEN;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PENETRATING_INJURY_TX;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRACHEAL_INTUBATION;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRANSFER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_NOTES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_BACK;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_CHEST;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.url_all_injured;
import static com.navarra.dya.encierro.CommonUtilities.url_stand_injured;
import static com.navarra.dya.encierro.CommonUtilities.url_one_injured;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_DEFORMITY_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_DEFORMITY_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CUTS;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class AllInjuredActivity extends ListActivity {


    Boolean conscious, goringHead, goringBack, goringArms, goringLegs, breakHead, breakArms, breakLegs, tce, hemorrhages, bruises, face, dead, penetrating_injury_tx, penetrating_injury_abdomen, tracheal_intubation, transfer, contusionHead, contusionBack, contusionChest, contusionArms, contusionLegs, contusionDeformityArms, contusionDeformityLegs, cuts;
    String triage = "white";
    String userId, stand, glasgow, blood_presure, resource, gpsPosition, status, typeList, notes, test;

    static String id;

    // Creating JSON Parser object
    JSONParser jParserStand = new JSONParser();
    JSONObject jsonStand = null;
    ArrayList<HashMap<String, String>> injuredList;

    //JSONArray
    JSONArray injured = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.all_injured);

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        Intent i1 = getIntent();
        id = i1.getStringExtra(TAG_ID_INJURED);
        stand = i1.getStringExtra("stand");
        userId = i1.getStringExtra("userId");
        resource = i1.getStringExtra("resource");
        gpsPosition = i1.getStringExtra("gpsPosition");
        status = i1.getStringExtra("status");
        typeList = i1.getStringExtra(TAG_TYPE_LIST_INJURED);
        test = i1.getStringExtra("test");

        params.add(new BasicNameValuePair("stand", stand));
        params.add(new BasicNameValuePair(TAG_ID_INJURED, id));
        params.add(new BasicNameValuePair("test", test));

        if (typeList.equalsIgnoreCase("all")) {
            jsonStand = jParserStand.makeHttpRequest(url_all_injured, "POST", params);
        } else if (typeList.equalsIgnoreCase("oneInjured")) {
            jsonStand = jParserStand.makeHttpRequest(url_one_injured, "POST", params);
        } else {
            jsonStand = jParserStand.makeHttpRequest(url_stand_injured, "POST", params);
        }

        injuredList = new ArrayList<HashMap<String, String>>();

        // Check your log cat for JSON reponse
        Log.d("All Injured people: ", jsonStand.toString());

        JSONObject c;
        try {
            // Checking for SUCCESS TAG
            int success = jsonStand.getInt(TAG_SUCCESS);

            if (success == 1) {
                // injured people found
                // Getting Array of Injured people
                injured = jsonStand.getJSONArray(TAG_INJURED);
                if (injured != null) {
                    // looping through All Products
                    for (int i = 0; i < injured.length(); i++) {
                        c = injured.getJSONObject(i);

                        // Storing each json item in variable
                        id = c.getString(TAG_ID_INJURED);
                        String gender = c.getString(TAG_GENDER);
                        String old = c.getString(TAG_OLD);
                        String stand = c.getString(TAG_STAND);
                        String ambulance = c.getString(TAG_AMBULANCE);
                        String health_center = c.getString(TAG_HEALTH_CENTER);

                        conscious = Boolean.parseBoolean(c.getString(TAG_CONSCIOUS));
                        goringHead = Boolean.parseBoolean(c.getString(TAG_GORING_HEAD));
                        goringBack = Boolean.parseBoolean(c.getString(TAG_GORING_BACK));
                        goringArms = Boolean.parseBoolean(c.getString(TAG_GORING_ARMS));
                        goringLegs = Boolean.parseBoolean(c.getString(TAG_GORING_LEGS));
                        breakHead = Boolean.parseBoolean(c.getString(TAG_BREAK_HEAD));
                        breakArms = Boolean.parseBoolean(c.getString(TAG_BREAK_ARMS));
                        breakLegs = Boolean.parseBoolean(c.getString(TAG_BREAK_LEGS));
                        tce = Boolean.parseBoolean(c.getString(TAG_TCE));
                        hemorrhages = Boolean.parseBoolean(c.getString(TAG_HEMORRHAGES));
                        bruises = Boolean.parseBoolean(c.getString(TAG_BRUISES));
                        dead = Boolean.parseBoolean(c.getString(TAG_DEAD));
                        face = Boolean.parseBoolean(c.getString(TAG_FACE));
                        transfer = Boolean.parseBoolean(c.getString(TAG_TRANSFER));

                        penetrating_injury_abdomen = Boolean.parseBoolean(c.getString(TAG_PENETRATING_INJURY_ABDOMEN));
                        penetrating_injury_tx = Boolean.parseBoolean(c.getString(TAG_PENETRATING_INJURY_TX));
                        tracheal_intubation = Boolean.parseBoolean(c.getString(TAG_TRACHEAL_INTUBATION));
                        glasgow = c.getString(TAG_GLASGOW);
                        blood_presure = c.getString(TAG_BLOOD_PRESSURE);
                        notes = c.getString(TAG_NOTES);
                        contusionHead = Boolean.parseBoolean(c.getString(TAG_CONTUSION_HEAD));
                        contusionBack = Boolean.parseBoolean(c.getString(TAG_CONTUSION_BACK));
                        contusionChest = Boolean.parseBoolean(c.getString(TAG_CONTUSION_CHEST));
                        contusionArms = Boolean.parseBoolean(c.getString(TAG_CONTUSION_ARMS));
                        contusionLegs = Boolean.parseBoolean(c.getString(TAG_CONTUSION_LEGS));
                        contusionDeformityArms = Boolean.parseBoolean(c.getString(TAG_CONTUSION_DEFORMITY_ARMS));
                        contusionDeformityLegs = Boolean.parseBoolean(c.getString(TAG_CONTUSION_DEFORMITY_LEGS));
                        cuts = Boolean.parseBoolean(c.getString(TAG_CUTS));


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID_INJURED, id);
                        map.put(TAG_GENDER, gender);
                        map.put(TAG_OLD, old);
                        map.put(TAG_STAND, stand);
                        map.put(TAG_AMBULANCE, ambulance);
                        map.put(TAG_HEALTH_CENTER, health_center);

                        if (conscious) {
                            map.put(TAG_CONSCIOUS, "CONSCIENTE");
                        } else {
                            map.put(TAG_CONSCIOUS, "INCONSCIENTE");
                        }
                        if (goringHead) {
                            map.put(TAG_GORING_HEAD, "Cornada en cabeza/cuello");
                        } else {
                            map.put(TAG_GORING_HEAD, "");
                        }
                        if (goringBack) {
                            map.put(TAG_GORING_BACK, "Cornada en espalda");
                        } else {
                            map.put(TAG_GORING_BACK, "");
                        }
                        if (goringArms) {
                            map.put(TAG_GORING_ARMS, "Cornada en brazos");
                        } else {
                            map.put(TAG_GORING_ARMS, "");
                        }
                        if (goringLegs) {
                            map.put(TAG_GORING_LEGS, "Cornada en piernas");
                        } else {
                            map.put(TAG_GORING_LEGS, "");
                        }
                        if (breakHead) {
                            map.put(TAG_BREAK_HEAD, "Fractura craneal");
                        } else {
                            map.put(TAG_BREAK_HEAD, "");
                        }
                        if (breakArms) {
                            map.put(TAG_BREAK_ARMS, "Fractura en brazos");
                        } else {
                            map.put(TAG_BREAK_ARMS, "");
                        }
                        if (breakLegs) {
                            map.put(TAG_BREAK_LEGS, "Fractura en piernas");
                        } else {
                            map.put(TAG_BREAK_LEGS, "");
                        }
                        if (tce) {
                            map.put(TAG_TCE, "TCE");
                        } else {
                            map.put(TAG_TCE, "");
                        }
                        if (hemorrhages) {
                            map.put(TAG_HEMORRHAGES, "Sangrado masivo");
                        } else {
                            map.put(TAG_HEMORRHAGES, "");
                        }
                        if (bruises) {
                            map.put(TAG_BRUISES, "Magulladuras");
                        } else {
                            map.put(TAG_BRUISES, "");
                        }
                        if (face) {
                            map.put(TAG_FACE, "Golpe en cara");
                        } else {
                            map.put(TAG_FACE, "");
                        }
                        if (!glasgow.equals("")) {
                            map.put(TAG_GLASGOW, glasgow);
                        } else {
                            map.put(TAG_GLASGOW, "");
                        }
                        if (!blood_presure.equals("")) {
                            map.put(TAG_BLOOD_PRESSURE, blood_presure);
                        } else {
                            map.put(TAG_BLOOD_PRESSURE, "");
                        }
                        if (penetrating_injury_tx) {
                            map.put(TAG_PENETRATING_INJURY_TX, "Cornada en tórax");
                        } else {
                            map.put(TAG_PENETRATING_INJURY_TX, "");
                        }
                        if (penetrating_injury_abdomen) {
                            map.put(TAG_PENETRATING_INJURY_ABDOMEN, "Cornada en abdomen");
                        } else {
                            map.put(TAG_PENETRATING_INJURY_ABDOMEN, "");
                        }
                        if (tracheal_intubation) {
                            map.put(TAG_TRACHEAL_INTUBATION, "Insuficiencia respiratoria");
                        } else {
                            map.put(TAG_TRACHEAL_INTUBATION, "");
                        }
                        if (transfer) {
                            map.put(TAG_TRANSFER, "TRASLADO HOSPITAL");
                        } else {
                            map.put(TAG_TRANSFER, "");
                        }
                        if (notes.equals("")) {
                            map.put(TAG_NOTES, notes);
                        } else {
                            map.put(TAG_NOTES, "");
                        }
                        if (contusionHead) {
                            map.put(TAG_CONTUSION_HEAD, "Contusión en cabeza");
                        } else {
                            map.put(TAG_CONTUSION_HEAD, "");
                        }
                        if (contusionChest) {
                            map.put(TAG_CONTUSION_CHEST, "Contusión en tórax/abdomen"); //
                        } else {
                            map.put(TAG_CONTUSION_CHEST, "");
                        }
                        if (contusionBack) {
                            map.put(TAG_CONTUSION_BACK, "Contusión en espalda");
                        } else {
                            map.put(TAG_CONTUSION_BACK, "");
                        }
                        if (contusionArms) {
                            map.put(TAG_CONTUSION_ARMS, "Contusión en brazos");
                        } else {
                            map.put(TAG_CONTUSION_ARMS, "");
                        }
                        if (contusionLegs) {
                            map.put(TAG_CONTUSION_LEGS, "Contusión en piernas");
                        } else {
                            map.put(TAG_CONTUSION_LEGS, "");
                        }
                        if (contusionDeformityArms) {
                            map.put(TAG_CONTUSION_DEFORMITY_ARMS, "Contusión con deformidad en brazos");
                        } else {
                            map.put(TAG_CONTUSION_DEFORMITY_ARMS, "");
                        }
                        if (contusionDeformityLegs) {
                            map.put(TAG_CONTUSION_DEFORMITY_LEGS, "Contusión con deformidad en piernas");
                        } else {
                            map.put(TAG_CONTUSION_DEFORMITY_LEGS, "");
                        }
                        if (cuts) {
                            map.put(TAG_CUTS, "Pequeños cortes (necesaria sutura)");
                        } else {
                            map.put(TAG_CUTS, "");
                        }

                        checkTriage();
                        map.put(TAG_TRIAGE, triage);

                        // adding HashList to ArrayList
                        injuredList.add(map);
                    }
                } else { //No hay heridos
                    //Mostrar una notificacion diciendo que no hay heridos.
                    //Al acpetar la notificacion, volver al menu
                    Toast.makeText(AllInjuredActivity.this, "¡Perfecto!. No hay heridos", Toast.LENGTH_LONG).show();
                    Intent inte = new Intent(getApplicationContext(), MenuActivity.class);
                    // Closing all previous activities
                    inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    inte.putExtra(TAG_STAND, stand);
                    inte.putExtra("userId", userId);
                    inte.putExtra("resource", resource);
                    inte.putExtra("gpsPosition", gpsPosition);
                    inte.putExtra("status", status);
                    inte.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                    inte.putExtra("test", test);
                    startActivity(inte);
                    finish();
                }
            } else {
                // no injured people found
                Toast.makeText(AllInjuredActivity.this, "No hay heridos. ¿Deseas crear un nuevo herido?", Toast.LENGTH_LONG).show();
                // Launch Add New Activity
                Intent inte = new Intent(getApplicationContext(), NewInjuredGenderActivity.class);
                // Closing all previous activities
                inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inte.putExtra(TAG_STAND, stand);
                inte.putExtra("userId", userId);
                inte.putExtra("resource", resource);
                inte.putExtra("gpsPosition", gpsPosition);
                inte.putExtra("status", status);
                inte.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                inte.putExtra("test", test);
                startActivity(inte);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView imgTest = (ImageView) findViewById(R.id.imgTest4);

        if (test.equalsIgnoreCase("true")) {
            imgTest.setVisibility(View.VISIBLE);
        } else {
            imgTest.setVisibility(View.GONE);
        }

        ListAdapter adapter = new SpecialAdapter(
                AllInjuredActivity.this, injuredList,
                R.layout.list_injured, new String[]{TAG_TRIAGE, TAG_ID_INJURED,
                TAG_GENDER, TAG_OLD, TAG_AMBULANCE, TAG_STAND, TAG_HEALTH_CENTER, TAG_CONSCIOUS, TAG_GORING_HEAD, TAG_GORING_BACK, TAG_GORING_ARMS, TAG_GORING_LEGS, TAG_BREAK_HEAD, TAG_BREAK_ARMS, TAG_BREAK_LEGS, TAG_TCE, TAG_HEMORRHAGES, TAG_BRUISES, TAG_FACE, TAG_GLASGOW, TAG_BLOOD_PRESSURE, TAG_PENETRATING_INJURY_ABDOMEN, TAG_PENETRATING_INJURY_TX, TAG_TRACHEAL_INTUBATION, TAG_NOTES, TAG_CONTUSION_DEFORMITY_ARMS, TAG_CONTUSION_DEFORMITY_LEGS, TAG_CUTS, TAG_CONTUSION_HEAD, TAG_CONTUSION_BACK, TAG_CONTUSION_CHEST, TAG_CONTUSION_ARMS, TAG_CONTUSION_LEGS},
                new int[]{R.id.imgTriage, R.id.iid, R.id.gender, R.id.old, R.id.ambulance, R.id.stand, R.id.health_center, R.id.txtConscious, R.id.txtGoringHead, R.id.txtGoringBack, R.id.txtGoringArms, R.id.txtGoringLegs, R.id.txtBreakHead, R.id.txtBreakArms, R.id.txtBreakLegs, R.id.txtTce, R.id.txtHemorrhages, R.id.txtBruises, R.id.txtFace, R.id.txtGlasgow, R.id.txtBloodPresure, R.id.txtPenetratingInjuryTx, R.id.txtPenetratingInjuryAbdomen, R.id.txtTrachealIntubation, R.id.txtNotes, R.id.txtContusionDeformityArms, R.id.txtContusionDeformityLegs, R.id.txtCuts, R.id.txtContusionHead, R.id.txtContusionBack, R.id.txtContusionChest, R.id.txtContusionArms, R.id.txtContusionLegs}
        );
        // updating listview
        setListAdapter(adapter);

        // Get listview
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String iid = ((TextView) view.findViewById(R.id.iid)).getText().toString();
                //String stand=((TextView) view.findViewById(R.id.stand)).getText().toString();
                Intent in = new Intent(AllInjuredActivity.this, EditInjuredActivity.class);
                // sending id to next activity
                in.putExtra(TAG_ID_INJURED, iid);
                in.putExtra(TAG_STAND, stand);
                in.putExtra("userId", userId);
                in.putExtra("resource", resource);
                in.putExtra("gpsPosition", gpsPosition);
                in.putExtra("status", status);
                in.putExtra(TAG_TYPE_LIST_INJURED, typeList);
                in.putExtra("test", test);

                startActivityForResult(in, 100);
                finish();
            }
        });
    }

    /**
     * Function on back Pressed
     * */
    @Override
    public void onBackPressed() {
        Intent i;
        if (resource.equalsIgnoreCase("Transmisiones")) {
            i = new Intent(AllInjuredActivity.this, MenuActivity.class);

        } else {
            if (status.equalsIgnoreCase("cargado"))
                i = new Intent(AllInjuredActivity.this, CargadoActivity.class);
            else if (status.equalsIgnoreCase("camino"))
                i = new Intent(AllInjuredActivity.this, CaminoActivity.class);
            else if (status.equalsIgnoreCase("hospital"))
                i = new Intent(AllInjuredActivity.this, HospitalActivity.class);
            else if (status.equalsIgnoreCase("urgencia"))
                i = new Intent(AllInjuredActivity.this, UrgenciaActivity.class);
            else
                i = new Intent(AllInjuredActivity.this, MainActivity.class);
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
     * Function to display the triage
     * @return triage - value of the triage
     * */
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
        triage = "6"; //Blanco (Sin valorar)
        //Si inconsciente --> rojo
        if (dead) {
            triage = "0"; //Negro (muerto)
        } else {
            if (!conscious) {
                triage = "1";//Rojo
            } else { //Está consciente y tiene cornada --> rojo
                if (hemorrhages || tce || penetrating_injury_tx || penetrating_injury_abdomen || tracheal_intubation || goringBack || goringArms || goringLegs || goringHead) {
                    triage = "1";//rojo (grandes hemorragias, TCE, cornada en torax o abdomen, insuficiencia respiratoria)
                } else if (breakArms || breakHead || breakLegs) {
                    triage = "2"; //Naranja (cornada brazos y piernas y fracturas abiertas)
                } else if (contusionDeformityArms || contusionDeformityLegs || contusionHead || contusionChest || contusionBack) {
                    triage = "3"; //Amarillo (Contusiones con deformidad, contusion en cabeza)
                } else if (cuts || (contusionArms && contusionLegs)) {
                    triage = "4"; //verde (cortes, contusiones en piernes y brazos)
                } else if (bruises || contusionArms || contusionLegs)
                    triage = "5"; //azul (moratones, contusiones en piernes o brazos)
            }
        }
        return triage;
    }
}