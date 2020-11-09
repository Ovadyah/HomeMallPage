package com.ovadyah.echome.demo2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovadyah.echome.ImageUtils;
import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.adapter.NestedScrollTestAdapter;
import com.ovadyah.echome.demo2.bean.DataItemBean;
import com.ovadyah.echome.demo2.bean.MultiBaseBean;
import com.ovadyah.echome.demo2.bean.ViewPagerBean;
import com.ovadyah.echome.demo2.view.NestedScrollingOuterLayout;

import java.util.ArrayList;
import java.util.List;

public class NestedScrollFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;

    private NestedScrollingOuterLayout mNestedScrollingParent;


    private int mFragmentIndex;

    public NestedScrollFragment() {
    }

    public static NestedScrollFragment newInstance() {
        NestedScrollFragment fragment = new NestedScrollFragment();
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NestedScrollTestFragment.
     */
    public static NestedScrollFragment newInstance(String param1, String param2) {
        NestedScrollFragment fragment = new NestedScrollFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nested_scroll_test, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NestedScrollTestAdapter(getContext(), getListData()));
    }

    private List<DataItemBean> getListData() {
        List<DataItemBean> listData = new ArrayList<>();
        String[] images = ImageUtils.images;
        int imageles = images.length;
        for (int i =0;i<500;i++){
            int ran = (int) (Math.random()*(imageles-i)+1);
            String imgUrl = images[ran];
            listData.add(new DataItemBean(String.format("%s%s","嵌套列表数据----Item----",i+1), imgUrl,R.mipmap.maomao));
        }
        return listData;
    }

    /**
     * 这个方法仅仅工作在FragmentPagerAdapter的场景中。（普通的activity中的一个fragment 不会调用。）
     *
     * @param visibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean visibleToUser) {
        super.setUserVisibleHint(visibleToUser);

        if (visibleToUser && isCurrentDisplayedFragment()) {
            if (mNestedScrollingParent != null) {
                mNestedScrollingParent.setChildRecyclerView(recyclerView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCurrentDisplayedFragment()) {
            if (mNestedScrollingParent != null) {
                mNestedScrollingParent.setChildRecyclerView(recyclerView);
            }
        }
    }

    /**
     * 是当前展示的fragment
     * @return
     */
    private boolean isCurrentDisplayedFragment() {
        if (getView() == null || !(getView().getParent() instanceof View)) {
            return false;
        }
        View parent = (View) getView().getParent();
        if (parent instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) parent;
            int currentItem = viewPager.getCurrentItem();
            return currentItem == mFragmentIndex;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setNestedParentLayout(NestedScrollingOuterLayout nestedScrollingParent2Layout) {
        mNestedScrollingParent = nestedScrollingParent2Layout;
    }

    public void setIndex(int i) {
        mFragmentIndex = i;
    }
}
