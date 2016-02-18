/**
 * Author: Paula Remirez
 * twitter: http://twitter.com/pauremirez
 * company: DYA Navarra
 * */
package com.navarra.dya.encierro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


@SuppressWarnings("ALL")
public class SettingsActivity extends Activity {

    ImageView imgSettings;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        imgSettings = (ImageView) findViewById(R.id.imgSettings);


        // save button click event
        imgSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setContentView(R.layout.login);
            }
        });

    }
}