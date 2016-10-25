package com.rimi.angel.angeldoctor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.PatientDataActivity;
import com.rimi.angel.angeldoctor.adapter.ReportListAdapter;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Android on 2016/6/2.
 * 检查记录
 */
public class ReportListFragment extends Fragment implements AdapterView.OnItemClickListener{

    @Bind(R.id.mListview)
    ListView mListView;

    public static JSONArray result;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reportlist,null);
        ButterKnife.bind(this,rootView);

        loadData(PatientDataActivity.icNo);
        return rootView;
    }
    @OnItemClick({R.id.mListview})
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PatientDataActivity thisActivty = (PatientDataActivity) getActivity();
        PatientDataActivity.BACK_STATE = 1;
        String id = result.optJSONObject(i).optString("id");
        String type = result.optJSONObject(i).optString("type");
        String source = result.optJSONObject(i).optString("source");
        thisActivty.gone(id,type,source);
    }
    private void loadData(String icNo){
        RequestParams params = new RequestParams(HttpUrls.REPORT);
        params.addBodyParameter("method", "cList");
        params.addBodyParameter("IcNo",icNo);
        XHttpRequest.getInstance().httpPost(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                Log.d("result", "22222");
                PatientDataActivity.requestNum = PatientDataActivity.requestNum + 1;
                if (PatientDataActivity.requestNum == 2){
                    CommonUtils.dismiss();
                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    result = response.getJSONArray("result");
                    mListView.setAdapter(new ReportListAdapter(getActivity(),result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
