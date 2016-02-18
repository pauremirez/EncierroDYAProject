package com.navarra.dya.encierro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.navarra.dya.encierro.CommonUtilities.TAG_MESSAGE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_USERID;
import static com.navarra.dya.encierro.CommonUtilities.sendSMS;



public class ChatActivity extends Activity {

    String stand, userId, resource, status, test, message;
    static TextView txtMessage;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String sms, sms2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();

        Intent i = getIntent();
        stand=i.getStringExtra("stand");
        userId=i.getStringExtra("userId");
        resource=i.getStringExtra("resource");
        status=i.getStringExtra("status");
        test = i.getStringExtra("test");
        message = i.getStringExtra("message");

        txtMessage = (TextView) findViewById(R.id.message);
        Button enviar = (Button) findViewById(R.id.btnSend);
        final EditText txtToSend = (EditText) findViewById(R.id.editText);
        String past_sms=pref.getString(TAG_MESSAGE, null);
        if ((message!=null && !message.equalsIgnoreCase("")) || past_sms!=null) {
            txtMessage.setVisibility(View.VISIBLE);
            if (message==null){
                sms = past_sms;
                txtMessage.setText(sms);
            }
            else {
                if (past_sms != null) {
                    sms = past_sms + "Coordinador: " + message + "\n\r";
                    //sms2 = sms.replaceAll("<br/>", "\n\r");
                }else {
                    sms = "Coordinador: " + message + "\n\r";
                    //sms2 = sms.replaceAll("<br/>", "\\n\\r");
                } txtMessage.setText(sms);
                editor.putString(TAG_MESSAGE, sms);// Storing string stand
                editor.commit();// commit changes
            }
        }
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto= txtToSend.getText().toString();
                if (!texto.equalsIgnoreCase("")) {
                    String past_sms = pref.getString(TAG_MESSAGE, null);
                    if (past_sms != null){
                        sms = past_sms + userId + ": " + texto + "\n\r";
                        //sms2 = sms.replaceAll("<br/>", "\n\r");
                     }else{
                        sms = userId + ": " + texto + "\n\r";// \n\r
                        //sms2 = sms.replaceAll("<br/>", "\n\r");
                    }
                    txtMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText(sms);
                    txtToSend.setText("");
                    editor.putString(TAG_MESSAGE, sms);// Storing string stand
                    editor.commit();// commit changes

                    //Guardar en la BBDD la conversación mantenida --> Enviar al servidor el mensaje del usuario
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair(TAG_MESSAGE, sms));
                    params.add(new BasicNameValuePair(TAG_USERID, userId));
                    JSONParser jsonParser = new JSONParser();

                    JSONObject jsonInsert = jsonParser.makeHttpRequest(sendSMS, "POST", params);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Volver a la pantalla anterior
        Intent i;
        if (resource.equalsIgnoreCase("Transmisiones"))
            i = new Intent(ChatActivity.this, MenuActivity.class);
        else
            i = new Intent(ChatActivity.this, MainActivity.class);
        // sending id to next activity
            i.putExtra(TAG_TYPE_LIST_INJURED, "stand");
            i.putExtra("stand",stand);
            i.putExtra("userId",userId);
            i.putExtra("resource",resource);
            i.putExtra("status",status);
            //i.putExtra("message",message);
            i.putExtra("test",test);
            startActivity(i);
            finish();
    }
}
