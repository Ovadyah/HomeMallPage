package com.ovadyah.echome.demo1.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovadyah.echome.R;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    private final List<String> mData = new ArrayList<>();
    private final Context mContext;

    public SimpleAdapter(Context context, List<String> list) {
        mContext = context;
        mData.addAll(list);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SimpleViewHolder.getDefault(parent);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.update(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.title_info);
        }

        public static SimpleViewHolder getDefault(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new SimpleViewHolder(v);
        }

        public void update(String s) {
            text.setText(s);
        }
    }
}
