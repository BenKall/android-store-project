package com.example.palacio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class angleCheck extends AppCompatActivity {
    private TextView txtExplenation;
    private Button btnMeasure, btnToRecomm, backToShopFromAngleCheck;
    private Gyroscope gyroscope;
    private boolean start = false;
    private boolean startPoint = true;
    private String myAngle = "";
    private float num1;

    public static int abs(int a) {
        if(a>0)
            return a;
        if(a<0)
            return -a;
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle_check);

        gyroscope = new Gyroscope(this);
        txtExplenation = findViewById(R.id.txtexplenation);
        btnMeasure = findViewById(R.id.btnmeasure);
        btnToRecomm = findViewById(R.id.btntorecomm);
        backToShopFromAngleCheck = findViewById(R.id.backtoshopfromanglecheck);
        backToShopFromAngleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(angleCheck.this,shop.class));
            }
        });

        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start)
                    start = false;
                else
                    start = true;

                gyroscope.setListener(new Gyroscope.Listener() {
                    ;
                    @Override
                    public void onRotation(float rx, float ry, float rz) {
                        if(start)
                        {
                            if(startPoint)
                            {
                                num1 = ry;
                                startPoint = false;
                            }
                            btnMeasure.setText(String.valueOf(abs(abs((int)ry)-abs((int)num1))));
                        }
                        else if(!startPoint)
                        {
                            myAngle = String.valueOf(abs(abs((int)ry)-abs((int)num1)));
                            startPoint = true;
                        }
                    }
                });
            }
        });

        btnToRecomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myAngle.equals(""))
                {
                    Intent it = new Intent(angleCheck.this,ShowRecommendedProducts.class);
                    //it.putExtra(userCheckProduct.line, myAngle);
                    ShowRecommendedProducts.currentAngle = Integer.parseInt(myAngle);
                    startActivity(it);
                }
                else
                    Toast.makeText(angleCheck.this, "Please check angle", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        gyroscope.register();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gyroscope.unregister();
    }
}