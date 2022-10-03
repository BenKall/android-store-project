package com.example.palacio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private Button reg,log;

    private VideoView videoview;
    MediaPlayer mMediaplayer;
    int mCurrentVideoPosition;
    //This is my final project

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reg = findViewById(R.id.btnreg);

        log = findViewById(R.id.btnlog);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });

        videoview = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.alhsonmoving);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaplayer = mp;

                mMediaplayer.setLooping(true);
                if(mCurrentVideoPosition!=0)
                    mMediaplayer.seekTo(mCurrentVideoPosition);
                mMediaplayer.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentVideoPosition = mMediaplayer.getCurrentPosition();
        videoview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoview.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaplayer.release();
        mMediaplayer = null;
    }
}
