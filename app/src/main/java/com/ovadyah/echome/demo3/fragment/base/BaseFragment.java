package com.ovadyah.echome.demo3.fragment.base;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.ovadyah.echome.demo3.fragment.NestRecyclerViewFragment;
import com.ovadyah.echome.demo3.scrollview.ScrollableLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
public abstract class BaseFragment extends Fragment {

    private String[] mTitles = new String[]{"精选", "首页", "手机", "家电", "生鲜", "食品", "家居厨具", "美妆", "酒水", "母婴童装"};
    private List<ScrollBaseFragment> fragmentList = new ArrayList<>();

    public void initFragment(ViewPager viewPager, TabLayout indicator, TabLayout headerTab, final ScrollableLayout mScrollLayout, RefreshLayout refreshLayout, RelativeLayout headerRl) {
        viewPager.setOffscreenPageLimit(mTitles.length);
        for (int i = 0; i < mTitles.length; i++) {
            fragmentList.add(NestRecyclerViewFragment.newInstance());
        }
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(this.getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
        viewPager.setAdapter(mAdapter);
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        mScrollLayout.setClickHeadExpand(dip2px(getContext(),50));
        indicator.setupWithViewPager(viewPager);
        headerTab.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0){
                    headerRl.getLayoutParams().height = dip2px(getContext(),50);
                    headerTab.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.GONE);
                    refreshLayout.setEnableRefresh(true);
                } else {
                    headerRl.getLayoutParams().height = 0;
                    headerTab.setVisibility(View.GONE);
                    indicator.setVisibility(View.VISIBLE);
                    refreshLayout.setEnableRefresh(false);
                }
                /** 标注当前页面 **/
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
    }

    /*** 根据手机的分辨率从 dp 的单位 转成为 px(像素)*/
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
