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

public class viewUsers extends AppCompatActivity {
    private ListView allUsersLst;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference allUserRef=db.getReference("User");
    //----------------------------------------
    private static ArrayList<String> arrAllUserPhones = new ArrayList<>();
    private static ArrayList<String> arrAllUserPicNames = new ArrayList<>();
    private static ArrayList<String> arrAllUserEmails = new ArrayList<>();
    private Button btnBackToAdmin;


    private ProgressDialog prgallusers = null;

    public static void delete()
    {
        while (arrAllUserPicNames.size()>0)
        {
            arrAllUserPicNames.clear();
            arrAllUserEmails.clear();
            arrAllUserPhones.clear();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        delete();
        prgallusers=new ProgressDialog(viewUsers.this);
        prgallusers.setTitle("All Users");
        prgallusers.setMessage("Loading User Details");
        prgallusers.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgallusers.setCancelable(false);
        prgallusers.setIcon(R.drawable.description);
        btnBackToAdmin = findViewById(R.id.btnbacktoadminfromviewusers);
        btnBackToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(viewUsers.this,admin.class));
            }
        });

        allUsersLst = findViewById(R.id.alluserslst);

        allUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    User me = child.getValue(User.class);
                    arrAllUserEmails.add(me.getEmail());
                    arrAllUserPhones.add(me.getPhone());
                    arrAllUserPicNames.add(me.getPic());

                    cardAdapterAllUsers cardAdapter = new cardAdapterAllUsers(arrAllUserPicNames, arrAllUserEmails, arrAllUserPhones,prgallusers,viewUsers.this);
                    allUsersLst.setAdapter(cardAdapter);

                    allUsersLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(viewUsers.this).create();

                            dialog.setTitle("Check User");
                            dialog.setMessage("Do you want to check this user?");
                            dialog.setCancelable(false);
                            dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(viewUsers.this,userInfoForAdmin.class);
                                    it.putExtra(userInfoForAdmin.line, arrAllUserEmails.get(position));
                                    startActivity(it);
                                }
                            });
                            dialog.setButton(dialog.BUTTON_NEGATIVE, "To Their Deliveries", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent it = new Intent(viewUsers.this,userDeliveryInfoForAdmin.class);
                                    it.putExtra(userDeliveryInfoForAdmin.line, arrAllUserEmails.get(position));
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}