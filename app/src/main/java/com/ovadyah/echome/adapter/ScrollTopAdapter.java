package com.ovadyah.echome.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ovadyah.echome.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollTopAdapter extends RecyclerView.Adapter<ScrollTopAdapter.ImgViewHolder> {
    private final List<String> mData = new ArrayList<>();
    private final Context mContext;

    public ScrollTopAdapter(Context context, List<String> list) {
        mContext = context;
        mData.addAll(list);
    }

    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ImgViewHolder.getDefault(parent);
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {
        holder.update(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ImgViewHolder extends RecyclerView.ViewHolder {
        private final ImageView text;

        public ImgViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.image);
        }

        public static ImgViewHolder getDefault(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scroll_top_item, parent, false);
            return new ImgViewHolder(v);
        }

        public void update(String s) {
            text.setTag(s);
        }
    }
}
