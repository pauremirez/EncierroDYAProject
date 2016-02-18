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
public class NewInjuredOldActivity extends Activity {

    ImageButton  btnMinus30, btn3050, btnPlus50;
    String old, userId, gender,stand, resource, typeList, gpsPosition, status,test;
    ImageView  imgTest;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //Para evitar la android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.old);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnMinus30 = (ImageButton) findViewById(R.id.btnMinus30);
        btn3050 = (ImageButton) findViewById(R.id.btn3050);
        btnPlus50 = (ImageButton) findViewById(R.id.btnPlus50);
        imgTest = (ImageView) findViewById(R.id.imgTest2);

        Intent i1= getIntent();
        stand=i1.getStringExtra("stand");
        userId=i1.getStringExtra("userId");
        gender=i1.getStringExtra("gender");
        resource=i1.getStringExtra("resource");
        gpsPosition=i1.getStringExtra("gpsPosition");
        status=i1.getStringExtra("status");
        typeList = i1.getStringExtra(TAG_TYPE_LIST_INJURED);
        test=i1.getStringExtra("test");

        if (test.equalsIgnoreCase("true"))
            imgTest.setVisibility(View.VISIBLE);
        else
            imgTest.setVisibility(View.GONE);

        btnMinus30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old="-30";
                Intent i = new Intent(NewInjuredOldActivity.this, NewInjuredSymptomActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("gender",gender);
                i.putExtra("old",old);
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
        btn3050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old="30-50";
                Intent i = new Intent(NewInjuredOldActivity.this, NewInjuredSymptomActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("gender",gender);
                i.putExtra("old",old);
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

        btnPlus50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old="+50";
                Intent i = new Intent(NewInjuredOldActivity.this, NewInjuredSymptomActivity.class);
                i.putExtra("userId",userId);
                i.putExtra("gender",gender);
                i.putExtra("old",old);
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
        Intent i = new Intent(NewInjuredOldActivity.this,NewInjuredGenderActivity.class);
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