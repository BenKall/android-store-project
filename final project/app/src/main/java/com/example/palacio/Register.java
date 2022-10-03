package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    private EditText editemail,editpass,editphone,editFname,editLname,editCity,editAddress,editgender,editweight,editheight;
    private ImageView img;
    private Button btnsend,btnpic,editBday,back;
    private RadioGroup rdg;
    private Spinner choocity;
    private RadioButton rdbmale,rdbfemale;
    private CircleImageView circleImageView;

    //----------------------------------------
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = null;
    private FirebaseAuth auth=null;
    private DatabaseReference cityreference = db.getReference("City");
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    //----------------------------------------
    private static Uri selectedImage=null;
    private static String picname="";
    private ProgressDialog prg=null;
    private int day, month, year;
    private static boolean choosen=false;
    private ArrayList<String> arrayListCITIES = new ArrayList<String>();
    //------------------------------------------
    private ArrayList<String> arrayListday = new ArrayList<String>();
    private ArrayList<String> arrayListmonth = new ArrayList<String>();
    private ArrayList<String> arrayListyear = new ArrayList<String>();
    //------------------------------------------
    private void AddPicture()
    {
        StorageReference mypic_storage=storageReference.child("pics/" + picname);

        mypic_storage.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(Register.this, "Picture DataBase is Updated!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this,Login.class));
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==777 && data!=null)
        {
            choosen=true;

            selectedImage=data.getData();
            circleImageView.setImageURI(selectedImage);

            String[] filepathcolumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedImage,filepathcolumn,null,null,null);
            cursor.moveToFirst();
            int columnindex=cursor.getColumnIndex(filepathcolumn[0]);

            picname=cursor.getString(columnindex);

            picname=picname.substring(picname.lastIndexOf("/")+1);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editemail=findViewById(R.id.editemail);
        editpass=findViewById(R.id.editpass);
        editphone=findViewById(R.id.editphone);

        editweight=findViewById(R.id.editweight);
        editheight=findViewById(R.id.editheight);

        btnsend=findViewById(R.id.btnsend);
        btnpic=findViewById(R.id.btnpic);
        back = findViewById(R.id.back);

        editFname = findViewById(R.id.editFname);
        editLname = findViewById(R.id.editLname);

        editBday = findViewById(R.id.editBday);

        choocity = findViewById(R.id.choocity);
        editAddress = findViewById(R.id.editadress);
        rdg = findViewById(R.id.rdg);
        rdbmale = findViewById(R.id.rdbmale);
        rdbfemale = findViewById(R.id.rdbfemale);

        circleImageView = findViewById(R.id.cricImage);


        prg=new ProgressDialog(Register.this);
        prg.setTitle("New Register");
        prg.setMessage("Saving User Details");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);
        prg.setIcon(R.drawable.agala);




        final Permissions p = new Permissions(Register.this);
        p.verifyPermissions();

        //----------------------------------------------
        db=FirebaseDatabase.getInstance();
        reference=db.getReference("User");
        auth=FirebaseAuth.getInstance();
        //----------------------------------------------

        btnpic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it,777);

            }
        });


        cityreference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    String City = child.getValue(String.class);
                    arrayListCITIES.add(City);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this, R.layout.customspinner, arrayListCITIES);
                choocity.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        editBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editBday.setText(day+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prg.show();

                final String email=editemail.getText().toString();
                final  String pass=editpass.getText().toString();
                String pic ="profile.png";

                if(choosen==true)
                    pic=picname;

                final String myfinalpic=pic;

                final String myphone = editphone.getText().toString();

                final String myFname = editFname.getText().toString();

                final String myLname = editLname.getText().toString();


                String gender="Male";
                if(rdbfemale.isChecked()==true)
                    gender="Female";
                final String mygender=gender;


                final String myweight = editweight.getText().toString();

                final String myheight = editheight.getText().toString();

                final String myaddress = editAddress.getText().toString();

                final String mycity = choocity.getSelectedItem().toString();

                char[] TEMP = editBday.getText().toString().toCharArray();
                String[] date = new String[3];
                date[0] = "";
                date[1] = "";
                date[2] = "";
                int t = 0;
                for (int i = 0; i < TEMP.length; i++) {
                    if(TEMP[i]!='/')
                        date[t] += TEMP[i];
                    else
                        t++;
                }
                day = Integer.parseInt(date[0]);
                month = Integer.parseInt(date[1])-1;
                year = Integer.parseInt(date[2]);

                final myDate mybirth = new myDate(year,month,day,0,0);

                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser currentUser=auth.getCurrentUser();
                            String uid=currentUser.getUid();
                            User u= new User(email,myFname,myLname,mybirth,myphone,myfinalpic,mygender,myweight,myheight,false,myaddress,mycity);

                            reference.child(uid).setValue(u).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        prg.dismiss();

                                        Toast.makeText(Register.this, "DataBase Updated", Toast.LENGTH_SHORT).show();

                                        if(choosen==true)
                                            AddPicture();
                                        startActivity(new Intent(Register.this,Login.class));
                                    }

                                }
                            }).addOnFailureListener(Register.this, new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    prg.dismiss();

                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                    }
                }).addOnFailureListener(Register.this, new OnFailureListener()
                {

                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        prg.dismiss();
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
