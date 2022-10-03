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

public class ShowChairProducts extends AppCompatActivity {

    private ListView chairprolst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference chairprostockref=db.getReference("Stock");
    private DatabaseReference chairprostockpicref=db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<String> arrchairpronames = new ArrayList<>();
    private static ArrayList<String> arrchairproprice = new ArrayList<>();
    private static ArrayList<Integer> arrchairproCODE = new ArrayList<>();
    private static ArrayList<String> arrchairpropicnames = new ArrayList<>();
    private Button backToShopFromChair;
    //----------------------------------------

    private ProgressDialog prgchairpro = null;
    //----------------------------------------


    public static void delete()
    {
        while (arrchairpropicnames.size()>0)
        {
            arrchairpropicnames.remove(0);
            arrchairpronames.remove(0);
            arrchairproprice.remove(0);
            arrchairproCODE.remove(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chair_products);
        delete();
        prgchairpro=new ProgressDialog(ShowChairProducts.this);
        prgchairpro.setTitle("All Product");
        prgchairpro.setMessage("Loading Product Details");
        prgchairpro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgchairpro.setCancelable(false);
        prgchairpro.setIcon(R.drawable.description);
        chairprolst = findViewById(R.id.chairprolst);
        backToShopFromChair = findViewById(R.id.backtoshopfromchair);
        backToShopFromChair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowChairProducts.this,shop.class));
            }
        });

        chairprostockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if(me.getProducttype().equals("Chair")&&!(me.getAmount().equals("0"))) {
                        arrchairpronames.add(me.getProductname());
                        arrchairproprice.add(me.getPrice());
                        arrchairproCODE.add(me.getProductcode());


                        final Stock meme = me;
                        chairprostockpicref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    if (child.getValue(Stockpic.class).getProductcode() == meme.getProductcode()) {
                                        arrchairpropicnames.add(child.getValue(Stockpic.class).getPicname());
                                        break;
                                    }
                                }
                                cardAdapter cardAdapter = new cardAdapter(arrchairpropicnames, arrchairpronames, arrchairproprice, prgchairpro, ShowChairProducts.this);
                                chairprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }



                    chairprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(ShowChairProducts.this).create();

                            dialog.setTitle("Check product");
                            dialog.setMessage("Do you want to check this product?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(ShowChairProducts.this,userCheckProduct.class);
                                    it.putExtra(userCheckProduct.line, arrchairproCODE.get(position));
                                    startActivity(it);
                                }
                            });
                            if(Client.getMyuser().getAdmin())
                            {
                                dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowChairProducts.this,updateProduct.class);
                                        it.putExtra(updateProduct.line, arrchairproCODE.get(position));
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