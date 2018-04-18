package com.example.boomplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boomplayer.util.MusicUtil;

import java.util.List;

/**
 * 音乐列表适配器
 * Created by uidq1246 on 2018-4-12.
 */

public class MusicAdapter extends BaseAdapter {

    /**
     * 装载音乐的集合
     */
    private List<MusicMedia> mList;
    /**
     * 内置加载器对象
     */
    private LayoutInflater mInflater ;

    public MusicAdapter(Context context , List<MusicMedia> mList) {
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        //判断是否缓存
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.music_item,null,false);
            holder.musicImageView = convertView.findViewById(R.id.music_image_view);
            holder.musicTitle = convertView.findViewById(R.id.music_title);
            holder.musicArtist = convertView.findViewById(R.id.music_artist);
            holder.musicDuration = convertView.findViewById(R.id.music_duration);
            holder.musicSize = convertView.findViewById(R.id.music_size);
            convertView.setTag(holder);
        }else {
            //通过Tag找到缓存的数据
            holder = (ViewHolder) convertView.getTag();
        }
        //设置布局中控件要显示的视图
        //holder.musicImageView.setImageBitmap(mList.get(position).getBitmap());
        holder.musicTitle.setText(mList.get(position).getTitle());
        holder.musicArtist.setText(mList.get(position).getArtist());
        String duration = MusicUtil.formatMusicDuration(mList.get(position).getDuration());
        holder.musicDuration.setText(duration);
//        holder.musicDuration.setText(mList.get(position).getDuration());
        String size = MusicUtil.formatMusicSize(mList.get(position).getSize());
        holder.musicSize.setText(size);
//        holder.musicSize.setText(Long.toString(mList.get(position).getSize()));
        return convertView;
    }

    /**
     * ViewHolder实体类
     * 装载单个列表信息的视图控件对象
     */
    public class ViewHolder{
        public ImageView musicImageView ;
        public TextView musicTitle ;
        public TextView musicArtist;
        public TextView musicDuration ;
        public TextView musicSize ;
    }
}
