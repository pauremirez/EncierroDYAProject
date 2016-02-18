package com.navarra.dya.encierro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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




public class ChatActivity_old extends Activity {

    String stand, userId, resource, status, test, message;
    //static TextView txtMessage;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String sms;
    //TextView singleMessage;


    private TextView chatText;
    //private List chatMessageList = new ArrayList();
    //private LinearLayout singleMessageContainer;
    private ListView listView;
    private ChatArrayAdapter chatArrayAdapter;
    private EditText txtToSend;
    private String side;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.chat);
        setContentView(R.layout.activity_chat);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        //Nuevo
        //singleMessage = (TextView) findViewById(R.id.singleMessage);
        listView = (ListView) findViewById(R.id.listView1);
        if (chatArrayAdapter==null)
            chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);


        pref = getApplicationContext().getSharedPreferences("EncierroAppPreferences", 0); // 0 - for private mode
        editor = pref.edit();



        Intent i = getIntent();
        stand = i.getStringExtra("stand");
        userId = i.getStringExtra("userId");
        resource = i.getStringExtra("resource");
        status = i.getStringExtra("status");
        test = i.getStringExtra("test");
        message = i.getStringExtra("message");

        receiveMessage();
        //txtMessage = (TextView) findViewById(R.id.message);
//        Button enviar = (Button) findViewById(R.id.btnSend);
        Button enviar = (Button) findViewById(R.id.buttonSend);
//        final EditText txtToSend = (EditText) findViewById(R.id.editText);
        txtToSend = (EditText) findViewById(R.id.chatText);

        txtToSend.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        enviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                listView.setSelection(chatArrayAdapter.getCount()-1);
            }
        });
    }




    @Override
    public void onBackPressed() {
        //Volver a la pantalla anterior
        Intent i;
        if (resource.equalsIgnoreCase("Transmisiones"))
            i = new Intent(ChatActivity_old.this, MenuActivity.class);
        else
            i = new Intent(ChatActivity_old.this, MainActivity.class);
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

    private boolean sendChatMessage() {

        String texto = txtToSend.getText().toString();
        if (texto != null) {
            if (!texto.equalsIgnoreCase("")) {
             /*   String past_sms = pref.getString(TAG_MESSAGE, null);
                if (past_sms != null) {
                    sms = past_sms + userId + ": " + texto + "\n\r";
                    //chatText.setBackgroundResource(R.drawable.bubble_b);
                    //sms2 = sms.replaceAll("<br/>", "\n\r");
                } else {
                    sms = userId + ": " + texto + "\n\r";// \n\r
                    //chatText.setBackgroundResource(R.drawable.bubble_a);
                    //sms2 = sms.replaceAll("<br/>", "\n\r");
                }
*/
                sms=texto;
                //chatArrayAdapter.add(new ChatMessage(side,chatText.getText().toString()));
                //txtMessage.setVisibility(View.VISIBLE);
                chatArrayAdapter.add(new ChatMessage(true, sms)); //true --> bubble al lado derecho
                //chatText.setText(sms);
                //txtMessage.setText(sms);
                txtToSend.setText("");
                editor.putString(TAG_MESSAGE, sms);// Storing string stand
                editor.commit();// commit changes

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_MESSAGE, sms));
                params.add(new BasicNameValuePair(TAG_USERID, userId));
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonInsert = jsonParser.makeHttpRequest(sendSMS, "POST", params);


                //chatArrayAdapter.add(new ChatMessage(side, sms));
                //txtToSend.setText("");
                return true;
            }
        }return false;
    }






 /*
        String texto= txtToSend.getText().toString();
        if (!texto.equalsIgnoreCase("")) {
            String past_sms = pref.getString(TAG_MESSAGE, null);
            if (past_sms != null) {
                sms = past_sms + userId + ": " + texto + "\n\r";
                //sms2 = sms.replaceAll("<br/>", "\n\r");
            } else {
                sms = userId + ": " + texto + "\n\r";// \n\r
                //sms2 = sms.replaceAll("<br/>", "\n\r");
            }
            //txtMessage.setVisibility(View.VISIBLE);
            chatText.setText(sms);
            //txtMessage.setText(sms);
            txtToSend.setText("");
            editor.putString(TAG_MESSAGE, sms);// Storing string stand
            editor.commit();// commit changes
*/
    //Guardar en la BBDD la conversación mantenida --> Enviar al servidor el mensaje del usuario






    public void receiveMessage() {


        //String past_sms = pref.getString(TAG_MESSAGE, null);
        if ((message != null && !message.equalsIgnoreCase("")) ) {
            //txtMessage.setVisibility(View.VISIBLE);
                   /* if (message == null) {
                        sms = past_sms;
                        //txtMessage.setText(sms);
                        //chatText.setText(sms);
                    } else {
                        if (past_sms != null) {
                            //chatText.setBackgroundResource(R.drawable.bubble_b);
                            sms = past_sms + "Coordinador: " + message + "\n\r";
                            //sms2 = sms.replaceAll("<br/>", "\n\r");
                        } else {
                            //chatText.setBackgroundResource(R.drawable.bubble_a);
                            sms = "Coordinador: " + message + "\n\r";

                            //sms2 = sms.replaceAll("<br/>", "\\n\\r");
                        } //txtMessage.setText(sms);
                 */       //chatText.setText(sms);

            chatArrayAdapter.add(new ChatMessage(false, message)); //false --> bubble al lado izquierdo
            editor.putString(TAG_MESSAGE, sms);// Storing string stand
            editor.commit();// commit changes
        }
    }

}


