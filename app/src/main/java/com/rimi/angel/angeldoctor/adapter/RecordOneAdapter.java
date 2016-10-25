package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.bean.PatientData;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * Created by Android on 2016/6/17.
 */
public class RecordOneAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<PatientData> list;
    private Map<Integer, JSONObject> map;

    public RecordOneAdapter(Context mContext, List<PatientData> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void setMap(Map<Integer, JSONObject> map) {
        this.map = map;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public PatientData getGroup(int i) {
        return list.get(i);
    }

    @Override
    public String getChild(int i, int i1) {
        return list.get(i).getVISIT_NO();
    }

    @Override
    public long getGroupId(int i) {
        return 1;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_record_one, null);
        TextView dataTv = (TextView) view.findViewById(R.id.record_data_tv);
        dataTv.setText(list.get(i).getVISIT_DATE());
        return view;
    }


    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_record_two, null);
        ListView reportListView = (ListView) view.findViewById(R.id.report_listview);
        if (null != map) {
            if (null != map.get(i)) {
                JSONObject job = map.get(i);
                reportListView.setAdapter(new ReportThreeAdapter(mContext,job));
            }

        }
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }


}
