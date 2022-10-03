package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class makeDelivery extends AppCompatActivity {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String orderDate;
    private CalendarView calendarView;
    private Button btnPur;
    private int count = 0;
    private TextView txtOrderInfo;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ordersRef=db.getReference("Orders");
    private DatabaseReference orderDetailsRef=db.getReference("Order Details");
    private DatabaseReference DeliveryRef=db.getReference("Delivery");
    private DatabaseReference stockRef = db.getReference("Stock");
    private Task<Void> Taskreference = null;
    private static int sum = 0, itemcount = 0;
    private static ArrayList<Integer> arrproCODE = new ArrayList<>();
    private static ArrayList<String> arrproAmount = new ArrayList<>();
    private Button btnToast, btnCharNum;
    private int num = 0;
    private myDate DeliveryDate = new myDate();

    public static void delete()
    {
        while (arrproAmount.size()>0)
        {
            arrproAmount.clear();
            arrproCODE.clear();

        }
    }

    private void updateNewProductAmount() {
        for (int i = 0; i < arrproCODE.size(); i++) {
            final int currentProCode = arrproCODE.get(i);
            final String amountToSubtract = arrproAmount.get(i);
            stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Stock newStock = new Stock();
                    for (DataSnapshot child : children) {
                        Stock me = child.getValue(Stock.class);
                        if (currentProCode == (me.getProductcode())) {
                            newStock.setaType(me.getaType());
                            newStock.sethType(me.gethType());
                            newStock.setwType(me.getwType());
                            newStock.setProductdescription(me.getProductdescription());
                            newStock.setProductname(me.getProductname());
                            newStock.setPrice(me.getPrice());
                            newStock.setProductcode(me.getProductcode());
                            newStock.setProducttype(me.getProducttype());
                            int amountInt = parseInt(me.getAmount());
                            int sub = parseInt(amountToSubtract);
                            newStock.setAmount(String.valueOf(amountInt - sub));
                            Taskreference = db.getReference("Stock").child(child.getKey()).setValue(newStock);
                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        delete();

    }

    public myDate getDateNHour(int x)
    {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");//07-10-1996 12:08:56
        orderDate = dateFormat.format(calendar.getTime());
        char[] orderDateChar = orderDate.toCharArray();
        return arrToDate(orderDateChar,x);


    }

    public myDate arrToDate(char[] givenDate, int x)
    {
        String year = "", month = "", day = "", hour = "", minute = "";
        year += givenDate[6];
        year +=givenDate[7];
        year +=givenDate[8];
        year +=givenDate[9];
        month +=givenDate[0];
        month +=givenDate[1];
        day+= givenDate[3];
        day+= givenDate[4];
        hour+= givenDate[11];
        hour+= givenDate[12];
        minute += givenDate[14];
        minute += givenDate[15];
        int yearNum = 0, monthNum = 0, dayNum = 0, hourNum = 0, minuteNum = 0;
        yearNum = parseInt(year);
        monthNum = parseInt(month);
        dayNum = parseInt(day);
        myDate Date = new myDate(yearNum,monthNum,dayNum,0,0);
        myDate Hour = new myDate(0,0,0, parseInt(hour), parseInt(minute));
        if(x == 0)
        return Date;
        else
            return Hour;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_delivery);

        txtOrderInfo = findViewById(R.id.txtorderinfo);
        btnPur = findViewById(R.id.btnpur);


        calendarView = findViewById(R.id.calendarView);

                Calendar maxcalendar = Calendar.getInstance();
                maxcalendar.setTime(new Date());
                maxcalendar.add(Calendar.MONTH, 2);

                Calendar mincalendar = Calendar.getInstance();
                mincalendar.setTime(new Date());
                mincalendar.add(Calendar.DATE, 3);

                calendarView.setMinDate(mincalendar.getTime().getTime());
                calendarView.setMaxDate(maxcalendar.getTime().getTime());
        int currentYear = mincalendar.get(Calendar.YEAR);
        int currentMonth = mincalendar.get(Calendar.MONTH) + 1;
        int currentDay = mincalendar.get(Calendar.DAY_OF_MONTH);

        DeliveryDate.setYear(currentYear);
        DeliveryDate.setDay(currentDay);
        DeliveryDate.setMonth(currentMonth);
        DeliveryDate.setHour(0);
        DeliveryDate.setMinute(0);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                DeliveryDate.setYear(year);
                DeliveryDate.setMonth(month+1);
                DeliveryDate.setDay(dayOfMonth);
                DeliveryDate.setHour(0);
                DeliveryDate.setMinute(0);
            }
        });

        orderDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    OrderDetails me = child.getValue(OrderDetails.class);
                    if (shop.curOrders.getOrderCode()==child.getValue(OrderDetails.class).getOrderDetsCode())
                    {
                        sum += parseInt(me.getOrderDetsProAmount())* parseInt(me.getOrderDetsProPPU());
                        count += parseInt(me.getOrderDetsProAmount());
                        arrproCODE.add(me.getOrderDetsProductCode());
                        arrproAmount.add(me.getOrderDetsProAmount());
                    }
                }
                txtOrderInfo.setText("You are buying "+count+" items for "+sum+" Shekels");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        btnPur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateNewProductAmount();



                Orders newOrder = new Orders();
                newOrder.setOrderCode(shop.curOrders.getOrderCode());
                newOrder.setOrderUser(shop.curOrders.getOrderUser());
                newOrder.setOrderAmount(String.valueOf(count));


                newOrder.setOrderDate(getDateNHour(0));
                newOrder.setOrderHour(getDateNHour(1));
                shop.curOrders = null;
                shop.FirstTime = true;
                DatabaseReference newref = ordersRef.push();
                Task<Void> rafi = newref.setValue(newOrder);//adds to Firebase



                Delivery newDelivery = new Delivery();
                newDelivery.setOrderCode(newOrder.getOrderCode());
                newDelivery.setOrderUser(newOrder.getOrderUser());
                final long fileName = System.currentTimeMillis();
                newDelivery.setDeliveryCode(fileName);

                final myDate DeliveryDateFinal = DeliveryDate;
                
                //newDelivery.setDeliveryDate(arrToDate(DeliveryFullDateCharArr,0));
                newDelivery.setDeliveryDate(DeliveryDateFinal);
                newDelivery.setStatus("No");
                newDelivery.setUserOrderAddress(Client.getMyuser().getAddress());

                newref = DeliveryRef.push();
                Task<Void> rafiel = newref.setValue(newDelivery);//adds to Firebase
                Toast.makeText(makeDelivery.this, "Your order will be delivered to you soon!", Toast.LENGTH_SHORT).show();



                startActivity(new Intent(makeDelivery.this,MainHub.class));
            }
        });

    }
}