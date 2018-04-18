package com.example.boomplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.boomplayer.util.LogHelper;

import org.w3c.dom.ProcessingInstruction;

import java.io.IOException;
import java.security.PublicKey;


public class MusicService extends Service {

    private MyBinder mBinder;
    private MediaPlayer player ;
    private String TAG = "MusicService";

    public MusicService() {
    }

    @Override
    public void onCreate() {
        LogHelper.e(TAG,getClass().getName()+"---onCreate");
        super.onCreate();
        player = new MediaPlayer();
        mBinder = new MyBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogHelper.e(TAG,getClass().getName()+"---onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogHelper.e(TAG,getClass().getName()+"---onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务与活动绑定后的绑定类
     */
    public class MyBinder extends Binder {
        /**
         * 设置要播放的音乐地址
         * @param url
         */
        public void setMusicData(String url){
            LogHelper.e(TAG,getClass().getName()+"---setMusicData");
            try {
                player.setDataSource(url);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 开始播放
         */
        public void startPlay(){
            LogHelper.e(TAG,getClass().getName()+"---startPlay");
            startForeground(0,getNotification());
            player.start();
        }
        /**
         * 暂停播放
         */
        public void pausePlay(){
            LogHelper.e(TAG,getClass().getName()+"---pausePlay");
            player.pause();
        }

        /**
         * 停止播放
         */
        public void stopPlay(){
            LogHelper.e(TAG,getClass().getName()+"---stopPlay");
            player.stop();
        }

        /**
         * 获取音乐时长
         * @return
         */
        public int getDuration(){
            LogHelper.e(TAG,getClass().getName()+"---getDuration");
            int duration = player.getDuration();
            return duration;
        }

        /**
         * 获取当前播放的位置
         * @return
         */
        public int getCurrentPosition(){
            LogHelper.e(TAG,getClass().getName()+"---getCurrentPosition");
            int currentPosition = player.getCurrentPosition();
            return currentPosition;
        }

        /**
         * 跳转到指定位置播放
         * @param position
         */
        public void seekTo(int position){
            LogHelper.e(TAG,getClass().getName()+"---seekTo");
            player.seekTo(position);
        }

        /**
         * 是否正在播放
         * @return
         */
        public boolean isPlaying(){
            LogHelper.e(TAG,getClass().getName()+"---isPlaying");
            return player.isPlaying();
        }
    }

    /**
     * 创建通知
     * @return
     */
    public Notification getNotification(){
        LogHelper.e(TAG,getClass().getName()+"---getNotification");
        //获取通知管理器对象
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //创建意图对象
        Intent intent = new Intent(this,MusicPlayerActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        //设置通知属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,null);
        builder.setContentTitle("BoomPlayer");
        builder.setContentText("The music is playing ... ");
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        return builder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
