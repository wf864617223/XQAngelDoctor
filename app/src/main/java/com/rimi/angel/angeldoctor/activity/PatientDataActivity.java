package com.rimi.angel.angeldoctor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.reflect.TypeToken;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.MyViewPagerAdapter;
import com.rimi.angel.angeldoctor.adapter.ReportCommonAdapter;
import com.rimi.angel.angeldoctor.bean.PatientData;
import com.rimi.angel.angeldoctor.bean.ReportDate;
import com.rimi.angel.angeldoctor.fragment.RecordFragment;
import com.rimi.angel.angeldoctor.fragment.ReportListFragment;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.PhoneCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.Base64Utils;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;
import com.rimi.angel.angeldoctor.utils.HttpLoadImg;
import com.rimi.angel.angeldoctor.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/6/2.
 * 病人信息（检查记录，报告详情）
 */
public class PatientDataActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.home_btn)
    ImageButton homeBtn;
    @Bind(R.id.patient_name)
    TextView nameTv;
    @Bind(R.id.age_tv)
    TextView ageTv;
    @Bind(R.id.sex_tv)
    TextView sexTv;
    @Bind(R.id.mViewpager)
    ViewPager mViewPager;
    @Bind(R.id.cursor_img)
    ImageView cursorImg;
    @Bind(R.id.tab_one)
    TextView tabOneTv;
    @Bind(R.id.tab_two)
    TextView tabTwoTv;
    @Bind(R.id.continer)
    FrameLayout continerView;
    @Bind(R.id.report_list_layout)
    LinearLayout reportListLayout;
    @Bind(R.id.record_layout)
    ScrollView recordLayout;
    @Bind(R.id.call_btn)
    ImageButton callBtn;
    @Bind(R.id.common_list)
    ListView commonListView;
    @Bind(R.id.report_name)
    TextView reportNameTv;
    @Bind(R.id.type_two_layout)
    ViewGroup typeLayout;
    @Bind(R.id.type_two_tv)
    TextView typeTwoTv;
    @Bind(R.id.img_listview)
    ListView imgListView;
    @Bind(R.id.type_three_img)
    SubsamplingScaleImageView typeThreeImg;
    @Bind(R.id.show_big_img)
    Button showBigImgBtn;
    @Bind(R.id.birth_tv)
    TextView birthTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.headimg)
    ImageView headImg;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private ReportListFragment reportListFragment;//检查记录
    private RecordFragment recordFragment;//门诊病历
    public static int BACK_STATE = 0;
    public static String icNo;
    private String phone;
    public static List<PatientData> LIST;
    private JSONArray listArray;
    public static int requestNum ;
    private String phonenum;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    private String age;

    private String myPhone;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(PatientDataActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);
        ButterKnife.bind(this);
        LIST = null;
        int code = getIntent().getIntExtra("code",0);
        requestNum = 0;
        icNo = getIntent().getStringExtra("icNo");
        phonenum = getIntent().getStringExtra("phone");
        if ("".equals(phonenum) || null == phonenum){
            callBtn.setVisibility(View.GONE);
        }else {
            callBtn.setVisibility(View.VISIBLE);
        }
        if (code == 2){
            String id = String.valueOf(getIntent().getIntExtra("code_id",0));
            String type = getIntent().getStringExtra("type");
            String patName = getIntent().getStringExtra("name");
            String source = getIntent().getStringExtra("source");
            nameTv.setText(patName);
            gone(id,type,source);
            getPatientData(icNo);
        }else {
            CommonUtils.getLoading(this, "加载中.....");
            String name = getIntent().getStringExtra("name");
            String birthday = getIntent().getStringExtra("birth");
            String sexs = getIntent().getStringExtra("sex");
            String vipType = getIntent().getStringExtra("vipType");
            String phone = getIntent().getStringExtra("phone");
            if (null != vipType){
                if (vipType.equals("是")){
                    Drawable drawable = getResources().getDrawable(R.mipmap.vip);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    nameTv.setCompoundDrawables(null,null,drawable,null);
                }
            }
            nameTv.setText(name);
            String birth = (null == birthday ? "暂无" : birthday.substring(0,11));
            String sex = (null == sexs ? "暂无" : sexs);
            age = (null == birthday ? "暂无" : String.valueOf(CommonUtils.judgeBirth(birthday.substring(0, 11))));
            birthTv.setText("出生年月 :" + birth);
            ageTv.setText(getResources().getString(R.string.age) + age);
            sexTv.setText(getResources().getString(R.string.sex) + sex);
            if (!"男".equals(sex)){
                headImg.setImageDrawable(getResources().getDrawable(R.mipmap.girl));
            }else {
                headImg.setImageDrawable(getResources().getDrawable(R.mipmap.boy));
            }
            phoneTv.setText("号码 :" + null == phone ? "暂无" : phone);
            init();
            initTabLineWidth();
            loadPatientData(icNo);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myPhone = (String) SPUtils.get(getApplicationContext(),"doc_phone","phone","");
    }

    private void init() {
        reportListFragment = new ReportListFragment();
        recordFragment = new RecordFragment();
        fragmentList.add(reportListFragment);
        fragmentList.add(recordFragment);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursorImg
                        .getLayoutParams();
                if (currentIndex == 0 && position == 0) {// 0->1
                    lp.leftMargin = (int) (offset * (screenWidth / 2) + currentIndex
                            * (screenWidth / 2));
                } else if (currentIndex == 1 && position == 0) { // 1->0
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth / 2) + currentIndex
                            * (screenWidth / 2));
                }
                cursorImg.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTvBackG();
                switch (position) {
                    case 0:
                        tabOneTv.setTextColor(getResources().getColor(R.color.isselect));
                        break;
                    case 1:
                        tabTwoTv.setTextColor(getResources().getColor(R.color.isselect));
                        break;
                }
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursorImg
                .getLayoutParams();
        lp.width = screenWidth / 2;
        cursorImg.setLayoutParams(lp);
    }

    private void resetTvBackG() {
        tabOneTv.setTextColor(Color.BLACK);
        tabTwoTv.setTextColor(Color.BLACK);
    }

    public void gone(String id,String type, String source) {
        if (type.equals("2")){
            reportListLayout.setVisibility(View.GONE);//test 2
            typeLayout.setVisibility(View.VISIBLE);
            typeThreeImg.setVisibility(View.GONE);
            loadCommonData(id,type,source);
        }else if (type.equals("1")){
            reportListLayout.setVisibility(View.GONE);//test 1
            recordLayout.setVisibility(View.VISIBLE);
            typeThreeImg.setVisibility(View.GONE);
            loadCommonData(id,type,source);
        }else {
            reportListLayout.setVisibility(View.GONE);//test 2
            typeLayout.setVisibility(View.GONE);
            typeThreeImg.setVisibility(View.VISIBLE);
            loadCommonData(id,type,source);
        }

    }

    @OnClick({R.id.back_btn, R.id.home_btn, R.id.call_btn,R.id.show_big_img,R.id.tab_one,R.id.tab_two})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (BACK_STATE == 0) {
                    finish();
                } else if (BACK_STATE == 1) {
                    ageTv.setText(getResources().getString(R.string.age) + age);
                    reportListLayout.setVisibility(View.VISIBLE);
                    recordLayout.setVisibility(View.GONE);
                    typeLayout.setVisibility(View.GONE);
                    typeThreeImg.setVisibility(View.GONE);
                    BACK_STATE = 0;
                }
                break;
            case R.id.home_btn:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.call_btn:

                final AlertDialog dialog = new AlertDialog.Builder(PatientDataActivity.this)
                        .setTitle("提示")
                        .setMessage("".equals(myPhone) ? "请先设置本机号码" : "是否拨打电话?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface  dialogInterface, int i) {
                                if (TextUtils.isEmpty(myPhone)){
                                    startActivity(new Intent(PatientDataActivity.this, UserinfoActivity.class));
                                    return;
                                }
                                RequestParams params = new RequestParams(HttpUrls.GET_PHONE);
                                params.addBodyParameter("all","true");
                                XHttpRequest.getInstance().httpGet(PatientDataActivity.this, params, new MyCallBack() {
                                    @Override
                                    public void onCallBack(boolean isSuccess, Object obj) {
                                        if (!isSuccess){
                                            return;
                                        }
                                        try {
                                            JSONObject response = new JSONObject(obj.toString());
                                            final Boolean isCall = response.getJSONObject("result").getJSONObject("hide_telephone").getBoolean("hide_doctor_telephone");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String result = PhoneCallBack.startCall(getApplicationContext(),myPhone,phonenum, isCall);
                                                    Message msg = new Message();
                                                    msg.obj = result;
                                                    handler.sendMessage(msg);
                                                }
                                            }).start();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create();
                dialog.show();

                break;
            case R.id.show_big_img:
                if (null == listArray)
                    return;
                Intent intent = new Intent(this, ShowReportImgActivity.class);
                intent.putExtra("big_img", listArray.toString());
                startActivity(intent);
                break;
            case R.id.tab_one:
                setCurrentItem(0);
                break;
            case R.id.tab_two:
                setCurrentItem(1);
                break;
        }
    }
    private void setCurrentItem(int position){
        mViewPager.setCurrentItem(position);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (BACK_STATE == 0) {
                finish();
            } else if (BACK_STATE == 1) {
                ageTv.setText(getResources().getString(R.string.age) + age);
                reportListLayout.setVisibility(View.VISIBLE);
                recordLayout.setVisibility(View.GONE);
                typeLayout.setVisibility(View.GONE);
                typeThreeImg.setVisibility(View.GONE);
                BACK_STATE = 0;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadCommonData(final String id, final String type, String source){
        CommonUtils.getLoading(this, "加载中.....");
        RequestParams params = new RequestParams(HttpUrls.REPORT);
        params.addBodyParameter("method", "info");
        params.addBodyParameter("type",type);
        params.addBodyParameter("id",id);
        params.addBodyParameter("source",source);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
//                requestNum = requestNum + 1;
//                if (requestNum == 2){
                    CommonUtils.dismiss();
//                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONObject result = response.getJSONObject("result");

                    if (type.equals("1")){
                        nameTv.setText(result.getString("PatName"));
                        ageTv.setText(getResources().getString(R.string.age) + result.getString("Age"));
                        sexTv.setText(getResources().getString(R.string.sex) + (result.getInt("Sex") == 2 ? "女" : "男"));
                        if (!"男".equals(sexTv.getText().toString())){
                            headImg.setImageDrawable(getResources().getDrawable(R.mipmap.girl));
                        }else {
                            headImg.setImageDrawable(getResources().getDrawable(R.mipmap.boy));
                        }
                        reportNameTv.setText(result.getString("slavename") + getResources().getString(R.string._normal));
                        List<ReportDate> list = GsonUtils.getGson().fromJson(result.getJSONArray("list").toString(), new TypeToken<List<ReportDate>>(){}.getType());
                        ReportCommonAdapter adapter = new ReportCommonAdapter(PatientDataActivity.this, list);
                        commonListView.setAdapter(adapter);

                    }else if (type.equals("2")){
                        nameTv.setText(result.getString("PatName"));
                        ageTv.setText(getResources().getString(R.string.age) + result.getString("Age"));
                        sexTv.setText(getResources().getString(R.string.sex) + (result.getInt("Sex") == 2 ? "女" : "男"));
                        if (!"男".equals(sexTv.getText().toString())){
                            headImg.setImageDrawable(getResources().getDrawable(R.mipmap.girl));
                        }else {
                            headImg.setImageDrawable(getResources().getDrawable(R.mipmap.boy));
                        }
                        String ItemResult1 = result.getString("ItemResult1");
                        String str = Base64Utils.decode(ItemResult1.getBytes(),"gb2312");
                        typeTwoTv.setText(str);
                        listArray = result.getJSONArray("list");
                    }else {
                        HttpLoadImg.downLoadImg(PatientDataActivity.this,result.getString("imgUrl"),typeThreeImg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void loadPatientData(String icNo){
        RequestParams params = new RequestParams(HttpUrls.JIUZHEN_DATA);
        params.addBodyParameter("method", "Visitdetail");
        params.addBodyParameter("ic_no", icNo);
        params.addBodyParameter("visit_date1", "2000-01-01 00:00:00");
        params.addBodyParameter("visit_date2", CommonUtils.getNowDate() + " 23:59:59");
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                requestNum = requestNum + 1;
                Log.d("result", "3333");
                if (requestNum == 2){
                    CommonUtils.dismiss();
                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    LIST = GsonUtils.getGson().fromJson(response.getJSONArray("result").toString(), new TypeToken<List<PatientData>>(){}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getPatientData(String no){
        RequestParams params = new RequestParams(HttpUrls.QUERY_DATA);
        params.addBodyParameter("method", "IC");
        params.addBodyParameter("cardNo",no);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                requestNum = requestNum + 1;
                Log.d("result", "3333");
                if (requestNum == 2){
                    CommonUtils.dismiss();
                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONArray respnse = new JSONObject(obj.toString()).getJSONArray("result");
                    if (respnse.toString().equals("[]")){
//                        sexTv.setText( "暂无" );
                        birthTv.setText("出生年月 :" + "暂无" );
//                        ageTv.setText("年龄 :" +  "暂无" );
                        phoneTv.setText("号码 :暂无");
                        return;
                    }
                    JSONObject result = respnse.getJSONObject(0);
                    phone = result.getString("PHONE");
                    if (phone.equals("")){
                        callBtn.setVisibility(View.GONE);
                    }else {
                        callBtn.setVisibility(View.VISIBLE);
                    }
                    phonenum = phone;
                    String sex = result.getString("PATIENT_SEX");
                    String birth = result.getString("BIRTHDAY");
                    sexTv.setText(getResources().getString(R.string.sex) + ("".equals(sex) ? "暂无" : sex));
                    if (!"男".equals(sexTv.getText().toString())){
                        headImg.setImageDrawable(getResources().getDrawable(R.mipmap.girl));
                    }else {
                        headImg.setImageDrawable(getResources().getDrawable(R.mipmap.boy));
                    }
                    birthTv.setText("出生年月 :" + CommonUtils.judgeTime(birth.equals("") ? "暂无" : birth));
                    ageTv.setText("年龄 :" + (birth.equals("") ? "暂无" : CommonUtils.judgeBirth(birth.substring(0,11))));
                    phoneTv.setText("号码 :" + phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
