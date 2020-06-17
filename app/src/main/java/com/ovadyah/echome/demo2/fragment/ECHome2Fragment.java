package com.ovadyah.echome.demo2.fragment;

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
import com.ovadyah.echome.home.HomeItemFragment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ECHome2Fragment extends Fragment {
    private String[] mTitles = new String[]{"精选", "首页", "手机", "家电", "生鲜", "食品", "家居厨具", "美妆", "酒水", "母婴童装"};
    private TabLayout mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private ECHome2ItemFragment[] mFragments = new ECHome2ItemFragment[mTitles.length];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ec_home2_view, container, false);
        initView(view);
        return view;
    }

    public static ECHome2Fragment newInstance() {
        ECHome2Fragment homeFragment = new ECHome2Fragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private void initView( View view) {
      RefreshLayout refreshLayout = view.findViewById(R.id.home2_refreshLayout);
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
        mViewPager = view.findViewById(R.id.home2_viewpager);
        mIndicator = view.findViewById(R.id.home2_indicator);
        mIndicator.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mTitles.length);
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = ECHome2ItemFragment.newInstance();
        }
        mAdapter = new FragmentPagerAdapter(this.getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
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
