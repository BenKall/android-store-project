package com.example.palacio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class admin extends AppCompatActivity {

    private Button add,viewUser,btnBackToMainHub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btnBackToMainHub = findViewById(R.id.btnbacktomainhubfromadmin);
        add = findViewById(R.id.adddd);
        viewUser = findViewById(R.id.viewuser);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin.this,addproduct.class));
            }
        });

        btnBackToMainHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin.this,MainHub.class));
            }
        });

        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin.this,viewUsers.class));
            }
        });
    }
}