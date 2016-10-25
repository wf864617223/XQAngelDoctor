package com.rimi.angel.angeldoctor.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.MyPatientAdapter;
import com.rimi.angel.angeldoctor.adapter.MyViewPagerAdapter;
import com.rimi.angel.angeldoctor.bean.MyPatient;
import com.rimi.angel.angeldoctor.fragment.NotVIPCustomerFragment;
import com.rimi.angel.angeldoctor.fragment.VIPCustomeFragment;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;
import com.rimi.angel.angeldoctor.utils.SPUtils;
import com.rimi.angel.angeldoctor.widget.RefreshLayout;

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
 * 我的病人
 */
public class MyPatientActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.search_btn)
    ImageButton searchBtn;
    @Bind(R.id.cancel_btn)
    Button cancelBtn;
    @Bind(R.id.search_layout)
    ViewGroup searchLayout;
    @Bind(R.id.patientlayout)
    ViewGroup patientLayout;
    @Bind(R.id.mViewpager)
    ViewPager mViewPager;
    @Bind(R.id.tab_two)
    TextView tabTwoTv;
    @Bind(R.id.tab_three)
    TextView tabThreeTv;
    @Bind(R.id.cursor_img)
    ImageView cursorImg;
    @Bind(R.id.search_edit)
    EditText searchEdt;
    @Bind(R.id.searchListview)
    ListView searchListView;
    @Bind(R.id.tishi_tv)
    TextView tishiTv;
    @Bind(R.id.title_name)
    TextView titlenameTv;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private VIPCustomeFragment vipCustomeFragment;
    private NotVIPCustomerFragment notVIPCustomerFragment;
    private int code;
    private static int requserNum ;
    private static long START_TIEM ;
    private static long END_TIME ;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    public static String myPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypatient);
        ButterKnife.bind(this);
        getCounts("1");
        getCounts("2");
        requserNum = 0;
        code = getIntent().getIntExtra("code",0);
        if (code == MainActivity.MY_PATIENT){
            patientLayout.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            CommonUtils.getLoading(this, "加载中.....");
        }else {
            patientLayout.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
        }
        init();
        initTabLineWidth();
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    String searchName = searchEdt.getText().toString().trim();
                    if (searchName.equals("")){
                        Toast.makeText(MyPatientActivity.this,"请输入您想要查看的病人名或手机号",Toast.LENGTH_LONG).show();
                        return false;
                    }
                    CommonUtils.getLoading(MyPatientActivity.this,"加载中.....");
                    tishiTv.setText(null);
                    loadData(ALL_TYPE, searchListView, MyPatientActivity.this, null , searchName, "100",null);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myPhone = (String) SPUtils.get(this,"doc_phone","phone","");

    }

    public long loadData(final String type, final ListView listView, final Context mContext, String docCode, String patName, String pageSize, final RefreshLayout refresh){
//        if (code == MainActivity.SEARCH_PATIENT  & null == patName & patName.equals("")){
//            Toast.makeText(this, "请输入要查找的病人姓名", Toast.LENGTH_SHORT).show();
//            return;
//        }
        START_TIEM = System.currentTimeMillis();
        RequestParams params = new RequestParams(HttpUrls.LOGIN);
        params.addBodyParameter("method", "customer");
        params.addBodyParameter("doctor_code",null == docCode ? "" : docCode);
        params.addBodyParameter("context",null == patName ? "" : patName);
        params.addBodyParameter("operStatr","2000-01-01 00:00:00");
        params.addBodyParameter("operEnd", CommonUtils.getNowDate() + " 23:59:59");
        params.addBodyParameter("type",null == type ? "" : type);
        params.addBodyParameter("pageNumber","1");
        params.addBodyParameter("pageSize",pageSize);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                requserNum = requserNum + 1;
                if (requserNum == 4){
                    CommonUtils.dismiss();
                }
                if (type.equals("3")){
                    CommonUtils.dismiss();
                }
                if (refresh != null){
                    refresh.setRefreshing(false);
                }
                if (!isSuccess){
                    return;
                }
                try {
                    String strs = obj.toString();
                    Gson gson = GsonUtils.getGson();

                    JSONObject response = new JSONObject(strs);
                    List<MyPatient> patientList = new ArrayList<MyPatient>();
                    String str = response.getJSONObject("result").getJSONArray("list").toString();
                    if (str.equals("[]")){
                        Toast.makeText(MyPatientActivity.this,"未找到相关病人",Toast.LENGTH_SHORT).show();
                    }
                    patientList = gson.fromJson(str, new TypeToken<List<MyPatient>>(){}.getType());
                    MyPatientAdapter adapter = new MyPatientAdapter(mContext, patientList,Integer.parseInt(type));
                    listView.setAdapter(adapter);
                    JSONObject result = response.getJSONObject("result");
                    String counts = result.getString("totalCount");
                    if (type.equals("1")){
                        titlenameTv.setText(counts);
                    }else if (type.equals("2")){
                        titlenameTv.setText(counts);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                END_TIME = System.currentTimeMillis();
                long cart = Math.abs(END_TIME - START_TIEM);
            }
        });
        return Math.abs(END_TIME - START_TIEM);
    }

    private void init(){
        vipCustomeFragment = new VIPCustomeFragment();
        notVIPCustomerFragment = new NotVIPCustomerFragment();
        fragmentList.add(vipCustomeFragment);
        fragmentList.add(notVIPCustomerFragment);


        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursorImg.getLayoutParams();
                if (currentIndex == 0 && position ==0){ //0->1
                    lp.leftMargin = (int) (offset * (screenWidth / 2 ) + currentIndex
                            * (screenWidth / 2 ));
                }else if (currentIndex == 1 && position == 0){ //1->0
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth / 2 ) + currentIndex
                            * (screenWidth / 2));
                }
                cursorImg.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTvBackG();
                switch (position){
                    case 0:
                        tabTwoTv.setTextColor(getResources().getColor(R.color.isselect));
                        break;
                    case 1:
                        tabThreeTv.setTextColor(getResources().getColor(R.color.isselect));
                        break;

                }
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initTabLineWidth(){
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursorImg
                .getLayoutParams();
        lp.width = screenWidth / 2;
        cursorImg.setLayoutParams(lp);
    }
    private void resetTvBackG(){
        tabTwoTv.setTextColor(Color.WHITE);
        tabThreeTv.setTextColor(Color.WHITE);
    }

    @OnClick({R.id.back_btn,R.id.search_btn,R.id.cancel_btn,R.id.tab_two,R.id.tab_three})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_btn:
                searchLayout.setVisibility(View.VISIBLE);
                patientLayout.setVisibility(View.GONE);
                searchListView.setAdapter(null);
                tishiTv.setText("请输入搜索关键字如病人名或电话");
                break;
            case R.id.cancel_btn:
                if (code == MainActivity.SEARCH_PATIENT){
                    finish();
                    return;
                }
                searchLayout.setVisibility(View.GONE);
                patientLayout.setVisibility(View.VISIBLE);
                searchEdt.setText(null);
                break;
            case R.id.tab_two:
                setPagerCurrent(0);
                break;
            case R.id.tab_three:
                setPagerCurrent(1);
                break;
        }
    }
    private void setPagerCurrent(int position){
        mViewPager.setCurrentItem(position);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    private void getCounts(final String type){
        RequestParams params = new RequestParams(HttpUrls.LOGIN);
        params.addBodyParameter("method", "customer");
        params.addBodyParameter("doctor_code",getDocCode());
        params.addBodyParameter("context","");
        params.addBodyParameter("operStatr","2000-01-01 00:00:00");
        params.addBodyParameter("operEnd", CommonUtils.getNowDate() + " 23:59:59");
        params.addBodyParameter("type",null == type ? "" : type);
        params.addBodyParameter("pageNumber","1");
        params.addBodyParameter("pageSize","100");
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                requserNum = requserNum + 1;
                if (requserNum == 4){
                    CommonUtils.dismiss();
                }
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject result = new JSONObject(obj.toString()).getJSONObject("result");
                    String counts = result.getString("totalCount");
                    if (type.equals("1")){
                        tabTwoTv.setText(getResources().getString(R.string.member) + "(" + counts + ")");
                    }else {
                        tabThreeTv.setText(getResources().getString(R.string.not_member) + "(" + counts + ")");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
