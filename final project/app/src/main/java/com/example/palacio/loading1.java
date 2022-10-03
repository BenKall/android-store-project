package com.example.palacio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class loading1 extends AppCompatActivity {
    private Animation Animation;
    private ImageView img;

    private void MoveImage()
    {
        Animation = AnimationUtils.loadAnimation(loading1.this,R.anim.goup);
        img.startAnimation(Animation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading1);

        img = findViewById(R.id.imganim);

        new Thread(new Runnable()
        {
         public void run()
         {

         try
          {
             MoveImage();
             Thread.sleep(3000);
         } catch (InterruptedException e)
         {
            e.printStackTrace();
        }
        startActivity(new Intent(loading1.this,MainHub.class));
         finish();
         }
          }).start();
    }
}