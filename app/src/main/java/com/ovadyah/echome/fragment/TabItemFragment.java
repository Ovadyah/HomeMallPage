package com.ovadyah.echome.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovadyah.echome.R;
import com.ovadyah.echome.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabItemFragment extends Fragment {
    public static final String TITLE = "title";
    private String mTitle = "";
    private RecyclerView mRecyclerView;
    private List<String> mDatas = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_item_view, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        for (int i = 0; i < 100; i++) {
            mDatas.add(String.format("%s展示效果%s",mTitle,i));
        }
        mRecyclerView.setAdapter(new SimpleAdapter(getActivity(), mDatas));
        return view;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public static TabItemFragment newInstance(String title) {
        TabItemFragment tabItemFragment = new TabItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabItemFragment.setArguments(bundle);
        return tabItemFragment;
    }

}
