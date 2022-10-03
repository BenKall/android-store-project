package com.example.palacio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class shop extends AppCompatActivity {

    private Button toallpro, matt, chairs, sofas, tables, btnToCurOrders, recommended, btnBackToMainHub, ADMINAmountFix;
    public static Orders curOrders;
    public static Boolean FirstTime = true;
    private String isCharged = "false";

    public void getORDER()
    {
        if(FirstTime)
        {
            curOrders = new Orders();
            curOrders.setOrderUser(Client.getMyuser().getEmail());
            long orderCode = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
            curOrders.setOrderCode(orderCode);
            FirstTime = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getORDER();

        //Toast.makeText(this, String.valueOf(shop.curOrders.getOrderCode()), Toast.LENGTH_SHORT).show();

        toallpro=findViewById(R.id.toallpro);
        matt = findViewById(R.id.matt);
        chairs = findViewById(R.id.chairs);
        sofas = findViewById(R.id.sofas);
        tables = findViewById(R.id.tables);
        btnToCurOrders = findViewById(R.id.btntocurorders);
        recommended = findViewById(R.id.recommended);
        btnBackToMainHub = findViewById(R.id.btnbacktomainhub);
        ADMINAmountFix = findViewById(R.id.ADMINamountfix);

        toallpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,ShowAllProducts.class));
            }
        });
        btnBackToMainHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,MainHub.class));
            }
        });

        sofas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,ShowSofaProducts.class));
            }
        });
        recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,angleCheck.class));
            }
        });
        tables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,ShowTableProducts.class));
            }
        });
        chairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,ShowChairProducts.class));
            }
        });
        matt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,ShowMattressProducts.class));
            }
        });
        btnToCurOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,Cart.class));
            }
        });
        if(Client.getMyuser().getAdmin())
            ADMINAmountFix.setVisibility(View.VISIBLE);
        ADMINAmountFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop.this,ShowAllZeroAmountProductsForAdmin.class));
            }
        });


        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);


        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if(isCharging)
            isCharged="true";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, isCharged, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, "Sub Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this, "Sub Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}