
package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderDetailsHistory extends AppCompatActivity {

    public static final String line = "line";
    public static final long priCode = 0;
    private long currentOrderDetsCode = orderHistory.clickedOrder;
    private ListView itemsOnCurOrder;
    private Button btnBackToOrderHis;
    ////
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference orderDetailsRef=db.getReference("Order Details");
    private DatabaseReference proStockRef=db.getReference("Stock");
    private DatabaseReference proStockPicRef=db.getReference("Stock Pics");
    private static ArrayList<String> arrPPU = new ArrayList<>();
    private static ArrayList<String> arrAmount = new ArrayList<>();
    private static ArrayList<String> arrOrderDetsProductCode = new ArrayList<>();
    private static ArrayList<String> arrProNames = new ArrayList<>();
    private static ArrayList<String> arrProPicNames = new ArrayList<>();
    private ProgressDialog prgOrderDets = null;

    public static void delete()
    {
        while (arrPPU.size()>0)
        {
            arrPPU.remove(0);
            arrAmount.remove(0);
            arrOrderDetsProductCode.remove(0);
            arrProPicNames.remove(0);
            arrProNames.remove(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_history);
        delete();

        prgOrderDets =new ProgressDialog(orderDetailsHistory.this);
        prgOrderDets.setTitle("All Product");
        prgOrderDets.setMessage("Loading Product Details");
        prgOrderDets.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgOrderDets.setCancelable(false);
        prgOrderDets.setIcon(R.drawable.description);
        itemsOnCurOrder = findViewById(R.id.itemsoncurorder);
        btnBackToOrderHis = findViewById(R.id.btnbacktoorderhis);

        btnBackToOrderHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(orderDetailsHistory.this,orderHistory.class));
            }
        });

        orderDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    OrderDetails me = child.getValue(OrderDetails.class);
                    if (currentOrderDetsCode==me.getOrderDetsCode())
                    {
                        arrPPU.add(me.getOrderDetsProPPU());
                        arrAmount.add(me.getOrderDetsProAmount());
                        arrOrderDetsProductCode.add(String.valueOf(me.getOrderDetsProductCode()));

                        final OrderDetails meme = me;
                        proStockRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {

                                    if (child.getValue(Stock.class).getProductcode() == meme.getOrderDetsProductCode()) {
                                        arrProNames.add(child.getValue(Stock.class).getProductname());
                                        final OrderDetails mememe = meme;
                                        proStockPicRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                                for (DataSnapshot child : children) {
                                                    if (child.getValue(Stockpic.class).getProductcode() == mememe.getOrderDetsProductCode()) {
                                                        arrProPicNames.add(child.getValue(Stockpic.class).getPicname());
                                                        break;
                                                    }
                                                }
                                                cardAdapter6 cardAdapter = new cardAdapter6(arrProPicNames, arrProNames, arrPPU, arrAmount, prgOrderDets, orderDetailsHistory.this);
                                                itemsOnCurOrder.setAdapter(cardAdapter);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}