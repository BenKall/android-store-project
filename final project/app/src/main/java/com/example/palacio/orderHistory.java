package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderHistory extends AppCompatActivity {
    private ListView itemsOnCurUser;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ordersRef=db.getReference("Orders");
    //-----------------------
    private static ArrayList<myDate> arrDate = new ArrayList<>();
    private static ArrayList<String> arrAmount = new ArrayList<>();
    private static ArrayList<String> arrOrderCode = new ArrayList<>();
    private static String userEmail= Client.getMyuser().getEmail();
    public static long clickedOrder;
    private Button btnBackToMainHub;
    //////////////

    public static void delete()
    {
        while (arrDate.size()>0)
        {
            arrDate.remove(0);
            arrAmount.remove(0);
            arrOrderCode.remove(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        delete();

        itemsOnCurUser = findViewById(R.id.itemsoncuruser);
        btnBackToMainHub = findViewById(R.id.btnbacktomainhub);

        btnBackToMainHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(orderHistory.this,MainHub.class));
            }
        });

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Orders me = child.getValue(Orders.class);
                    if (userEmail.equals(me.getOrderUser()))
                    {
                        arrDate.add(me.getOrderDate());
                        arrAmount.add(me.getOrderAmount());
                        arrOrderCode.add(String.valueOf(me.getOrderCode()));
                        cardAdapter4 cardAdapter4 = new cardAdapter4(arrDate,arrAmount,arrOrderCode,orderHistory.this);
                        itemsOnCurUser.setAdapter(cardAdapter4);

                        itemsOnCurUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(orderHistory.this).create();

                            dialog.setTitle("Check order");
                            dialog.setMessage("Do you want to check this order?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    clickedOrder = Long.parseLong(arrOrderCode.get(position));
                                    Intent it = new Intent(orderHistory.this,orderDetailsHistory.class);
                                    startActivity(it);
                                }
                            });
                            dialog.setButton(dialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });






    }
}