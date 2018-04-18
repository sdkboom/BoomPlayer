package com.example.okplayer;

import android.media.MediaPlayer;
import android.net.ProxyInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button lastMusic ;
    private Button nextMusic ;
    private Button playPause ;
    private SeekBar musicProgress ;
    private TextView currentTime ;
    private TextView totalTime ;
    private MediaPlayer player;
    private boolean hadDestroy = false;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo(seekBar.getProgress());
        }
    };

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                default:
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!hadDestroy){
                handler.postDelayed(this,1000);
                musicProgress.setProgress(player.getCurrentPosition());
                int current = Math.round(player.getCurrentPosition()/1000);
                String strCurrent = String.format("%02d:%02d",current/60,current%60);
                currentTime.setText(strCurrent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initMediaPlayer();
    }

    public void initViews(){
        //初始化视图对象
        lastMusic = findViewById(R.id.last_music);
        nextMusic = findViewById(R.id.next_music);
        playPause = findViewById(R.id.play_pause);
        musicProgress = findViewById(R.id.progress);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        //设置监听
        lastMusic.setOnClickListener(this);
        nextMusic.setOnClickListener(this);
        playPause.setOnClickListener(this);

        musicProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public void initMediaPlayer(){
        player = MediaPlayer.create(this,R.raw.demo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last_music:

                break;
            case R.id.play_pause:
                if(!player.isPlaying()){
                    player.start();
                    musicProgress.setMax(player.getDuration());
                    int total = Math.round(player.getDuration()/1000);
                    String strTotal = String.format("%02d:%02d",total/60,total%60);
                    totalTime.setText(strTotal);
                    handler.postDelayed(runnable,1000);
                }else {
                    player.pause();
                }
                break;
            case R.id.next_music:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hadDestroy = true;
        player.release();
    }
}
