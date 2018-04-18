package com.example.boomplayer;

import android.graphics.Bitmap;

/**
 * 媒体音乐信息实体类
 * Created by uidq1246 on 2018-4-12.
 */

public class MusicMedia {

    /**
     * 歌曲ID
     */
    private int mId ;

    /**
     * 歌曲标题
     */
    private String mTitle ;

    /**
     * 歌曲演唱者
     */
    private String mArtist ;

    /**
     * 歌曲时长
     */
    private long mDuration ;

    /**
     * 歌曲所占空间大小
     */
    private long mSize ;

    /**
     * 歌曲在文件中的路径
     */
    private String mUrl ;

    /**
     * 歌曲专辑名
     */
    private String mAlbum ;

    /**
     * 歌曲专辑ID
     */
    private int mAlbumId ;

    /**
     * 歌曲专辑封面
     */
    private Bitmap mBitmap;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap Bitmap) {
        this.mBitmap = Bitmap;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long mSize) {
        this.mSize = mSize;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int mAlbumId) {
        this.mAlbumId = mAlbumId;
    }
}
