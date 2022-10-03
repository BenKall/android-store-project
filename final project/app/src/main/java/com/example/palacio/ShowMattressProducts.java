package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class ShowMattressProducts extends AppCompatActivity {
    private ListView mattressprolst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mattressprostockref=db.getReference("Stock");
    private DatabaseReference mattressprostockpicref=db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<String> arrmattresspronames = new ArrayList<>();
    private static ArrayList<String> arrmattressproprice = new ArrayList<>();
    private static ArrayList<Integer> arrmattressproCODE = new ArrayList<>();
    private static ArrayList<String> arrmattresspropicnames = new ArrayList<>();
    //----------------------------------------

    private ProgressDialog prgmattresspro = null;
    private Button backToShopFromMatt;
    //----------------------------------------


    public static void delete()
    {
        while (arrmattresspropicnames.size()>0)
        {
            arrmattresspropicnames.remove(0);
            arrmattresspronames.remove(0);
            arrmattressproprice.remove(0);
            arrmattressproCODE.remove(0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mattress_products);
        delete();
        prgmattresspro=new ProgressDialog(ShowMattressProducts.this);
        prgmattresspro.setTitle("Mattress Product");
        prgmattresspro.setMessage("Loading Product Details");
        prgmattresspro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgmattresspro.setCancelable(false);
        prgmattresspro.setIcon(R.drawable.description);
        backToShopFromMatt = findViewById(R.id.backtoshopfrommatt);
        backToShopFromMatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowMattressProducts.this,shop.class));
            }
        });
        mattressprolst = findViewById(R.id.mattressprolst);
        mattressprostockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if(me.getProducttype().equals("Mattress")&&!(me.getAmount().equals("0"))) {
                        arrmattresspronames.add(me.getProductname());
                        arrmattressproprice.add(me.getPrice());
                        arrmattressproCODE.add(me.getProductcode());


                        final Stock meme = me;
                        mattressprostockpicref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    if (child.getValue(Stockpic.class).getProductcode() == meme.getProductcode()) {
                                        arrmattresspropicnames.add(child.getValue(Stockpic.class).getPicname());
                                        break;
                                    }
                                }
                                cardAdapter cardAdapter = new cardAdapter(arrmattresspropicnames, arrmattresspronames, arrmattressproprice, prgmattresspro, ShowMattressProducts.this);
                                mattressprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    mattressprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(ShowMattressProducts.this).create();

                            dialog.setTitle("Check product");
                            dialog.setMessage("Do you want to check this product?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(ShowMattressProducts.this,userCheckProduct.class);
                                    it.putExtra(userCheckProduct.line, arrmattressproCODE.get(position));
                                    startActivity(it);
                                }
                            });
                            if(Client.getMyuser().getAdmin())
                            {
                                dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowMattressProducts.this,updateProduct.class);
                                        it.putExtra(updateProduct.line, arrmattressproCODE.get(position));
                                        startActivity(it);
                                    }
                                });
                            }
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}