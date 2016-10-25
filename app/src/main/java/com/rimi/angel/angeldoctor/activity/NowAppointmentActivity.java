package com.rimi.angel.angeldoctor.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.TodayAppointTwoAdapter;
import com.rimi.angel.angeldoctor.bean.TodayAppointment;
import com.rimi.angel.angeldoctor.bean.TodayAppointmentTwo;
import com.rimi.angel.angeldoctor.fragment.AlreadyDealFragment;
import com.rimi.angel.angeldoctor.fragment.WaitDealFragment;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/6/1.
 * 今日预约
 */
public class NowAppointmentActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.mListview)
    ListView mListView;
    @Bind(R.id.title_tv)
    TextView titleTv;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private WaitDealFragment waitDealFragment;
    private AlreadyDealFragment alreadyDealFragment;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    private String listData;
    private List<TodayAppointment> list;
    public static Map<Integer, TodayAppointment> map = new HashMap<Integer, TodayAppointment>();
    public static Map<Integer, TodayAppointmentTwo> map1 = new HashMap<Integer, TodayAppointmentTwo>();;
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_appointment);
        ButterKnife.bind(this);
        code = getIntent().getIntExtra(CODE, 0);
        if (code == 1){
            titleTv.setText(getIntent().getStringExtra("time"));
            CommonUtils.getLoading(this, "加载中.....");
            String date = getIntent().getStringExtra("date");
            String deptCode = getIntent().getStringExtra("deptCode");
            loadTodayAppointment(date,deptCode);
            return;
        }

        listData = getIntent().getStringExtra(DATA);
        setAdapter(listData);
    }
    private void setAdapter(String listData){
        Gson gson = GsonUtils.getGson();
//        if (code == 1){
            List<TodayAppointmentTwo> mList = gson.fromJson(listData.toString(), new TypeToken<List<TodayAppointmentTwo>>(){}.getType());
            TodayAppointTwoAdapter adapter = new TodayAppointTwoAdapter(this, mList);
            for (int i = 0; i < mList.size(); i++) {
                map1.put(i,mList.get(i));
            }
            mListView.setAdapter(adapter);
//        }else {
//            list = gson.fromJson(listData.toString(), new TypeToken<List<TodayAppointment>>(){}.getType());
//            TodayAppointAdapter adapter = new TodayAppointAdapter(this, list);
//            for (int i = 0; i < list.size(); i++) {
//                map.put(i,list.get(i));
//            }
//            mListView.setAdapter(adapter);
//        }
    }
    //加载今日预约
    public void loadTodayAppointment(String date, String deptCode) {
//        RequestParams params = new RequestParams(HttpUrls.TODAY_APPOINTMENT);
//        params.addBodyParameter("pageSize", "200");
//        params.addBodyParameter("pageIndex", "1");
//        params.addBodyParameter("doctor", getDocName());
//        params.addBodyParameter("makeDate", date);
//        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
//            @Override
//            public void onCallBack(boolean isSuccess, Object obj) {
//                CommonUtils.dismiss();
//                if (!isSuccess) {
//                    return;
//                }
//                try {
//                    JSONObject response = new JSONObject(obj.toString());
//                    JSONObject result = response.getJSONObject("result");
//                    listData = result.getJSONArray("list").toString();
//                    setAdapter(listData);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        RequestParams params1 = new RequestParams(HttpUrls.TEST_URL + "Customer/MyAppointentInfo.ashx?action=Dea");
        params1.addBodyParameter("appDate", date);
        params1.addBodyParameter("docCode",getDocCode());
        params1.addBodyParameter("pageIndex","1");
        params1.addBodyParameter("pageSize","200");
        params1.addBodyParameter("deptCode",deptCode);
        XHttpRequest.getInstance().httpPost(this, params1, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONArray res = response.getJSONObject("result").getJSONArray("res");
                    if (null == res){
                        return;
                    }
                    listData = res.toString();
                    setAdapter(listData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.back_btn})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}