package com.rimi.angel.angeldoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.AppointmentAdapter;
import com.rimi.angel.angeldoctor.bean.AppointmentDataTwo;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;
import com.rimi.angel.angeldoctor.utils.SortTool2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Android on 2016/6/1.
 * 我的预约
 */
public class MyAppointmentActivity extends BaseActivity implements OnClickListener,AdapterView.OnItemClickListener{

    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.dateTv)
    TextView dateTv;
    @Bind(R.id.next_month)
    ImageButton nextMonthBtn;
    @Bind(R.id.before_month)
    ImageButton beforeMonthBtn;
    @Bind(R.id.mListview)
    ListView mListView;

    private int nowYear;
    private int nowMonth;
    private int nowDay;
    private static final int NEXT_MONTH = 1;
    private static final int BEFORE_MONTH = 0;

    private List<AppointmentDataTwo> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        ButterKnife.bind(this);
        CommonUtils.getLoading(this, "加载中.....");
        loadData();
    }
    private void loadData(){
//        String doc_code = (String) SPUtils.get(getApplicationContext(),DOC_CODE,CODE,"");
//        RequestParams params = new RequestParams(HttpUrls.MY_APPOINTMENT);
//        params.addBodyParameter("code", doc_code);
//        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
//            @Override
//            public void onCallBack(boolean isSuccess, Object obj) {
//                CommonUtils.dismiss();
//                if (!isSuccess){
//                    return;
//                }
//                try {
//                    JSONObject response = new JSONObject(obj.toString());
//                    if (response.isNull("result")){
//                        return;
//                    }
//                    JSONArray result = response.getJSONArray("result");
//                    list = GsonUtils.getGson().fromJson(result.toString(), new TypeToken<List<AppointmentData>>(){}.getType());
//                    setadapter(mListView, list);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        RequestParams params1 = new RequestParams(HttpUrls.TEST_URL + "Customer/MyAppointentInfo.ashx?action=Get");
        params1.addBodyParameter("doctorCode",getDocCode());
        XHttpRequest.getInstance().httpPost(this, params1, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    if (response.isNull("result")){
                        return;
                    }
                    JSONArray result = response.getJSONArray("result");
                    list = GsonUtils.getGson().fromJson(result.toString(), new TypeToken<List<AppointmentDataTwo>>(){}.getType());
                    setadapter(mListView, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setadapter(ListView listView,List<AppointmentDataTwo> list){
        SortTool2 tool = new SortTool2();
        Collections.sort(list,tool);
        AppointmentAdapter adapter = new AppointmentAdapter(this,list);
        listView.setAdapter(adapter);
    }
    @OnClick({R.id.back_btn,R.id.next_month,R.id.before_month})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.before_month:
                nextMonthBtn.setVisibility(View.VISIBLE);
                beforeMonthBtn.setVisibility(View.GONE);
                dateDeal(0);
                break;
            case R.id.next_month:
                nextMonthBtn.setVisibility(View.GONE);
                beforeMonthBtn.setVisibility(View.VISIBLE);
                dateDeal(1);
                break;
        }
    }
    @OnItemClick({R.id.mListview})
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NowAppointmentActivity.class);
        intent.putExtra(CODE , 1);
        intent.putExtra("date",list.get(i).getAPPOINTMENT());
        intent.putExtra("deptCode",list.get(i).getDEPT_CODE());
        intent.putExtra("time", list.get(i).getAPPOINTMENT());
        startActivity(intent);
    }
    private void dateDeal(int nextOrbefore){
        switch (nextOrbefore){
            case 0: //before
                dateTv.setText(nowYear + "年" + (nowMonth ) + "月");
                break;
            case 1: //next
                dateTv.setText(nowYear + "年" + (nowMonth + 1) + "月");
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
