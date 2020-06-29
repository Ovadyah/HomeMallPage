package com.ovadyah.echome.demo3.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.bean.DataItemBean;
import com.ovadyah.echome.demo2.bean.MultiBaseBean;
import com.ovadyah.echome.demo2.bean.ViewPagerBean;
import com.ovadyah.echome.demo2.fragment.ECHome2ItemFragment;
import com.ovadyah.echome.demo3.fragment.base.BaseFragment;
import com.ovadyah.echome.demo3.scrollview.ScrollableLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ECHome3Fragment extends BaseFragment {

    private TabLayout mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ec_home3_view, container, false);
        initView(view);
        return view;
    }

    public static ECHome3Fragment newInstance() {
        ECHome3Fragment homeFragment = new ECHome3Fragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private void initView( View view) {
        ScrollableLayout scrollableLayout = view.findViewById(R.id.home3_scrollable_layout);
        RefreshLayout refreshLayout = view.findViewById(R.id.home3_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(800);//传入false表示加载失败
            }
        });
        //不能加载更多
        refreshLayout.setEnableLoadMore(false);
//        NestedScrollLayout nestedScrollView = view.findViewById(R.id.home_neste_scroll_view);
//        nestedScrollView.setNeedScroll(false);
        mViewPager = view.findViewById(R.id.home3_viewpager);
        mIndicator = view.findViewById(R.id.home3_indicator);
        TabLayout headerTab = view.findViewById(R.id.home3_header_indicator);

        initFragment(mViewPager,mIndicator,headerTab,scrollableLayout,refreshLayout,view.findViewById(R.id.rl_head));
    }

    private List<MultiBaseBean> getListData() {
        List<MultiBaseBean> listData = new ArrayList<>();
        for (int i =0;i<10;i++){
            listData.add(new DataItemBean(String.format("%s%s","加载列表数据----Text----",i+1), "",R.mipmap.maomao));
        }
        listData.add(new ViewPagerBean());
        return listData;
    }

}
