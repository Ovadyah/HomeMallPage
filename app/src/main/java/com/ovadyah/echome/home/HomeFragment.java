package com.ovadyah.echome.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovadyah.echome.R;

public class HomeFragment extends Fragment {
    private String[] mTitles = new String[]{"精选", "首页", "手机", "家电", "生鲜", "食品", "家居厨具", "美妆", "酒水", "母婴童装"};
    private TabLayout mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private HomeItemFragment[] mFragments = new HomeItemFragment[mTitles.length];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_view, container, false);
        initView(view);
        return view;
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private void initView( View view) {
        mViewPager = view.findViewById(R.id.home_viewpager);
        mIndicator = view.findViewById(R.id.home_indicator);
        mIndicator.setupWithViewPager(mViewPager);
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
        mViewPager.setCurrentItem(0);
    }

}
