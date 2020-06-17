package com.ovadyah.echome.demo2.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.ovadyah.echome.home.HomeItemFragment;

import java.util.ArrayList;
import java.util.List;

public class ECHome2ItemFragment extends Fragment {
    private String[] mTitles = new String[]{"精选", "首页", "手机", "家电", "生鲜", "食品", "家居厨具", "美妆", "酒水", "母婴童装"};
    private TabLayout mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private HomeItemFragment[] mFragments = new HomeItemFragment[mTitles.length];

    private NestedScrollingOuterLayout nestedScrollingOuterLayout;
    private RecyclerView recyclerViewParent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ec_home2_item_view, container, false);
        initView(view);
        return view;
    }

    public static ECHome2ItemFragment newInstance() {
        ECHome2ItemFragment homeFragment = new ECHome2ItemFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private void initView( View view) {
        nestedScrollingOuterLayout = view.findViewById(R.id.nested_scrolling_outer_layout);
        recyclerViewParent = view.findViewById(R.id.recyclerView_parent);
        recyclerViewParent.setLayoutManager(new LinearLayoutManager(getContext()));
        EcHome2NestAdapter adapter = new EcHome2NestAdapter(getContext(),getChildFragmentManager());
        if (nestedScrollingOuterLayout != null) {
            adapter.setNestedParentLayout(nestedScrollingOuterLayout);
        }
        recyclerViewParent.setAdapter(adapter);

        adapter.updateAdapter(getListData());

       /* RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800*//*,false*//*);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(800*//*,false*//*);//传入false表示加载失败
            }
        });
        //不能加载更多
        refreshLayout.setEnableLoadMore(false);
        NestedScrollLayout nestedScrollView = view.findViewById(R.id.home_neste_scroll_view);
        nestedScrollView.setNeedScroll(false);
        mViewPager = view.findViewById(R.id.home_viewpager);
        mIndicator = view.findViewById(R.id.home_indicator);
        mIndicator.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mTitles.length);
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = HomeItemFragment.newInstance(mTitles[i]);
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
        mViewPager.setCurrentItem(0);*/
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
