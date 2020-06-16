package com.ovadyah.echome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovadyah.echome.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private final List<String> mData = new ArrayList<>();
    private final Context mContext;

    public ScrollTopAdapter(Context context, List<String> list) {
        mContext = context;
        mData.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        return ImgViewHolder.getDefault(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position) {
        if (holder instanceof ImgViewHolder){
            ((ImgViewHolder)holder).update(mData.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ImgViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private final TextView text;

        public ImgViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }

        public static ImgViewHolder getDefault(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scroll_top_img_item, parent, false);
            return new ImgViewHolder(v);
        }

        public void update(String s) {
            text.setText(s);
            img.setTag(s);
        }
    }
}
