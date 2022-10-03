package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class addproduct extends AppCompatActivity {
    private Button backToAdmin,btnpickpic,addproduct;
    private EditText proname,prodescription,proprice,proamount;
    private ImageView proimg;
    private Spinner protype;
    //----------------------------------------
    private String Productcode;
    private String Productname;
    private String Producttype;
    private String Productdescription;
    private String Price;
    private String Amount;
    private String proimage;
    private String productAngleType;
    private String productHeightType;
    private String productWeightType;
    private ListView listView;
    private RadioGroup rdgA, rdgW, rdgH;
    private RadioButton rdbAa, rdbAb, rdbAc, rdbWa, rdbWb, rdbWc, rdbHa, rdbHb, rdbHc;
    private ProgressDialog proprg=null;
    private static ArrayList<Integer> arrpics = new ArrayList<>();
    private static ArrayList<String> arrnumbers = new ArrayList<>();
    private static ArrayList<Uri> arrdata = new ArrayList<>();
    private static ArrayList<String> arrdataepic = new ArrayList<>();
    private static ArrayList<String> arrayListprotypes = new ArrayList<>();
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    //----------------------------------------
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = null;
    private FirebaseAuth auth=null;
    private DatabaseReference stockref = db.getReference("Stock");
    private DatabaseReference stockpicref = db.getReference("Stock Pics");
    private DatabaseReference prodnumref = db.getReference("prodnum");
    private DatabaseReference typeref = db.getReference("Product Type");
    //----------------------------------------
    private static Uri selectedImage = null;
    private static String picname = "";
    private static boolean choosen=false;
    //----------------------------------------
    private int count = 1;
    ///////

    private void AddPicture(Uri pic,String picepic)
    {
        StorageReference mypic_storage=storageReference.child("stockpics/" + picepic);

        mypic_storage.putFile(pic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(addproduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void delete()
    {
        while (arrdataepic.size()>0)
            arrdataepic.remove(0);
        while (arrdata.size()>0)
            arrdata.remove(0);
        while (arrnumbers.size()>0)
            arrnumbers.remove(0);
        while (arrayListprotypes.size()>0)
            arrayListprotypes.remove(0);
        while (arrpics.size()>0)
            arrpics.remove(0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==666 && data!=null)
        {
            choosen=true;

            selectedImage=data.getData();

            arrdata.add(data.getData());

            arrnumbers.add(Integer.toString(count));
            count++;

            //proimg.setImageURI(selectedImage);

            String[] filepathcolumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedImage,filepathcolumn,null,null,null);
            cursor.moveToFirst();
            int columnindex=cursor.getColumnIndex(filepathcolumn[0]);

            picname=cursor.getString(columnindex);

            picname=picname.substring(picname.lastIndexOf("/")+1);

            cardAdapter2 cardAdapter = new cardAdapter2(arrdata,arrnumbers,addproduct.this);
            listView.setAdapter(cardAdapter);

            arrdataepic.add(picname);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        delete();
        backToAdmin = findViewById(R.id.btnbacktoadmin);
        btnpickpic = findViewById(R.id.btnpickpic);
        addproduct = findViewById(R.id.addproduct);
        proamount = findViewById(R.id.proamount);
        proname = findViewById(R.id.proname);
        prodescription = findViewById(R.id.prodescription);
        proprice = findViewById(R.id.proprice);
        protype = findViewById(R.id.protype);
        listView = findViewById(R.id.lst);

        rdbAa = findViewById(R.id.rdbAa);
        rdbAb = findViewById(R.id.rdbAb);
        rdbAc = findViewById(R.id.rdbAc);
        rdbWa = findViewById(R.id.rdbWa);
        rdbWb = findViewById(R.id.rdbWb);
        rdbWc = findViewById(R.id.rdbWc);
        rdbHa = findViewById(R.id.rdbHa);
        rdbHb = findViewById(R.id.rdbHb);
        rdbHc = findViewById(R.id.rdbHc);
        rdgA = findViewById(R.id.rdgA);
        rdgW = findViewById(R.id.rdgW);
        rdgH = findViewById(R.id.rdgH);

        proprg=new ProgressDialog(addproduct.this);
        proprg.setTitle("New Product");
        proprg.setMessage("Saving Product Details");
        proprg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proprg.setCancelable(false);
        proprg.setIcon(R.drawable.description);

        productAngleType = "A";
        productHeightType = "A";
        productWeightType = "A";

        rdgA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbAa:
                        productAngleType = "A";
                        break;
                    case R.id.rdbAb:
                        productAngleType = "B";
                        break;
                    case R.id.rdbAc:
                        productAngleType = "C";
                        break;
                }
            }
        });

        rdgW.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbWa:
                        productWeightType = "A";
                        break;
                    case R.id.rdbWb:
                        productWeightType = "B";
                        break;
                    case R.id.rdbWc:
                        productWeightType = "C";
                        break;
                }
            }
        });

        rdgH.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbHa:
                        productHeightType = "A";
                        break;
                    case R.id.rdbHb:
                        productHeightType = "B";
                        break;
                    case R.id.rdbHc:
                        productHeightType = "C";
                        break;
                }
            }
        });

        final Permissions p = new Permissions(addproduct.this);
        p.verifyPermissions();


        backToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addproduct.this,admin.class));
            }
        });

        btnpickpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==6)
                    Toast.makeText(addproduct.this, "You have reached the limit", Toast.LENGTH_SHORT).show();
                else {
                    Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(it, 666);
                }
            }
        });

        typeref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    String City = child.getValue(String.class);
                    arrayListprotypes.add(City);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(addproduct.this, R.layout.customspinner, arrayListprotypes);
                protype.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Productname = proname.getText().toString();
                Producttype = protype.getSelectedItem().toString();
                Productdescription = prodescription.getText().toString();
                Price = proprice.getText().toString();
                Amount = proamount.getText().toString();

                if(Productname.equals("") ||Productdescription.equals("") ||Price.equals("") ||Amount.equals("") )
                    Toast.makeText(addproduct.this, "You are missing some information", Toast.LENGTH_SHORT).show();
                else {


                    prodnumref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int post = dataSnapshot.getValue(int.class);

                            Stock St = new Stock(post, Productname, Producttype, Productdescription, Price, Amount, productAngleType, productHeightType, productWeightType);

                            DatabaseReference newref = stockref.push();
                            Task<Void> rafi = newref.setValue(St);//adds to Firebase

                            Task<Void> rafiel;
                            for (int i = 0; i < count-1; i++) {

                                Stockpic Sp = new Stockpic();
                                Sp.setPicname(arrdataepic.get(i));
                                Sp.setProductcode(post);

                                newref = stockpicref.push();
                                rafiel = newref.setValue(Sp);//adds to Firebase

                                AddPicture(arrdata.get(i),arrdataepic.get(i));
                            }

                            prodnumref.setValue(post+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(addproduct.this, "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    proprg.dismiss();

                    startActivity(new Intent(addproduct.this, MainHub.class));
                }
            }
        });


    }
}