package com.ovadyah.echome.demo2.adapter.viewHolder;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.adapter.HomePagerAdapter;
import com.ovadyah.echome.demo2.bean.ViewPagerBean;
import com.ovadyah.echome.demo2.fragment.NestedScrollFragment;
import com.ovadyah.echome.demo2.view.NestedScrollingOuterLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerHolder extends RecyclerView.ViewHolder{

    private String[] titles = {"头条", "新闻", "娱乐"};
    private int mSelectedPosition;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    NestedScrollingOuterLayout mScrollingOuterLayout;
    FragmentManager mFragmentManager;
    public ViewPagerHolder(View view, NestedScrollingOuterLayout nestedScrollingParent, FragmentManager fm) {
        super(view);
        this.mFragmentManager = fm;
        this.mScrollingOuterLayout = nestedScrollingParent;
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

    }

    public static ViewPagerHolder getDefault(ViewGroup parent, NestedScrollingOuterLayout nestedScrollingParent, FragmentManager fm) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_page, parent, false);
        return new ViewPagerHolder(v,nestedScrollingParent,fm);
    }

    public void update(ViewPagerBean viewPagerBean) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NestedScrollFragment fragment = new NestedScrollFragment();
            fragment.setIndex(i);
            fragment.setNestedParentLayout(mScrollingOuterLayout);
            fragments.add(fragment);
        }


        HomePagerAdapter adapter = new HomePagerAdapter(mFragmentManager, titles, fragments);
        mViewPager.setAdapter(adapter);

        mViewPager.setCurrentItem(mSelectedPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mSelectedPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);

        if (mScrollingOuterLayout != null) {
            mScrollingOuterLayout.setLastItem(itemView);
//            mScrollingOuterLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//                //设置最后一个item：tab+viewPager
//                mScrollingOuterLayout.setLastItem(itemView);
//            });
        }
    }
}
