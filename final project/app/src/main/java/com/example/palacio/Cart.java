package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Cart extends AppCompatActivity {

    private ListView itemsOnCurOrder;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference orderDetailsRef=db.getReference("Order Details");
    private DatabaseReference ordersRef=db.getReference("Orders");
    private static ArrayList<String> arrPPU = new ArrayList<>();
    private static ArrayList<String> arrAmount = new ArrayList<>();
    private static ArrayList<String> arrOrderCode = new ArrayList<>();
    private String mypath = "gs://final-project-9dd22.appspot.com";
    private int curOrderCode =0;
    private Button btnToMakeDelivery, btnCancelDelivery, btnBackToMainHub;
    //////////////////////////////////////
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String orderDate;
    private int count = 0;
    //////////////////////////////////////

    public static void delete()
    {
        while (arrPPU.size()>0)
        {
            arrPPU.remove(0);
            arrAmount.remove(0);
            arrOrderCode.remove(0);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        delete();

        itemsOnCurOrder = findViewById(R.id.itemsoncurorder);


        orderDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    OrderDetails me = child.getValue(OrderDetails.class);
                    if (shop.curOrders.getOrderCode()==me.getOrderDetsCode())
                    {
                        arrPPU.add(me.getOrderDetsProPPU());
                        arrAmount.add(me.getOrderDetsProAmount());
                        arrOrderCode.add(String.valueOf(me.getOrderDetsCode()));
                        count++;//counts amount of orders
                        cardAdapter3 cardAdapter3 = new cardAdapter3(arrPPU,arrAmount,arrOrderCode,Cart.this);
                        itemsOnCurOrder.setAdapter(cardAdapter3);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
        });

        btnToMakeDelivery = findViewById(R.id.btntomakedelivery);
        btnToMakeDelivery.setOnClickListener(new View.OnClickListener() { // make the button send you to said activity
            @Override
            public void onClick(View v) {



                startActivity(new Intent(Cart.this,makeDelivery.class));
            }
        });
        btnBackToMainHub = findViewById(R.id.btnbacktomainhubfromcart);
        btnBackToMainHub.setOnClickListener(new View.OnClickListener() { // make the button send you to said activity
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this,shop.class));
            }
        });
        btnCancelDelivery = findViewById(R.id.btncanceldelivery);
        btnCancelDelivery.setOnClickListener(new View.OnClickListener() { // make the button send you to said activity
            @Override
            public void onClick(View v) {

                shop.curOrders = null;
                shop.FirstTime = true;
                Toast.makeText(Cart.this, "Delivery has been cancelled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cart.this,shop.class));
            }
        });






    }
}