package com.example.palacio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.Visibility;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;

public class MainHub extends AppCompatActivity {

    private Button btn,update,admin,btngobuy,btnOrderHistory, Deliveries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);

        btn = findViewById(R.id.logout);

        admin = findViewById(R.id.ADMIN);

        update = findViewById(R.id.Update);

        btngobuy = findViewById(R.id.btngobuy);

        btnOrderHistory = findViewById(R.id.btnorderhis);

        Deliveries = findViewById(R.id.Deliveries);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client.setMyuser(null);

                startActivity(new Intent(MainHub.this,MainActivity.class));
            }
        });

        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainHub.this,orderHistory.class));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainHub.this,Update.class));
            }
        });

        Deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainHub.this,userDeliveryInfo.class));
            }
        });

        btngobuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainHub.this,shop.class));

            }
        });
        if(Client.getMyuser().getAdmin())
            admin.setVisibility(View.VISIBLE);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainHub.this,admin.class));
            }
        });






    }
}
