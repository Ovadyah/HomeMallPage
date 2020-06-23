package com.ovadyah.echome.demo3.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.adapter.EcHome2NestAdapter;
import com.ovadyah.echome.demo2.bean.DataItemBean;
import com.ovadyah.echome.demo2.bean.MultiBaseBean;
import com.ovadyah.echome.demo2.bean.ViewPagerBean;
import com.ovadyah.echome.demo2.view.NestedScrollingOuterLayout;
import com.ovadyah.echome.demo3.fragment.base.ScrollBaseFragment;
import com.ovadyah.echome.demo3.scrollview.ScrollableHelper;

import java.util.ArrayList;
import java.util.List;

public class NestRecyclerViewFragment extends ScrollBaseFragment implements ScrollableHelper.ScrollableContainer{

    private RecyclerView mRecyclerView;

    public static NestRecyclerViewFragment newInstance() {
        NestRecyclerViewFragment recyclerViewFragment = new NestRecyclerViewFragment();
        return recyclerViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nest_recyclerview, container, false);
        initView(view);
        return view;
    }

    private void initView( View view) {
        NestedScrollingOuterLayout nestedScrollingOuterLayout = view.findViewById(R.id.nested_scrolling_outer_layout3);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.nest_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EcHome2NestAdapter adapter = new EcHome2NestAdapter(getContext(),getChildFragmentManager());
        if (nestedScrollingOuterLayout != null) {
            adapter.setNestedParentLayout(nestedScrollingOuterLayout);
        }
        mRecyclerView.setAdapter(adapter);
        adapter.updateAdapter(getListData());
    }

    private List<MultiBaseBean> getListData() {
        List<MultiBaseBean> listData = new ArrayList<>();
        for (int i =0;i<10;i++){
            listData.add(new DataItemBean(String.format("%s%s","加载列表数据----Text----",i+1), "",R.mipmap.maomao));
        }
        listData.add(new ViewPagerBean());
        return listData;
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }
}
