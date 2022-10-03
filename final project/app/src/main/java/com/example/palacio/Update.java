package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Update extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference("User");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Task<Void> Taskreference = null;
    //
    private Button back;
    //
    //--------------------------------------------------
    private TextView txtside, txtphone, txtpicture,txtheight,txtweight,txtcity,txtaddress,txtfname,txtlname;
    private RadioGroup rdg;
    private EditText editphone, editheight, editweight, editcity, editaddress, editfname, editlname;
    private RadioButton rdbmale, rdbfemale;
    private CircleImageView circleImageView;
    private Button btnchimg, btnsave, editbirthday;
    private String profile = Client.getMyuser().getPic();
    private String realphone = Client.getMyuser().getPhone();
    private String firstFname = Client.getMyuser().getFname();
    private String firstLname = Client.getMyuser().getLname();
    private String firstheight = Client.getMyuser().getHeight();
    private String firstweight = Client.getMyuser().getWeight();
    private String firstcity = Client.getMyuser().getCity();
    private String firstadrress = Client.getMyuser().getAddress();
    private int day, month, year, day1,month1,year1;
    private Boolean rebirth = false;
    private ProgressDialog prg=null;
    //--------------------------------------------------
    private static Uri selectedImage = null;
    private static String picname = "";

    private static boolean choosen = false;
    private String mypath = "gs://final-project-9dd22.appspot.com";  // Image Path

    //--------------------------------------------------
    private void AddPicture() {
        StorageReference mypic_storage = mStorageRef.child("pics/" + picname);

        mypic_storage.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Update.this, "Picture DataBase is Updated!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Update.this, MainHub.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666 && data != null) {
            choosen = true;
            selectedImage = data.getData();
            circleImageView.setImageURI(selectedImage);
            String[] filepathcolumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filepathcolumn, null, null, null);
            cursor.moveToFirst();
            int columnindex = cursor.getColumnIndex(filepathcolumn[0]);
            picname = cursor.getString(columnindex);
            picname = picname.substring(picname.lastIndexOf("/") + 1);
        }
    }

    public void ShowFromFirebase() {


        prg.show();

        User myuser = Client.getMyuser();

        String picname = myuser.getPic();

        String suffix = picname.substring(picname.lastIndexOf(".") + 1);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("pics").child(picname);

        try {
            final File localFile = File.createTempFile(picname, suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    circleImageView.setImageBitmap(bitmap);
                    prg.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    String message = exception.getMessage();

                    Toast.makeText(Update.this, message, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_update);

        txtside = findViewById(R.id.txtside);
        txtphone = findViewById(R.id.txtphone);
        txtpicture = findViewById(R.id.txtPicture);
        txtcity = findViewById(R.id.txtcity);
        txtaddress = findViewById(R.id.txtaddress);
        txtheight = findViewById(R.id.txtheight);
        txtweight = findViewById(R.id.txtweight);
        txtfname = findViewById(R.id.txtfname);
        txtlname = findViewById(R.id.txtlname);
        rdg = findViewById(R.id.rdg);
        rdbmale = findViewById(R.id.rdbmale);
        rdbfemale = findViewById(R.id.rdbfemale);
        editphone = findViewById(R.id.editphone);
        circleImageView = findViewById(R.id.cricImage);
        btnchimg = findViewById(R.id.btnchimg);
        btnsave = findViewById(R.id.btnsave);
        editheight = findViewById(R.id.editheight);
        editweight = findViewById(R.id.editweight);
        editcity = findViewById(R.id.editcity);
        editaddress = findViewById(R.id.editaddress);
        editfname = findViewById(R.id.editfname);
        editlname = findViewById(R.id.editlname);
        editbirthday = findViewById(R.id.editbirthday);

        back=findViewById(R.id.back);

        txtside.setText("Gender:  " + Client.getMyuser().getGender());
        txtphone.setText("Phone:  " + Client.getMyuser().getPhone());
        txtcity.setText("City:  " + Client.getMyuser().getCity());
        txtaddress.setText("Address:  " + Client.getMyuser().getAddress());
        txtweight.setText("Weight:  " + Client.getMyuser().getWeight());
        txtheight.setText("Height:  " + Client.getMyuser().getHeight());
        txtfname.setText("First name:  " + Client.getMyuser().getFname());
        txtlname.setText("Last name:  " + Client.getMyuser().getLname());
        editbirthday.setText(Client.getMyuser().getBirthday().getDay()+"/"+(Client.getMyuser().getBirthday().getMonth()+1)+"/"+Client.getMyuser().getBirthday().getYear());


        prg=new ProgressDialog(Update.this);
        prg.setTitle("Here to change something?");
        prg.setMessage("Loading User Details");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);
        prg.setIcon(R.drawable.unlock);

        ShowFromFirebase();

        if (Client.getMyuser().getGender().equals("Female"))
            rdbfemale.setChecked(true);
        else
            rdbmale.setChecked(true);

        btnchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, 666);
            }
        });

                editbirthday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog=new DatePickerDialog(Update.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                editbirthday.setText(day+"/"+(month+1)+"/"+year);
                            }
                        },year,month,day);
                        datePickerDialog.show();
                        rebirth = true;
                    }
                });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Update.this,MainHub.class));
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User();
                newUser.setEmail(Client.getMyuser().getEmail());
                String temp;
                temp = editfname.getText().toString();
                if (temp.matches("")||temp.matches("Change"))
                    newUser.setFname(firstFname);
                else
                    newUser.setFname(editfname.getText().toString());

                temp = editlname.getText().toString();
                if (temp.matches("")||temp.matches("Change"))
                    newUser.setLname(firstLname);
                else
                    newUser.setLname(editlname.getText().toString());

                temp = editheight.getText().toString();
                if (temp.matches("")||temp.matches("Change"))
                    newUser.setHeight(firstheight);
                else
                    newUser.setHeight(editheight.getText().toString());

                temp = editweight.getText().toString();
                if (temp.matches("")||temp.matches("Change"))
                    newUser.setWeight(firstweight);
                else
                    newUser.setWeight(editweight.getText().toString());

                temp = editcity.getText().toString();
                if (temp.matches("")||temp.matches("Change"))
                    newUser.setCity(firstcity);
                else
                newUser.setCity(editcity.getText().toString());

                temp = editaddress.getText().toString();
                if (temp.matches("")||temp.matches("Change"))
                    newUser.setAddress(firstadrress);
                else
                newUser.setAddress(editaddress.getText().toString());


                newUser.setAdmin(Client.getMyuser().getAdmin());

                if (editphone.getText().toString().length() < 9) {
                    newUser.setPhone(realphone);
                    if(!editphone.getText().toString().matches("")||!editphone.getText().toString().matches("Change"))
                    Toast.makeText(Update.this, "phone number is invalid", Toast.LENGTH_SHORT).show();
                } else newUser.setPhone(editphone.getText().toString());
                if (rdg.getCheckedRadioButtonId() == rdbmale.getId())
                    newUser.setGender("Male");
                else newUser.setGender("Female");
                if(rebirth)
                {
                    char[] TEMP = editbirthday.getText().toString().toCharArray();
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
                    myDate redate = new myDate(year,month,day,0,0);
                    newUser.setBirthday(redate);
                }
                else
                newUser.setBirthday(Client.getMyuser().getBirthday());
                if (choosen) {
                    AddPicture();
                    newUser.setPic(picname);

                } else
                    newUser.setPic(profile);

                Taskreference = db.getReference("User").child(Client.getUid()).setValue(newUser);
                Client.setMyuser(newUser);
                startActivity(new Intent(Update.this,MainHub.class));
            }
        });


    }
}
