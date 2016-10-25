package com.rimi.angel.angeldoctor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.MainListAdapter;
import com.rimi.angel.angeldoctor.bean.Report;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/5/31.
 * 一周报告
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.mListview)
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        CommonUtils.getLoading(this, "加载中.....");
        loadData();
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.REPORT);
        params.addBodyParameter("method", "dList");
        params.addBodyParameter("doctor_code",getDocCode());
        params.addBodyParameter("start_date",CommonUtils.getbeforeWeekDate() + " 0:00:00");
        params.addBodyParameter("end_date", CommonUtils.getNowDate() + " 23:59:59");
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    List<Report> list = GsonUtils.getGson().fromJson(response.getJSONArray("list").toString(), new TypeToken<List<Report>>(){}.getType());
                    MainListAdapter adapter = new MainListAdapter(ReportActivity.this,list);
                    mListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.back_btn})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
