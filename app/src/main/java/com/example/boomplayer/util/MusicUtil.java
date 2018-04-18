package com.example.boomplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.boomplayer.MusicMedia;

import java.util.ArrayList;

/**
 * 音乐工具类
 * Created by uidq1246 on 2018-4-13.
 */

public class MusicUtil {

    private static String TAG = "MusicUtil";

    /**
     * 扫描所有音频文件
     */
    public static ArrayList<MusicMedia> scanAllAudioFiles(Context context){
        LogHelper.e(TAG,TAG+"---scanAllAudioFiles");
        ArrayList<MusicMedia> mList = new ArrayList<MusicMedia>();
        //获取所有的音频文件的游标对象
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //遍历游标
        if(cursor == null){
            return mList;
        }
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                MusicMedia music = new MusicMedia();
                //获取ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲标题Title
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //演唱者artist
                String  artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲时长duration
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //歌曲所占空间size
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                //歌曲专辑album
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                //专辑id
                int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //歌曲文件路径url
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                //设置音乐信息
                music.setId(id);
                music.setTitle(title);
                music.setArtist(artist);
                music.setDuration(duration);
                music.setSize(size);
                music.setAlbum(album);
                music.setAlbumId(albumId);
                music.setUrl(url);
                //添加到音乐列表中
                mList.add(music);
            }
        }
        return mList;
    }

    /**
     * 将单位为byte的size转为以M为单位的字符串
     * @param size
     * @return
     */
    public static String formatMusicSize(long size){
        LogHelper.e(TAG,TAG+"---formatMusicSize");
        float s = ((float)size)/1024/1024 ;
        String sSize = String.format("%.2f",s)+"M";
//        String sSize = Float.toString(s);
        return sSize;
    }

    public static String formatMusicDuration(long duration){
        LogHelper.e(TAG,TAG+"---formatMusicDuration");
        int totalTime = Math.round(duration/1000);
        String d = String.format("%02d:%02d",totalTime/60,totalTime%60);
        return d ;
    }

}
