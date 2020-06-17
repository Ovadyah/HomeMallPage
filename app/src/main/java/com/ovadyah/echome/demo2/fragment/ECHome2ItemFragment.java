package com.ovadyah.echome.demo2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

public class ECHome2ItemFragment extends Fragment {
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
