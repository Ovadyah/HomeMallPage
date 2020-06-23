package com.ovadyah.echome.demo3.fragment.base;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ovadyah.echome.demo3.fragment.NestRecyclerViewFragment;
import com.ovadyah.echome.demo3.scrollview.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;
public abstract class BaseFragment extends Fragment {

    private String[] mTitles = new String[]{"精选", "首页", "手机", "家电", "生鲜", "食品", "家居厨具", "美妆", "酒水", "母婴童装"};
    private List<ScrollBaseFragment> fragmentList = new ArrayList<>();

    public void initFragment(ViewPager viewPager, TabLayout indicator,TabLayout headerTab,final ScrollableLayout mScrollLayout) {
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
        indicator.setupWithViewPager(viewPager);
        headerTab.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                /** 标注当前页面 **/
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
    }
}
