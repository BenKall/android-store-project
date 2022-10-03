package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class ShowAllProducts extends AppCompatActivity {

    private ListView allprolst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference allProStockRef=db.getReference("Stock");
    private DatabaseReference allProStockPicRef =db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<Integer> arrAllProPics = new ArrayList<>();
    private static ArrayList<String> arrAllProNames = new ArrayList<>();
    private static ArrayList<Uri> arrAllProData = new ArrayList<>();
    private static ArrayList<String> arrDataEpic = new ArrayList<>();
    private static ArrayList<String> arrAllProPrice = new ArrayList<>();
    private static ArrayList<Integer> arrAllProCODE = new ArrayList<>();
    private static ArrayList<String> arrAllProPicNames = new ArrayList<>();
    private String curpicname;
    private static boolean found = false;
    private Button backToShopFromAll;

    private ProgressDialog prgallpro = null;

    public static void delete()
    {
        while (arrAllProPicNames.size()>0)
        {
            arrAllProPicNames.clear();
            arrAllProNames.clear();
            arrAllProPrice.clear();
            arrAllProCODE.clear();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_products);
        delete();
        prgallpro=new ProgressDialog(ShowAllProducts.this);
        prgallpro.setTitle("All Product");
        prgallpro.setMessage("Loading Product Details");
        prgallpro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgallpro.setCancelable(false);
        prgallpro.setIcon(R.drawable.description);

        backToShopFromAll = findViewById(R.id.backtoshopfromall);
        backToShopFromAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowAllProducts.this,shop.class));
            }
        });

        allprolst = findViewById(R.id.allprolst);
        allProStockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if(!(me.getAmount().equals("0"))) {
                        arrAllProNames.add(me.getProductname());
                        arrAllProPrice.add(me.getPrice());
                        arrAllProCODE.add(me.getProductcode());


                        final Stock meme = me;
                        allProStockPicRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    if (child.getValue(Stockpic.class).getProductcode() == meme.getProductcode()) {
                                        arrAllProPicNames.add(child.getValue(Stockpic.class).getPicname());
                                        //found = true;
                                        break;
                                    }
                                    // if(found)
                                    // {
                                    //
                                    // }
                                }
                                cardAdapter cardAdapter = new cardAdapter(arrAllProPicNames, arrAllProNames, arrAllProPrice, prgallpro, ShowAllProducts.this);
                                allprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }



                    allprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(ShowAllProducts.this).create();

                            dialog.setTitle("Check product");
                            dialog.setMessage("Do you want to check this product?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(ShowAllProducts.this,userCheckProduct.class);
                                    it.putExtra(userCheckProduct.line, arrAllProCODE.get(position));
                                    startActivity(it);
                                }
                            });
                            if(Client.getMyuser().getAdmin())
                            {
                                dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowAllProducts.this,updateProduct.class);
                                        it.putExtra(updateProduct.line, arrAllProCODE.get(position));
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