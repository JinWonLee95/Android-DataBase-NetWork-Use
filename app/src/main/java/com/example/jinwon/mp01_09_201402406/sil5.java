package com.example.jinwon.mp01_09_201402406;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sil5 extends AppCompatActivity {

    EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sil5);
        Button get = (Button) findViewById(R.id.get);
        display = (EditText) findViewById(R.id.display);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNet = manager.getActiveNetworkInfo();
                    if(activeNet != null){
                        if(activeNet.getType() == ConnectivityManager.TYPE_WIFI)
                            display.setText(activeNet.toString());
                        else if(activeNet.getType() == ConnectivityManager.TYPE_MOBILE)
                            display.setText(activeNet.toString());
                    }
                }catch (Exception e){

                }
            }
        });
    }
}
