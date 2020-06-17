package com.ovadyah.echome.demo2.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.adapter.viewHolder.ViewPagerHolder;
import com.ovadyah.echome.demo2.bean.DataItemBean;
import com.ovadyah.echome.demo2.bean.MultiBaseBean;
import com.ovadyah.echome.demo2.bean.ViewPagerBean;
import com.ovadyah.echome.demo2.view.NestedScrollingOuterLayout;

import java.util.ArrayList;
import java.util.List;

public class EcHome2NestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private final List<MultiBaseBean> mData = new ArrayList<>();
    private final Context mContext;
    private NestedScrollingOuterLayout  mNestedScrollingParent;
    private FragmentManager fragmentManager;
    public EcHome2NestAdapter(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.fragmentManager = fragmentManager;
    }

    public void updateAdapter(List<MultiBaseBean> listItem) {
        if (listItem != null && listItem.size() > 0){
            if (mData != null) {
                mData.clear();
            }
            mData.addAll(listItem);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData != null && mData.size() > 0 ? mData.get(position).itemType() : super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewPagerBean.VIEW_PAGE_TYPE  ){
            return ViewPagerHolder.getDefault(parent,mNestedScrollingParent,fragmentManager);
        } else {
            return ImgViewHolder.getDefault(parent);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position) {
        if (holder instanceof ImgViewHolder){
            ((ImgViewHolder)holder).update(mData.get(position));
        } else if (holder instanceof ViewPagerHolder){
            ((ViewPagerHolder)holder).update((ViewPagerBean)mData.get(position));
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
            img = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.nest_textView);
        }

        public static ImgViewHolder getDefault(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nested_scroll_test, parent, false);
            return new ImgViewHolder(v);
        }

        public void update(MultiBaseBean dataBean) {
            if (dataBean instanceof DataItemBean){
                DataItemBean dataItemBean = (DataItemBean) dataBean;
                text.setText(dataItemBean.text);
                img.setImageResource(dataItemBean.imgRes);
            }

        }
    }

    public void setNestedParentLayout(NestedScrollingOuterLayout nestedScrollingParent) {
        this.mNestedScrollingParent = nestedScrollingParent;
    }
}
