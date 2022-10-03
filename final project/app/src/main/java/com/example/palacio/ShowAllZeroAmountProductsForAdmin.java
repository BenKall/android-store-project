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

public class ShowAllZeroAmountProductsForAdmin extends AppCompatActivity {


    private ListView allzprolst;
    private Button backToShopFromZ;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference allProzStockRef=db.getReference("Stock");
    private DatabaseReference allProzStockPicRef =db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<Integer> arrAllzProPics = new ArrayList<>();
    private static ArrayList<String> arrAllzProNames = new ArrayList<>();
    private static ArrayList<Uri> arrAllzProData = new ArrayList<>();
    private static ArrayList<String> arrDataEpic = new ArrayList<>();
    private static ArrayList<String> arrAllzProPrice = new ArrayList<>();
    private static ArrayList<Integer> arrAllzProCODE = new ArrayList<>();
    private static ArrayList<String> arrAllzProPicNames = new ArrayList<>();
    private String curpicname;
    private static boolean found = false;



    private ProgressDialog prgallzpro = null;

    public static void delete()
    {
        while (arrAllzProPicNames.size()>0)
        {
            arrAllzProPicNames.clear();
            arrAllzProNames.clear();
            arrAllzProPrice.clear();
            arrAllzProCODE.clear();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_zero_amount_products_for_admin);
        delete();
        prgallzpro=new ProgressDialog(ShowAllZeroAmountProductsForAdmin.this);
        prgallzpro.setTitle("All Empty Products");
        prgallzpro.setMessage("Loading Product Details");
        prgallzpro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgallzpro.setCancelable(false);
        prgallzpro.setIcon(R.drawable.description);
        backToShopFromZ = findViewById(R.id.backtoshopfromzeropro);
        backToShopFromZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowAllZeroAmountProductsForAdmin.this,shop.class));
            }
        });
        allzprolst = findViewById(R.id.allprolst);
        allProzStockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if(me.getAmount().equals("0")) {
                        arrAllzProNames.add(me.getProductname());
                        arrAllzProPrice.add(me.getPrice());
                        arrAllzProCODE.add(me.getProductcode());


                        final Stock meme = me;
                        allProzStockPicRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    if (child.getValue(Stockpic.class).getProductcode() == meme.getProductcode()) {
                                        arrAllzProPicNames.add(child.getValue(Stockpic.class).getPicname());
                                        break;
                                    }
                                }
                                cardAdapter cardAdapter = new cardAdapter(arrAllzProPicNames, arrAllzProNames, arrAllzProPrice, prgallzpro, ShowAllZeroAmountProductsForAdmin.this);
                                allzprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }



                    allzprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(ShowAllZeroAmountProductsForAdmin.this).create();

                            dialog.setTitle("Check product");
                            dialog.setMessage("Do you want to check this product?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(ShowAllZeroAmountProductsForAdmin.this,userCheckProduct.class);
                                    it.putExtra(userCheckProduct.line, arrAllzProCODE.get(position));
                                    startActivity(it);
                                }
                            });
                            if(Client.getMyuser().getAdmin())
                            {
                                dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowAllZeroAmountProductsForAdmin.this,updateProduct.class);
                                        it.putExtra(updateProduct.line, arrAllzProCODE.get(position));
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