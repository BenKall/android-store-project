package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userDeliveryInfoForAdmin extends AppCompatActivity {

    public static final String line = "line";
    private String currentUserEmail = " ";
    private String userPhone = " ";
    private ListView allDeleverieslst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userRef=db.getReference("User");
    private DatabaseReference allDeliveryRef=db.getReference("Delivery");

    private static ArrayList<myDate> arrDate = new ArrayList<>();
    private static ArrayList<String> arrStatus = new ArrayList<>();
    private static ArrayList<String> arrDeliveryCode = new ArrayList<>();
    private Task<Void> Taskreference = null;
    private Button btnBackToViewUsers;


    public static void delete()
    {
        while (arrDate.size()>0)
        {
            arrDate.clear();
            arrStatus.clear();
            arrDeliveryCode.clear();
        }
    }

    public void fillup(final ListView allDeleverieslst)
    {
        allDeliveryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {

                    Delivery me = child.getValue(Delivery.class);
                    if(me.getOrderUser().equals(currentUserEmail)) {
                        arrDate.add(me.getDeliveryDate());
                        arrStatus.add(me.getStatus());
                        arrDeliveryCode.add(Long.toString(me.getDeliveryCode()));

                        cardAdapter5 cardAdapter5 = new cardAdapter5(arrDate, arrStatus, arrDeliveryCode, userDeliveryInfoForAdmin.this);
                        allDeleverieslst.setAdapter(cardAdapter5);
                    }




                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_delivery_info_for_admin);
        delete();
        Intent intent = getIntent();
        currentUserEmail = getIntent().getStringExtra(line);// The current user's email
        allDeleverieslst = findViewById(R.id.alldelilst);
        btnBackToViewUsers = findViewById(R.id.btnbacktoviewusers);
        btnBackToViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(userDeliveryInfoForAdmin.this,viewUsers.class));
            }
        });

        Permissions p = new Permissions(userDeliveryInfoForAdmin.this);
        p.verifyPermissions();

        fillup(allDeleverieslst);

        allDeleverieslst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(userDeliveryInfoForAdmin.this).create();

                dialog.setTitle("Check User");
                dialog.setMessage("Do you want to change the status?");
                dialog.setCancelable(false);
                dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        allDeliveryRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                                Delivery newDelivery = new Delivery();
                                for (DataSnapshot child:children) {
                                    Delivery me = child.getValue(Delivery.class);
                                    if (Long.parseLong(arrDeliveryCode.get(position))==(me.getDeliveryCode()))
                                    {
                                        newDelivery.setUserOrderAddress(me.getUserOrderAddress());
                                        newDelivery.setStatus("Yes");
                                        newDelivery.setDeliveryDate(me.getDeliveryDate());
                                        newDelivery.setOrderUser(me.getOrderUser());
                                        newDelivery.setOrderCode(me.getOrderCode());
                                        newDelivery.setDeliveryCode(me.getDeliveryCode());
                                        Taskreference = db.getReference("Delivery").child(child.getKey()).setValue(newDelivery);
                                    }
                                }
                                delete();
                                fillup(allDeleverieslst);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                                for (DataSnapshot child:children) {
                                    User me = child.getValue(User.class);
                                    if (me.getEmail().equals(currentUserEmail))
                                    {
                                        userPhone = me.getPhone();
                                    }
                                }
                                String myphone=userPhone;//Client.getMyuser().getPhone();

                                String message="Your delivery has arrived!";

                                SmsManager smsManager=SmsManager.getDefault();
                                smsManager.sendTextMessage(myphone,null,message,null,null);
                                Toast.makeText(userDeliveryInfoForAdmin.this, "Your delivery has been arrived", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }
                });
                dialog.setButton(dialog.BUTTON_NEGATIVE, "Cancel it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        allDeliveryRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                                Delivery newDelivery = new Delivery();
                                for (DataSnapshot child:children) {
                                    Delivery me = child.getValue(Delivery.class);
                                    if (Long.parseLong(arrDeliveryCode.get(position))==(me.getDeliveryCode()))
                                    {
                                        newDelivery.setUserOrderAddress(me.getUserOrderAddress());
                                        newDelivery.setStatus("Canceled");
                                        newDelivery.setDeliveryDate(me.getDeliveryDate());
                                        newDelivery.setOrderUser(me.getOrderUser());
                                        newDelivery.setOrderCode(me.getOrderCode());
                                        newDelivery.setDeliveryCode(me.getDeliveryCode());
                                        Taskreference = db.getReference("Delivery").child(child.getKey()).setValue(newDelivery);
                                    }
                                }
                                delete();
                                fillup(allDeleverieslst);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                                User newUser = new User();
                                for (DataSnapshot child:children) {
                                    User me = child.getValue(User.class);
                                    if (me.getEmail().equals(currentUserEmail))
                                    {
                                        userPhone = me.getPhone();
                                        break;
                                    }
                                }
                                String myphone=userPhone;
                                String message="Your delivery has been canceled";

                                SmsManager smsManager=SmsManager.getDefault();
                                smsManager.sendTextMessage(myphone,null,message,null,null);
                                Toast.makeText(userDeliveryInfoForAdmin.this, "Your delivery has been canceled", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


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