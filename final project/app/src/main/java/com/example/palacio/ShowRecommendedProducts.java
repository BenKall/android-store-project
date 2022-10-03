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

public class ShowRecommendedProducts extends AppCompatActivity {
    public static final String line = "line";
    public static final int priCode = 0;
    public static int currentAngle = 0;
    private int currentHeight = Integer.parseInt(Client.getMyuser().getHeight());
    private int currentWeight = Integer.parseInt(Client.getMyuser().getWeight());
    private String aType = "A", hType = "A", wType = "A";
    //\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\//
    private ListView recommprolst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference recommProStockRef=db.getReference("Stock");
    private DatabaseReference recommProStockPicRef =db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<Integer> arrRecommProPics = new ArrayList<>();
    private static ArrayList<String> arrRecommProNames = new ArrayList<>();
    private static ArrayList<Uri> arrRecommProData = new ArrayList<>();
    private static ArrayList<String> arrDataEpic = new ArrayList<>();
    private static ArrayList<String> arrRecommProPrice = new ArrayList<>();
    private static ArrayList<Integer> arrRecommProCODE = new ArrayList<>();
    private static ArrayList<String> arrRecommProPicNames = new ArrayList<>();
    private String curpicname;
    private static boolean found = false;

    private ProgressDialog prgRecommpro = null;
    private Button backToAngleFromRec;


    public void getDataINeed()// gets the data that I need to recommend to user
    {
        if(currentAngle>30)
        {
            if(currentAngle>80)
                aType = "C";
            else
            aType = "B";
        }

        if(currentHeight>120)
        {
            if(currentHeight>180)
                hType = "C";
            else
                hType = "B";
        }
        if(currentWeight>60)
        {
            if(currentWeight>120)
                wType = "C";
            else
                wType = "B";
        }
    }

    public static void delete()
    {
        while (arrRecommProPicNames.size()>0)
        {
            arrRecommProPicNames.clear();
            arrRecommProNames.clear();
            arrRecommProPrice.clear();
            arrRecommProCODE.clear();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recommended_products);
        delete();
        prgRecommpro =new ProgressDialog(ShowRecommendedProducts.this);
        prgRecommpro.setTitle("All Product");
        prgRecommpro.setMessage("Loading Product Details");
        prgRecommpro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgRecommpro.setCancelable(false);
        prgRecommpro.setIcon(R.drawable.description);
        backToAngleFromRec = findViewById(R.id.backtoanglefromrec);
        backToAngleFromRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowRecommendedProducts.this,angleCheck.class));
            }
        });
        Intent intent = getIntent();
       // currentAngle = getIntent().getIntExtra(line,priCode);// The current users angle

        getDataINeed();

        recommprolst= findViewById(R.id.recommprolst);

        recommProStockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if((me.getaType().equals(aType) && (me.gethType().equals(hType) || me.getwType().equals(wType)))&&!(me.getAmount().equals("0")))
                    {
                        arrRecommProNames.add(me.getProductname());
                        arrRecommProPrice.add(me.getPrice());
                        arrRecommProCODE.add(me.getProductcode());

                        final Stock meme = me;
                        recommProStockPicRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                                for (DataSnapshot child:children)
                                {
                                    if(child.getValue(Stockpic.class).getProductcode()==meme.getProductcode())
                                    {
                                        arrRecommProPicNames.add(child.getValue(Stockpic.class).getPicname());
                                        break;
                                    }

                                }
                                cardAdapter cardAdapter = new cardAdapter(arrRecommProPicNames, arrRecommProNames, arrRecommProPrice,prgRecommpro,ShowRecommendedProducts.this);
                                recommprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        recommprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                AlertDialog dialog = new AlertDialog.Builder(ShowRecommendedProducts.this).create();

                                dialog.setTitle("Check product");
                                dialog.setMessage("Do you want to check this product?");
                                dialog.setCancelable(false);
                                dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowRecommendedProducts.this,userCheckProduct.class);
                                        it.putExtra(userCheckProduct.line, arrRecommProCODE.get(position));
                                        startActivity(it);
                                    }
                                });
                                if(Client.getMyuser().getAdmin())
                                {
                                    dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent it = new Intent(ShowRecommendedProducts.this,updateProduct.class);
                                            it.putExtra(updateProduct.line, arrRecommProCODE.get(position));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}