package com.rimi.angel.angeldoctor.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hb.update.UpdateManager;
import com.rimi.angel.angeldoctor.AngelApplication;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.MainListAdapter;
import com.rimi.angel.angeldoctor.bean.Report;
import com.rimi.angel.angeldoctor.bean.TodayAppointment;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;
import com.rimi.angel.angeldoctor.utils.SPUtils;
import com.rimi.angel.angeldoctor.widget.CircleImageView;
import com.rimi.angel.angeldoctor.widget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/5/31.
 * @author misy
 * 主页面
 */
public class MainActivity extends BaseActivity implements OnClickListener{

    private Context mContext;

    @Bind(R.id.userinfo_btn)
    ImageView userinfoBtn;
    @Bind(R.id.deal_num)
    TextView dealNumTv;
    @Bind(R.id.showmsg_img)
    ImageView showMsgImg;
    @Bind(R.id.report_btn)
    Button reportBtn;
    @Bind(R.id.my_patient_btn)
    Button myPatientBtn;
    @Bind(R.id.my_appointment_btn)
    Button myAppoitmentBtn;
    @Bind(R.id.see_patient_btn)
    Button seePatientBtn;
    @Bind(R.id.undeal_layout)
    ViewGroup dealingLayout;
    @Bind(R.id.now_appointment_btn)
    Button seeDealBtn;
    @Bind(R.id.mListview)
    ListView mListView;
    @Bind(R.id.red_point)
    CircleImageView redPointImg;
    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    public static final int MY_PATIENT = 1;
    public static final int SEARCH_PATIENT = 2;
    private int requestNum = 0;
    private int refreshNum ;
    private  List<TodayAppointment> list = new ArrayList<TodayAppointment>();
    private JSONArray listData;
    private boolean hasNews;
    private MainListAdapter adapter;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        AngelApplication.getInstance().addActivity(this);
        CommonUtils.getLoading(this, "加载中.....");
        loadTodayReport();
        loadTodayAppointment(CommonUtils.getNowDate());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNum = 1;
                loadTodayAppointment(CommonUtils.getNowDate());
                loadTodayReport();
            }
        });
    }
    //加载今日报告
    private void loadTodayReport(){
        RequestParams params = new RequestParams(HttpUrls.REPORT);
        params.addBodyParameter("method", "dList");
        params.addBodyParameter("doctor_code", (String) SPUtils.get(this,DOC_CODE,CODE,""));
        params.addBodyParameter("start_date", CommonUtils.getNowDate() + " 00:00:00");
        params.addBodyParameter("end_date", CommonUtils.getNowDate() + " 23:59:59");
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                requestNum = requestNum + 1;
                refreshNum = refreshNum + 1;
                if (requestNum == 2){
                    CommonUtils.dismiss();
                    int isUpdate = getIntent().getIntExtra("isUpdate",3);
                    if (isUpdate == 1){
                        UpdateManager.newUpdate(mContext);
                    }
                }
                if (refreshNum == 3){
                    if (isSuccess){
                        Toast.makeText(mContext,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                    refreshLayout.setRefreshing(false);
                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    List<Report> list ;
                    Gson gson = GsonUtils.getGson();
                    list = gson.fromJson(response.getJSONArray("list").toString(), new TypeToken<List<Report>>(){}.getType());
                    adapter = new MainListAdapter(mContext,list);
                    dealNumTv.setText(" (" + list.size() + ")");
                    mListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //加载某日预约
    public void loadTodayAppointment(String date){
        RequestParams params = new RequestParams("http://61.139.124.246:90/Customer/MyAppointentInfo.ashx?action=Dea");
        params.addBodyParameter("pageSize", "200");
        params.addBodyParameter("pageIndex", "1");
        params.addBodyParameter("docCode", getDocCode());
        params.addBodyParameter("appDate", date);
//        params.addBodyParameter("deptCode","");
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                requestNum = requestNum + 1;
                refreshNum = refreshNum + 1;
                if (requestNum == 2){
                    CommonUtils.dismiss();
                    int isUpdate = getIntent().getIntExtra("isUpdate",3);
                    if (isUpdate == 1){
                        UpdateManager.newUpdate(mContext);
                    }
                }
                if (refreshNum == 3){
                    if (isSuccess){
                        Toast.makeText(mContext,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                    refreshLayout.setRefreshing(false);
                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONObject result = response.getJSONObject("result");
                    if ("0".equals(result.getString("list"))){
                        seeDealBtn.setVisibility(View.GONE);
                    }
                    seeDealBtn.setText(getResources().getString(R.string.now_appointment) + " :" + result.getString("list"));
                    listData = result.getJSONArray("res");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //是否有未读消息
    private void isUnReadMsg(){
        RequestParams params = new RequestParams(HttpUrls.HAVE_NEW_MSG);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(mContext,"登录信息已过期请重新登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    hasNews = response.getJSONObject("result").getBoolean("has_new");
                    if (hasNews){
                        redPointImg.setVisibility(View.VISIBLE);
                    }else {
                        redPointImg.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.userinfo_btn,R.id.showmsg_img,R.id.report_btn,R.id.my_patient_btn,R.id.my_appointment_btn,R.id.see_patient_btn,R.id.undeal_layout,R.id.now_appointment_btn})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.userinfo_btn://个人中心
                startActivity(new Intent(mContext,UserinfoActivity.class).putExtra("hasNews",hasNews));
                break;
            case R.id.showmsg_img://查看消息通知
                startActivity(new Intent(mContext,ShowMsgActivity.class));
                break;
            case R.id.report_btn://查看未看报告
                startActivity(new Intent(mContext,ReportActivity.class));
                break;
            case R.id.my_patient_btn://查看我的病人
                startActivity(new Intent(mContext,MyPatientActivity.class).putExtra(CODE,MY_PATIENT));
                break;
            case R.id.my_appointment_btn://我的预约
                startActivity(new Intent(mContext,MyAppointmentActivity.class));
                break;
            case R.id.see_patient_btn://查找病人
                startActivity(new Intent(mContext,MyPatientActivity.class).putExtra(CODE,SEARCH_PATIENT));
                break;
            case R.id.undeal_layout://今日待办
                startActivity(new Intent(mContext,UnDealReportActivity.class));
                break;
            case R.id.now_appointment_btn://今日预约
                startActivity(new Intent(mContext,NowAppointmentActivity.class).putExtra(DATA, null == listData ? "" : listData.toString()));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否退出?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消",null)
                    .create();
            dialog.show();


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUnReadMsg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
