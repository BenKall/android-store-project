package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class updateProduct extends AppCompatActivity {

    //----------------------------------------
    private TextView txtCurProName, txtCurProDes, txtCurProType, txtCurProAmount, txtCurProPrice;
    private EditText editcurproname,editcurprodes,editcurproamount,editcurproprice;
    private Spinner editCurProType;
    private ImageButton imgBtnRight, imgBtnLeft,curImgDisplay;
    private Button btnUpPro, btnAdd, btnSave;
    private ProgressDialog prgUpPro;
    private String curKey;
    private RadioGroup rdgA, rdgW, rdgH;
    private RadioButton rdbAa, rdbAb, rdbAc, rdbWa, rdbWb, rdbWc, rdbHa, rdbHb, rdbHc;

    //----------------------------------------
    private String curProName;
    private String curProDes;
    private String curProType;
    private String curProAmount;
    private String curProPrice;
    private String curProAType;
    private String curProHType;
    private String curProWType;
    private String productAngleType = "A";
    private String productHeightType = "A";
    private String productWeightType = "A";
    //----------------------------------------
    private static ArrayList<String> arrCurProPics = new ArrayList<>();
    private static ArrayList<String> arrProPicKeys = new ArrayList<>();
    private ArrayList<String> arrayListTypes = new ArrayList<String>();
    private ArrayList<Boolean> arrFlags = new ArrayList<Boolean>();
    private ArrayList<Uri> pictures = new ArrayList<>();

    //----------------------------------------
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference stockRef = db.getReference("Stock");
    private DatabaseReference stockPicRef = db.getReference("Stock Pics");
    private DatabaseReference typeReference = db.getReference("Product Type");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    //----------------------------------------
    private int arrSize=0;
    private int amountt=0;
    private int currentCode;
    public static final String line = "line";
    public static final int priCode = 0;
    private String mypath = "gs://final-project-9dd22.appspot.com";
    private Boolean isZero = true;
    private int index = 1;
    private static String picname = "";
    private static Uri selectedImage = null;
    private static boolean choosen = false;
    private boolean flag0 = false, flag1 = false, flag2 = false, flag3 = false, flag4 = false;
   private Task<Void> rafiel;
   private Task<Void> rafi;
    ////

    public void update()
    {
        if(!editcurproname.getText().toString().equals("")&&!editcurprodes.getText().toString().equals("")&&!editcurproamount.getText().toString().equals("")&&!editcurproprice.getText().toString().equals("")&&choosen){
            final int code = currentCode;//My code is int
            final String name = editcurproname.getText().toString();
            final String description = editcurprodes.getText().toString();
            final String type = editCurProType.getSelectedItem().toString();
            final String price = editcurproprice.getText().toString();
            final String amount = editcurproamount.getText().toString();
            final String aType = productAngleType;
            final String hType = productHeightType;
            final String wType = productWeightType;


            if(amount.equals("0"))
            {
                stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        String key = "";
                        for (DataSnapshot child : children) {
                            Stock st = child.getValue(Stock.class);
                            if(st.getProductcode()==currentCode)
                                key = child.getKey();
                        }
                        rafi =db.getReference("Stock").child(key).setValue(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                stockPicRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> arrkeys = new ArrayList<>();
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child: children)
                        {
                            Stockpic Sp = child.getValue(Stockpic.class);
                            if (Sp.getProductcode()==currentCode)
                                rafiel = db.getReference("Stock Pics").child(child.getKey()).setValue(null);
                        }
                        startActivity(new Intent(updateProduct.this, ShowAllProducts.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else {

                stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Stock newStock = new Stock();
                        newStock.setProductcode(code);
                        newStock.setProductname(name);
                        newStock.setProductdescription(description);
                        newStock.setProducttype(type);
                        newStock.setPrice(price);
                        newStock.setAmount(amount);
                        newStock.setaType(aType);
                        newStock.sethType(hType);
                        newStock.setwType(wType);


                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        String key = "";
                        for (DataSnapshot child : children) {
                            Stock st = child.getValue(Stock.class);
                            if (st.getProductcode()==currentCode)
                                key = child.getKey();
                        }
                        rafi = db.getReference("Stock").child(key).setValue(newStock);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                stockPicRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            Stockpic Sp = child.getValue(Stockpic.class);
                            if (Sp.getProductcode()==currentCode)
                                rafiel = db.getReference("Stock Pics").child(child.getKey()).setValue(null); //problem may be here!!!!!!!!
                        }

                        for (int i = 0; i < amountt; i++) {
                            Stockpic newSp = new Stockpic();
                            newSp.setProductcode(currentCode);
                            newSp.setPicname(arrCurProPics.get(i));
                            DatabaseReference newref2 = stockPicRef.push();
                            rafiel = newref2.setValue(newSp);
                            AddPicture(pictures.get(i), arrCurProPics.get(i));
                        }
                        startActivity(new Intent(updateProduct.this, ShowAllProducts.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else {
            Toast.makeText(this, "please fill in all of the information", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddPicture(Uri pic, String picname) {
        StorageReference mypic_storage = mStorageRef.child("stockpics/" + picname);

        mypic_storage.putFile(pic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(updateProduct.this, "Picture DataBase is Updated!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==666 && data!=null)
        {
            String picnane;

            Uri selectedImage=data.getData();
            curImgDisplay.setImageURI(selectedImage);

            pictures.add(selectedImage);
            amountt++;
            index = pictures.size();
            choosen = true;

            String[] filepathcolumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedImage,filepathcolumn,null,null,null);
            cursor.moveToFirst();
            int columnindex=cursor.getColumnIndex(filepathcolumn[0]);

            picnane=cursor.getString(columnindex);

            picnane=picnane.substring(picnane.lastIndexOf("/")+1);
            arrCurProPics.add(picnane);

            if(index > 1)
            {
                imgBtnLeft.setVisibility(View.VISIBLE);
                imgBtnRight.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        txtCurProName = findViewById(R.id.txtcurproname);
        txtCurProDes = findViewById(R.id.txtcurprodes);
        txtCurProType = findViewById(R.id.txtcurprotype);
        txtCurProAmount = findViewById(R.id.txtcurproamount);
        txtCurProPrice = findViewById(R.id.txtcurproprice);
        editcurproname = findViewById(R.id.editproname);
        editcurprodes = findViewById(R.id.editdes);
        editCurProType = findViewById(R.id.editcity);
        editcurproamount = findViewById(R.id.editproamount);
        editcurproprice = findViewById(R.id.editproprice);
        btnSave = findViewById(R.id.btnSave);
        btnUpPro = findViewById(R.id.btnuppro);
        for (int i = 0; i < 5; i++) {
            arrFlags.add(false);
        }
        prgUpPro=new ProgressDialog(updateProduct.this);
        prgUpPro.setTitle(" ");
        prgUpPro.setMessage("Loading Product Details");
        prgUpPro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgUpPro.setCancelable(false);
        prgUpPro.setIcon(R.drawable.description);
        btnAdd = findViewById(R.id.btnadd);

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

        imgBtnLeft = findViewById(R.id.imgbtnleft);
        imgBtnRight = findViewById(R.id.imgbtnright);
        curImgDisplay = findViewById(R.id.curimgdisplay);

        imgBtnLeft.setVisibility(View.INVISIBLE);
        imgBtnRight.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        currentCode = getIntent().getIntExtra(line,priCode);

        stockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock This = child.getValue(Stock.class);
                    if(This.getProductcode()==currentCode) {
                        curProName = This.getProductname();
                        curProDes = This.getProductdescription();
                        curProType = This.getProducttype();
                        curProAmount = This.getAmount();
                        curProPrice = This.getPrice();
                        curProAType = This.getaType();
                        curProHType = This.gethType();
                        curProWType = This.getwType();
                    }
                }
                txtCurProName.setText(curProName);
                txtCurProDes.setText(curProDes);
                txtCurProType.setText(curProType);
                txtCurProAmount.setText(curProAmount);
                txtCurProPrice.setText(curProPrice);

                switch(curProAType){
                    case "A":
                        rdbAa.setChecked(true);
                        break;
                    case "B":
                        rdbAb.setChecked(true);
                        break;
                    case "C":
                        rdbAc.setChecked(true);
                        break;
                }

                switch(curProHType){
                    case "A":
                        rdbHa.setChecked(true);
                        break;
                    case "B":
                        rdbHb.setChecked(true);
                        break;
                    case "C":
                        rdbHc.setChecked(true);
                        break;
                }

                switch(curProWType){
                    case "A":
                        rdbWa.setChecked(true);
                        break;
                    case "B":
                        rdbWb.setChecked(true);
                        break;
                    case "C":
                        rdbWc.setChecked(true);
                        break;
                }

                typeReference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child: children) {
                            String Type = child.getValue(String.class);
                            arrayListTypes.add(Type);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(updateProduct.this, R.layout.customspinner, arrayListTypes);
                        editCurProType.setAdapter(adapter);
                        for (int i = 0; i < adapter.getCount() ; i++) {
                            if(adapter.getItem(i).equals(curProType))
                                editCurProType.setSelection(i);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                curImgDisplay.setImageURI(pictures.get(index-1));
                if(index==1) {
                    imgBtnLeft.setVisibility(View.INVISIBLE);
                    imgBtnRight.setVisibility(View.VISIBLE);
                }
                else {
                    imgBtnLeft.setVisibility(View.VISIBLE);
                    imgBtnRight.setVisibility(View.VISIBLE);
                }
            }
        });

        imgBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                curImgDisplay.setImageURI(pictures.get(index - 1));
                if (index == pictures.size()){
                    imgBtnRight.setVisibility(View.INVISIBLE);
                    imgBtnLeft.setVisibility(View.VISIBLE);
                }
                else {
                    imgBtnLeft.setVisibility(View.VISIBLE);
                    imgBtnRight.setVisibility(View.VISIBLE);
                }
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amountt < 5)
                {
                    Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(it,666);
                }
                else
                {
                    Toast.makeText(updateProduct.this, "Maximum photo count reached.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

    }
}