package com.example.boomplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boomplayer.util.LogHelper;
import com.example.boomplayer.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {
    private String TAG = "MusicPlayActivity" ;

    //界面上的控件对象
    private ListView musicListView ;
    public SeekBar playProgress ;
    private TextView musicInfoText ;
    private Button lastMusicBtn ;
    private Button pausePlayBtn ;
    private Button nextMusicBtn ;
    private TextView currentTimeText;
    private TextView totalTimeText;
    //绑定服务的对象
    public MusicService.MyBinder mBinder ;
    //当前正在播放的音乐
    private MusicMedia currentMusic ;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //更新进度条UI和播放时间
            int currentPosition = mBinder.getCurrentPosition();
            playProgress.setProgress(currentPosition);
            String currentTime = MusicUtil.formatMusicDuration(currentPosition);
            currentTimeText.setText(currentTime);
        }
    };

    //绑定服务的连接对象
    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 存储音乐的集合
     */
    public List<MusicMedia> musicList ;
    /**
     * ListView的item点击监听对象
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //点击列表时 音乐播放停止
            mBinder.stopPlay();
            //设置当前播放的音乐
            currentMusic = musicList.get(position);
            //获取被点击的item的音乐路径
            String url = currentMusic.getUrl();
            //调用方法将音乐资源设置给播放器对象
            mBinder.setMusicData(url);
            //开始播放
            mBinder.startPlay();
            //更新UI
            updateUI();
            //每一秒更新一次SeekBar进度
            handler.postDelayed(runnable,1000);
        }
    };
    /**
     * SeekBar的滑动进度条监听对象
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = playProgress.getProgress();
            mBinder.seekTo(progress);
            Toast.makeText(MusicPlayerActivity.this,"当前进度为："+progress,Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 通用View的点击监听对象
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(musicList.isEmpty()){
                Toast.makeText(MusicPlayerActivity.this,"音乐列表为空",Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()){
                case R.id.last_music:
                    //停止播放之前音乐
                    mBinder.stopPlay();
                    //获取之前音乐的位置
                    int beforeMusicPosition = musicList.indexOf(currentMusic);
                    //如果之前播放的是第一首歌  则播放最后一首歌
                    if(beforeMusicPosition == 0){
                        currentMusic = musicList.get(musicList.size()-1);
                    }else {
                        currentMusic = musicList.get(beforeMusicPosition - 1);
                    }
                    //设置播放内容并开始播放
                    mBinder.setMusicData(currentMusic.getUrl());
                    mBinder.startPlay();
                    //更新UI
                    updateUI();
                    //每一秒更新一次SeekBar进度
                    handler.postDelayed(runnable,1000);
                    Toast.makeText(MusicPlayerActivity.this,"上一首",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pause_play:
                    //判断是否正在播放
                    if(mBinder.isPlaying()){
                        //若正在播放  则暂停
                        mBinder.pausePlay();
                        //改变ui
                        pausePlayBtn.setText("播放");
                        Toast.makeText(MusicPlayerActivity.this,"暂停",Toast.LENGTH_SHORT).show();

                    }else {
                        //反之  则开始播放
                        mBinder.startPlay();
                        pausePlayBtn.setText("暂停");
                        Toast.makeText(MusicPlayerActivity.this,"播放",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.next_music:
                    //停止播放
                    mBinder.stopPlay();
                    //获取之前播放的音乐位置
                    int p = musicList.indexOf(currentMusic);
                    if(p == (musicList.size()-1)){
                        currentMusic = musicList.get(0);
                    }else {
                        currentMusic = musicList.get(p + 1);
                    }
                    //播放音乐
                    mBinder.setMusicData(currentMusic.getUrl());
                    mBinder.startPlay();
                    //更新UI
                    updateUI();
                    //每一秒更新一次SeekBar进度
                    handler.postDelayed(runnable,1000);
                    Toast.makeText(MusicPlayerActivity.this,"下一首",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.e(TAG,getClass().getName()+"---onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        //初始化音乐对象
        currentMusic = new MusicMedia();
        //视图初始化
        initViews();
        //遍历获取所有音频文件
        musicList = MusicUtil.scanAllAudioFiles(this);
        //ListView 设置适配器
        musicListView.setAdapter(new MusicAdapter(this,musicList));
        //启动和绑定服务
        sabService();
        //将音乐播放的音乐初始位置设置为列表中第一首
        initFirstMusic();
    }

    /**
     * 初始化视图对象并设置相应监听
     */
    public void initViews(){
        LogHelper.e(TAG,getClass().getName()+"---initViews");
        musicListView = findViewById(R.id.music_list);
        playProgress = findViewById(R.id.play_progress);
        musicInfoText = findViewById(R.id.music_info);
        lastMusicBtn = findViewById(R.id.last_music);
        pausePlayBtn = findViewById(R.id.pause_play);
        nextMusicBtn = findViewById(R.id.next_music);
        currentTimeText = findViewById(R.id.current_time);
        totalTimeText = findViewById(R.id.total_time);

        musicListView.setOnItemClickListener(onItemClickListener);
        playProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
        lastMusicBtn.setOnClickListener(onClickListener);
        pausePlayBtn.setOnClickListener(onClickListener);
        nextMusicBtn.setOnClickListener(onClickListener);

        //初始化musicList
        musicList = new ArrayList<MusicMedia>();
    }

    /**
     * 启动和绑定服务
     */
    public void sabService(){
        LogHelper.e(TAG,getClass().getName()+"---sabService");
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    /**
     * 播放另一首音乐时要更新的UI
     */
    public void updateUI(){
        String info = currentMusic.getTitle()+"-"+currentMusic.getArtist();
        musicInfoText.setText(info);
        pausePlayBtn.setText("暂停");
        playProgress.setMax(mBinder.getDuration());
        totalTimeText.setText(MusicUtil.formatMusicDuration(mBinder.getDuration()));
        currentTimeText.setText(MusicUtil.formatMusicDuration(mBinder.getCurrentPosition()));
    }

    /**
     * 打开应用后的默认音乐为列表中的第一首音乐
     */
    public void initFirstMusic(){
        if(musicList.isEmpty()){
            Toast.makeText(MusicPlayerActivity.this,"音乐列表为空",Toast.LENGTH_SHORT).show();
            return;
        }
        currentMusic = musicList.get(0);
        mBinder.setMusicData(currentMusic.getUrl());
        musicInfoText.setText(currentMusic.getTitle()+"-"+currentMusic.getArtist());
        playProgress.setMax(mBinder.getDuration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
