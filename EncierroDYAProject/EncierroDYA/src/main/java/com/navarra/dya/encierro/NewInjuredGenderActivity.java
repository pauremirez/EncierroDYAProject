package com.navarra.dya.encierro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TYPE_LIST_INJURED;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class NewInjuredGenderActivity extends Activity {

    ImageButton imgGenderMale, imgGenderFemale;
    String gender, userId, stand, resource, typeList, gpsPosition, status, test;
    ImageView  imgTest;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.gender);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent i1= getIntent();
        stand=i1.getStringExtra("stand");
        userId=i1.getStringExtra("userId");
        resource=i1.getStringExtra("resource");
        gpsPosition=i1.getStringExtra("gpsPosition");
        status=i1.getStringExtra("status");
        typeList = i1.getStringExtra(TAG_TYPE_LIST_INJURED);
        test=i1.getStringExtra("test");

       //SEXO DEL HERIDO
        imgGenderMale = (ImageButton) findViewById(R.id.imgMan);
        imgGenderFemale = (ImageButton) findViewById(R.id.imgWoman);
        imgTest = (ImageView) findViewById(R.id.imgTest1);

        if (test.equalsIgnoreCase("true"))
            imgTest.setVisibility(View.VISIBLE);
        else
            imgTest.setVisibility(View.GONE);

        imgGenderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="H";
                Intent i = new Intent(NewInjuredGenderActivity.this, NewInjuredOldActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("gender",gender);
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

        imgGenderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="M";
                Intent i = new Intent(NewInjuredGenderActivity.this, NewInjuredOldActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("gender",gender);
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
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NewInjuredGenderActivity.this, MenuActivity.class);
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
}
