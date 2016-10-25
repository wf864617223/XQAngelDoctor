package com.rimi.angel.angeldoctor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rimi.angel.angeldoctor.R;

/**
 * Created by Android on 2016/6/2.
 * 今日到院
 */
public class TodayToHospitalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list,null);
        return rootView;
    }
}
