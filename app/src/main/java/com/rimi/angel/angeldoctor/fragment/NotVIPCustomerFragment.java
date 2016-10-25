package com.rimi.angel.angeldoctor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.MyPatientActivity;
import com.rimi.angel.angeldoctor.utils.SPUtils;
import com.rimi.angel.angeldoctor.widget.RefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/2.
 * 非会员用户
 */
public class NotVIPCustomerFragment extends Fragment {
    @Bind(R.id.listview_frag)
    ListView mListView;
    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list,null);
        ButterKnife.bind(this, rootView);
        final MyPatientActivity thisActivity = new MyPatientActivity();
        final String DocCode  = (String) SPUtils.get(getActivity(),"doctor_code","code","");
        thisActivity.loadData("2", mListView, getActivity(),DocCode,null, "20",null);
//        refreshLayout.setChildView(mListView);
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                thisActivity.loadData("2", mListView, getActivity(),DocCode,null, "50",refreshLayout);
                refreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
