package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class userInfoForAdmin extends AppCompatActivity {

    public static final String line = "line";
    //public static final String priCode = "0";
    private String currentUserEmail = " ";
    private TextView txtUserGender, txtUserPhone, txtUserCity, txtUserHeight, txtUserWeight, txtUserFname, txtUserLname, txtUserAddress, txtUserBirthday;
    private ImageView userInfoImg;
    private ProgressDialog userInfoPrg =null;
    //-------------------------------------------------
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userRef=db.getReference("User");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    private Button backToViewUsersFromInfo;

    public void ShowFromFirebase(String userInQ) {


        userInfoPrg.show();

        User myuser = Client.getMyuser();

        String picname = userInQ; // User in question

        String suffix = picname.substring(picname.lastIndexOf(".") + 1);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("pics").child(picname);

        try {
            final File localFile = File.createTempFile(picname, suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    userInfoImg.setImageBitmap(bitmap);
                    userInfoPrg.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    String message = exception.getMessage();

                    Toast.makeText(userInfoForAdmin.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_for_admin);

        Intent intent = getIntent();
        currentUserEmail = getIntent().getStringExtra(line);// The current user's email

        txtUserGender = findViewById(R.id.txtusergender);
        txtUserAddress = findViewById(R.id.txtuseraddress);
        txtUserCity = findViewById(R.id.txtusercity);
        txtUserFname = findViewById(R.id.txtuserfname);
        txtUserLname = findViewById(R.id.txtuserlname);
        txtUserPhone = findViewById(R.id.txtuserphone);
        txtUserWeight = findViewById(R.id.txtuserweight);
        txtUserHeight = findViewById(R.id.txtuserheight);
        txtUserBirthday = findViewById(R.id.txtuserbirthday);
        userInfoImg = findViewById(R.id.userinfoimg);

        userInfoPrg =new ProgressDialog(userInfoForAdmin.this);
        userInfoPrg.setTitle("Checking User");
        userInfoPrg.setMessage("Loading User Details");
        userInfoPrg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        userInfoPrg.setCancelable(false);
        userInfoPrg.setIcon(R.drawable.unlock);

        backToViewUsersFromInfo = findViewById(R.id.backtoviewusersfrominfo);
        backToViewUsersFromInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userInfoForAdmin.this,viewUsers.class));
            }
        });
        // Displays all of the info from the user we picked
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    User me = child.getValue(User.class);
                    if(me.getEmail().equals(currentUserEmail))
                    {
                        txtUserGender.setText(me.getGender());
                        txtUserBirthday.setText(me.getBirthday().getDay()+"/"+(me.getBirthday().getMonth()+1)+"/"+me.getBirthday().getYear());
                        txtUserCity.setText(me.getCity());
                        txtUserAddress.setText(me.getAddress());
                        txtUserPhone.setText(me.getPhone());
                        txtUserWeight.setText(me.getWeight());
                        txtUserHeight.setText(me.getHeight());
                        txtUserFname.setText(me.getFname());
                        txtUserLname.setText(me.getLname());
                        // Show the user's profile picture
                        ShowFromFirebase(me.getPic());
                        break;
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}