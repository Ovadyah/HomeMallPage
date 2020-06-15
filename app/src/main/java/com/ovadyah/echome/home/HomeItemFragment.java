package com.ovadyah.echome.home;

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
import android.widget.RelativeLayout;

import com.ovadyah.echome.R;
import com.ovadyah.echome.adapter.ScrollTopAdapter;
import com.ovadyah.echome.fragment.TabItemFragment;
import com.ovadyah.echome.port.AsyncGetPort;
import com.ovadyah.echome.view.SmoothNestedScrollLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeItemFragment extends Fragment {
    public static final String TITLE = "title";
    private String[] mTitles = new String[]{"猜你喜欢", "买家秀", "爆品", "直播"};
    private TabLayout mIndicator;
    private ViewPager mViewPager;
    private RecyclerView mTopRecyclerView;
    private FragmentPagerAdapter mAdapter;
    private TabItemFragment[] mFragments = new TabItemFragment[mTitles.length];
    private SmoothNestedScrollLayout mScrollLayout;
//    private View mTopActionBar;
//    private View mBottomBar;
    private boolean isScrollTop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_item_view, container, false);
        initView(view);
        return view;
    }
    public static HomeItemFragment newInstance(String title) {
        HomeItemFragment homeFragment = new HomeItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private void initView( View view) {
        mScrollLayout = view.findViewById(R.id.scroll_parent);
        mIndicator = view.findViewById(R.id.scroll_page_indicator);
        RelativeLayout mTopView = view.findViewById(R.id.scroll_top_view);
        mTopRecyclerView = view.findViewById(R.id.scroll_top_recycler);
        mViewPager = view.findViewById(R.id.scroll_list_viewpager);
        // 注意：这次在layout里对 SmoothNestedScrollLayout 已经设置了nest_scroll_content等属性，
        // 所以这里不用再调API设置对应的view了，只需要把顶部、底部操作栏的高度告诉 SmoothNestedScrollLayout
        mTopRecyclerView.setNestedScrollingEnabled(false);
        mTopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> list = new ArrayList<>();
        for (int i=0;i<8;i++){
            list.add(""+i);
        }
        mTopRecyclerView.setAdapter(new ScrollTopAdapter(getContext(),list));
        mTopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();
                if (count > 0) {
                    int realHeight = 0;
                    int realWidth = 0;
                    for(int i = 0;i < count; i++){
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                    if (realHeight > 0){
                        mTopView.getLayoutParams().height = realHeight;
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
        mIndicator.setupWithViewPager(mViewPager);
//        mTopActionBar = view.findViewById(R.id.top_transparent_bar);
//        mTopActionBar.getViewTreeObserver().addOnGlobalLayoutListener(() ->
//                mScrollLayout.setOuterCoverTopMargin(mTopActionBar.getHeight()));
//        mBottomBar = view.findViewById(R.id.bottom_action_bar);
//        mBottomBar.getViewTreeObserver().addOnGlobalLayoutListener(() ->
//                mScrollLayout.setOuterCoverBottomMargin(mBottomBar.getHeight()));
        // 需要告诉nest scroll parent 当前的scroll child 是谁 以决定 scroll 的传递对象
        mScrollLayout.setControlDelegate(new SmoothNestedScrollLayout.OnScrollControlDelegate() {
            @Override
            public View getScrollChildView() {
                return mFragments[mViewPager.getCurrentItem()].getRecyclerView();
            }
        });

        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = TabItemFragment.newInstance(mTitles[i]);
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

        // 添加 top action bar 渐变 alpha 效果
//        mTopActionBar.getBackground().setAlpha(0);
//        mScrollLayout.setScrollListener((dy, scrollY) -> {
//            if (isScrollTop != (scrollY == 0)){
//                isScrollTop = scrollY == 0;
//                AsyncGetPort.getInstance().triggerOnScrollTopListener(scrollY == 0);
//            }
////            int alpha = scrollY * 255 / mScrollLayout.getTopScrollHeight();
////            mTopActionBar.getBackground().setAlpha(alpha);
//        });
    }

}
