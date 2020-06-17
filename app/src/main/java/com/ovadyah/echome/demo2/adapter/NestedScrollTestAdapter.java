package com.ovadyah.echome.demo2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.bean.DataItemBean;

import java.util.List;

public class NestedScrollTestAdapter extends RecyclerView.Adapter<NestedScrollTestAdapter.ViewHoder> {
    public static final String TAG = "hfy+NestedScrollTest";
    private final Context context;
    private final List<DataItemBean> list;

    public NestedScrollTestAdapter(Context context, List<DataItemBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_nested_scroll_test, viewGroup, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHoder(itemView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHoder holder) {
        super.onViewAttachedToWindow(holder);
        Log.i(TAG, "onViewAttachedToWindow: "+holder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, int i) {
        DataItemBean dataBean = list.get(i);
        viewHoder.textView.setText(dataBean.text);
        viewHoder.imageView.setImageResource(dataBean.imgRes);
//        ImageLoader.with(context).loadBitmapAsync(dataBean.url,viewHoder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nest_textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
