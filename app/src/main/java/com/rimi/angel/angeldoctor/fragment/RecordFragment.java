package com.rimi.angel.angeldoctor.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.PatientDataActivity;
import com.rimi.angel.angeldoctor.adapter.RecordOneAdapter;
import com.rimi.angel.angeldoctor.bean.PatientData;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.SortTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/2.
 * 门诊病历
 */
public class RecordFragment extends Fragment {

    @Bind(R.id.listview_frag)
    ExpandableListView fragListView;

    private List<PatientData> list;
    private RecordOneAdapter adapter;

    Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 2){
                SortTool sort = new SortTool();
                Collections.sort(list,sort);

//                List<PatientData> newList = new ArrayList<PatientData>();
//                for (int i = (list.size() - 1); i >= 0; i--) {
//                    newList.add(list.get(i));
//                }
                adapter = new RecordOneAdapter(getActivity(),list);
                fragListView.setAdapter(adapter);

                for (int j = 0; j < list.size() ; j++) {
                    loadData(list.get(j).getVISIT_NO(),j);
                }
            }
            return false;
        }
    });
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record,null);
        ButterKnife.bind(this,rootView);
        CommonUtils.setDialogMsg("加载中.....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                    list = PatientDataActivity.LIST;
                }while (null == list);
                handler.sendEmptyMessage(2);
            }
        }).start();
        fragListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (!map.containsKey(groupPosition)){
                    Toast.makeText(getActivity(), "此病例已撤回" , Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return rootView;
    }
    private void loadData(String visitNo, final int position){
        RequestParams params = new RequestParams(HttpUrls.BING_LI);
        params.addBodyParameter("method", "Outpmr");
        params.addBodyParameter("visit_no", visitNo);
        XHttpRequest.getInstance().httpPost(getActivity(),params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONArray result = response.getJSONArray("result");
                    if (null == result){
                        return;
                    }
                    map.put(position,result.getJSONObject(0));
                    adapter.setMap(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
