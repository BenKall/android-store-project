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

public class ShowTableProducts extends AppCompatActivity {
    private ListView tableprolst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference tableprostockref=db.getReference("Stock");
    private DatabaseReference tableprostockpicref=db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<String> arrtablepronames = new ArrayList<>();
    private static ArrayList<String> arrtableproprice = new ArrayList<>();
    private static ArrayList<Integer> arrtableproCODE = new ArrayList<>();
    private static ArrayList<String> arrtablepropicnames = new ArrayList<>();
    //----------------------------------------

    private ProgressDialog prgtablepro = null;
    private Button backToShopFromTable;
    //----------------------------------------


    public static void delete()
    {
        while (arrtablepropicnames.size()>0)
        {
            arrtablepropicnames.remove(0);
            arrtablepronames.remove(0);
            arrtableproprice.remove(0);
            arrtableproCODE.remove(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_products);
        delete();
        prgtablepro=new ProgressDialog(ShowTableProducts.this);
        prgtablepro.setTitle("Table Product");
        prgtablepro.setMessage("Loading Product Details");
        prgtablepro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgtablepro.setCancelable(false);
        prgtablepro.setIcon(R.drawable.description);
        backToShopFromTable = findViewById(R.id.backtoshopfromtable);
        backToShopFromTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTableProducts.this,shop.class));
            }
        });
        tableprolst = findViewById(R.id.tableprolst);
        tableprostockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if(me.getProducttype().equals("Table")&&!(me.getAmount().equals("0"))) {
                        arrtablepronames.add(me.getProductname());
                        arrtableproprice.add(me.getPrice());
                        arrtableproCODE.add(me.getProductcode());


                        final Stock meme = me;
                        tableprostockpicref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    if (child.getValue(Stockpic.class).getProductcode() == meme.getProductcode()) {
                                        arrtablepropicnames.add(child.getValue(Stockpic.class).getPicname());
                                        break;
                                    }
                                }
                                cardAdapter cardAdapter = new cardAdapter(arrtablepropicnames, arrtablepronames, arrtableproprice, prgtablepro, ShowTableProducts.this);
                                tableprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    tableprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(ShowTableProducts.this).create();

                            dialog.setTitle("Check product");
                            dialog.setMessage("Do you want to check this product?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(ShowTableProducts.this,userCheckProduct.class);
                                    it.putExtra(userCheckProduct.line, arrtableproCODE.get(position));
                                    startActivity(it);
                                }
                            });
                            if(Client.getMyuser().getAdmin())
                            {
                                dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowTableProducts.this,updateProduct.class);
                                        it.putExtra(updateProduct.line, arrtableproCODE.get(position));
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