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

public class ShowSofaProducts extends AppCompatActivity {

    private ListView sofaprolst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference sofaprostockref=db.getReference("Stock");
    private DatabaseReference sofaprostockpicref=db.getReference("Stock Pics");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    //----------------------------------------
    private static ArrayList<String> arrsofapronames = new ArrayList<>();
    private static ArrayList<String> arrsofaproprice = new ArrayList<>();
    private static ArrayList<Integer> arrsofaproCODE = new ArrayList<>();
    private static ArrayList<String> arrsofapropicnames = new ArrayList<>();
    //----------------------------------------

    private ProgressDialog prgsofapro = null;
    private Button backToShopFromSofa;
    //----------------------------------------


    public static void delete()
    {
        while (arrsofapropicnames.size()>0)
        {
            arrsofapropicnames.remove(0);
            arrsofapronames.remove(0);
            arrsofaproprice.remove(0);
            arrsofaproCODE.remove(0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sofa_products);
        delete();
        prgsofapro=new ProgressDialog(ShowSofaProducts.this);
        prgsofapro.setTitle("Sofa Product");
        prgsofapro.setMessage("Loading Product Details");
        prgsofapro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgsofapro.setCancelable(false);
        prgsofapro.setIcon(R.drawable.description);
        backToShopFromSofa = findViewById(R.id.backtoshopfromsofa);
        backToShopFromSofa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowSofaProducts.this,shop.class));
            }
        });
        sofaprolst = findViewById(R.id.sofaprolst);
        sofaprostockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock me = child.getValue(Stock.class);
                    if(me.getProducttype().equals("Sofa")&&!(me.getAmount().equals("0"))) {
                        arrsofapronames.add(me.getProductname());
                        arrsofaproprice.add(me.getPrice());
                        arrsofaproCODE.add(me.getProductcode());


                        final Stock meme = me;
                        sofaprostockpicref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    if (child.getValue(Stockpic.class).getProductcode() == meme.getProductcode()) {
                                        arrsofapropicnames.add(child.getValue(Stockpic.class).getPicname());
                                        break;
                                    }
                                }
                                cardAdapter cardAdapter = new cardAdapter(arrsofapropicnames, arrsofapronames, arrsofaproprice, prgsofapro, ShowSofaProducts.this);
                                sofaprolst.setAdapter(cardAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        sofaprolst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                AlertDialog dialog = new AlertDialog.Builder(ShowSofaProducts.this).create();

                                dialog.setTitle("Check product");
                                dialog.setMessage("Do you want to check this product?");
                                dialog.setCancelable(false);
                                dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(ShowSofaProducts.this,userCheckProduct.class);
                                        it.putExtra(userCheckProduct.line, arrsofaproCODE.get(position));
                                        startActivity(it);
                                    }
                                });
                                if(Client.getMyuser().getAdmin())
                                {
                                    dialog.setButton(dialog.BUTTON_NEGATIVE, "Update Product", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent it = new Intent(ShowSofaProducts.this,updateProduct.class);
                                            it.putExtra(updateProduct.line, arrsofaproCODE.get(position));
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