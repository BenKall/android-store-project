package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userDeliveryInfo extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference allDeliveryRef=db.getReference("Delivery");
    private DatabaseReference allOrderRef=db.getReference("Orders");
    private ListView allDeliLst;
    private static ArrayList<myDate> arrDate = new ArrayList<>();
    private static ArrayList<String> arrStatus = new ArrayList<>();
    private static ArrayList<String> arrDeliveryCode = new ArrayList<>();
    private Task<Void> Taskreference = null;
    private Button btnBackToMainHub;


    public static void delete()
    {
        while (arrDate.size()>0)
        {
            arrDate.clear();
            arrStatus.clear();
            arrDeliveryCode.clear();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_delivery_info);
        delete();
        allDeliLst = findViewById(R.id.alldelilst);
        btnBackToMainHub = findViewById(R.id.backtomainhub);
        btnBackToMainHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(userDeliveryInfo.this,MainHub.class));
            }
        });


        allDeliveryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Delivery me = child.getValue(Delivery.class);
                    if(me.getOrderUser().equals(Client.getMyuser().getEmail())) {
                        arrDate.add(me.getDeliveryDate());
                        arrStatus.add(me.getStatus());
                        arrDeliveryCode.add(Long.toString(me.getDeliveryCode()));

                        cardAdapter5 cardAdapter5 = new cardAdapter5(arrDate, arrStatus, arrDeliveryCode, userDeliveryInfo.this);
                        allDeliLst.setAdapter(cardAdapter5);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}