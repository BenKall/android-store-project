package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class userCheckProduct extends AppCompatActivity {

    public static final String line = "line";
    public static final int priCode = 0;
    private int currentCode = 0;
    private ListView piclist;
    private ProgressDialog chosenproprg =null;
    private static ArrayList<String> arrNumbers = new ArrayList<>();
    private static ArrayList<String> arrMSG = new ArrayList<>();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference proStockRef =db.getReference("Stock");
    private DatabaseReference proStockPicRef =db.getReference("Stock Pics");
    private DatabaseReference orderDetsRef = db.getReference("Order Details");
    private String mypath = "gs://final-project-9dd22.appspot.com";
    private TextView chosenproamount, chosenproprice, chosenproname, chosenprotype, chosenprodes;
    private Button btnBuyProduct, btnBackToShop, btnChooseAmount;
    private ImageButton btnAmountLeft, btnAmountRight;
    private static ArrayList<String> arrChosenProPics = new ArrayList<>();
    private int arrSize=0;
    private int count = 1;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String orderDate, amountStr;
    private int amountOfUnits = 1, amountInt = 0;
    private String curPrice = "";

    public static void delete()
    {
        while (arrChosenProPics.size()>0)
        {
            arrChosenProPics.clear();
            arrNumbers.clear();
            arrMSG.clear();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_product);
        delete();

        Intent intent = getIntent();
        currentCode = getIntent().getIntExtra(line,priCode);// The current product's code

        piclist = findViewById(R.id.piclist);
        chosenproamount = findViewById(R.id.chosenproamount);
        chosenprodes = findViewById(R.id.chosenprodes);
        chosenproname = findViewById(R.id.chosenproname);
        chosenproprice = findViewById(R.id.chosenproprice);
        chosenprotype = findViewById(R.id.chosenprotype);

        chosenproprg=new ProgressDialog(userCheckProduct.this);
        chosenproprg.setTitle(" ");
        chosenproprg.setMessage("Loading Product Details");
        chosenproprg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        chosenproprg.setCancelable(false);
        chosenproprg.setIcon(R.drawable.description);

        btnBuyProduct = findViewById(R.id.btnbuyproduct);
        btnBackToShop = findViewById(R.id.btnbacktoshop);
        btnAmountLeft = findViewById(R.id.btnamountleft);
        btnAmountRight = findViewById(R.id.btnamountright);
        btnChooseAmount = findViewById(R.id.btnchooseamount);
        btnChooseAmount.setText(String.valueOf(amountOfUnits));


        btnAmountLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountOfUnits==1)
                    Toast.makeText(userCheckProduct.this, "You can't order less then 1", Toast.LENGTH_SHORT).show();
                else
                {
                    amountOfUnits--;
                    btnChooseAmount.setText(String.valueOf(amountOfUnits));
                }

            }
        });
        btnAmountRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountInt != amountOfUnits) {
                    amountOfUnits++;
                    btnChooseAmount.setText(String.valueOf(amountOfUnits));
                }
                else
                    Toast.makeText(userCheckProduct.this, "You can't order more then the amount displayed above", Toast.LENGTH_SHORT).show();

            }
        });
        btnChooseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(userCheckProduct.this, amountOfUnits, Toast.LENGTH_SHORT).show();
            }
        });
        btnBackToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userCheckProduct.this,shop.class));
            }
        });

        proStockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stock This = child.getValue(Stock.class);
                    if(This.getProductcode()==currentCode) {
                        chosenproname.setText(This.getProductname());
                        chosenprodes.setText(This.getProductdescription());
                        chosenproamount.setText(This.getAmount());
                        chosenproprice.setText(This.getPrice());
                        chosenprotype.setText(This.getProducttype());
                        curPrice = This.getPrice();
                        amountStr = This.getAmount();
                        amountInt = parseInt(amountStr);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        proStockPicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    Stockpic This = child.getValue(Stockpic.class);
                    if(This.getProductcode()==currentCode) {
                        arrChosenProPics.add(This.getPicname());
                        arrSize++;
                        arrNumbers.add(Integer.toString(count));
                        arrMSG.add(" ");
                        count++;
                    }
                }

                cardAdapter cardAdapter = new cardAdapter(arrChosenProPics,arrNumbers,arrMSG,chosenproprg,userCheckProduct.this);
                piclist.setAdapter(cardAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderDetails neworder = new OrderDetails();
                neworder.setOrderDetsCode(shop.curOrders.getOrderCode());
                neworder.setOrderDetsProductCode(currentCode);
                neworder.setOrderDetsProAmount(String.valueOf(amountOfUnits));
                neworder.setOrderDetsProPPU(String.valueOf(curPrice));

                DatabaseReference newref = orderDetsRef.push();
                Task<Void> rafi = newref.setValue(neworder);//adds to Firebase
                startActivity(new Intent(userCheckProduct.this,shop.class));

            }
        });



    }
}