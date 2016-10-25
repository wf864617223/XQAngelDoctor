package com.rimi.angel.angeldoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hb.generalupdate.InitUpdateInterface;
import com.hb.update.UpdateManager;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.bean.UpdateInfo;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.JudgePushToken;
import com.rimi.angel.angeldoctor.utils.SPUtils;
import com.rimi.angel.angeldoctor.widget.ScrollbleViewPager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements InitUpdateInterface {

    @Bind(R.id.mViewpager)
    ScrollbleViewPager mViewpager;
    @Bind(R.id.viewGroup)
    ViewGroup group;

    private List<ImageView> imgList = new ArrayList<ImageView>();
    private ImageView imgs[] ;
    private ImageView tips[];
    private int[] imgIdArray;
    private Context mContext;
    private UpdateInfo info;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // To do something.
                    nextDip(0);
                    Log.d("GeneralUpdateLib", "There's no new version here.");
                    break;
                case 1:
                    // Find new version.
//                    UpdateManager.newUpdate(GuideActivity.this);
                    nextDip(1);
                    break;
                default:
                    break;

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        mContext = this;

        new  Thread(rv).start();
    }
    private Runnable rv = new Runnable() {
        @Override
        public void run() {
            boolean isUpdate = UpdateManager
                    .getUpdateInfo( GuideActivity.this,
                            "http://app.cdangel.com/checkversioncode.json",
                            true);
            if (isUpdate) {
                myHandler.sendEmptyMessage(1);
            } else {
                myHandler.sendEmptyMessage(0);
            }
        }
    };
    private void nextDip(int isUpdate){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable()){
            startActivity(new Intent(GuideActivity.this,LoginActivity.class));
            finish();
        }
        else {

        }
        getPhone();
        final String username = (String) SPUtils.get(GuideActivity.this,LOGIN_DATA,"username","");
        final String password = (String) SPUtils.get(GuideActivity.this,LOGIN_DATA,"password","");
        if (username.equals("") || username == null || password.equals("") || password == null){ //自动登录
            startActivity(new Intent(GuideActivity.this,LoginActivity.class).putExtra("isUpdate",isUpdate));
            finish();
        }else {
            login(username, password,isUpdate);
        }
    }
    private void getPhone(){
        RequestParams params = new RequestParams(HttpUrls.GET_PHONE);
        params.addBodyParameter("all","true");
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    String showPhone = response.getJSONObject("result").getJSONObject("contact_telephone").getString("show_telephone");
                    Boolean isUser = response.getJSONObject("result").getJSONObject("hide_telephone").getBoolean("hide_doctor_telephone");
                    SPUtils.put(getApplicationContext(),SAVE_SHOW_PHONE,SHOW_PHONE,showPhone);
                    SPUtils.put(getApplicationContext(),SAVE_SHOW_PHONE,USER_STATE,isUser);
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


    private void login(final String username, final String password,final  int isUpdate){
        RequestParams params = new RequestParams(HttpUrls.LOGIN);
        params.addBodyParameter("method", "login");
        params.addBodyParameter("login_name", username);
        params.addBodyParameter("password", password);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(mContext,"自动登录登录失败",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext, LoginActivity.class).putExtra("isUpdate",isUpdate));
                    finish();
                    return;
                }
                try {
                    JSONObject job = new JSONObject(obj.toString());
                    JSONObject result = job.getJSONObject("result");
                    String login_name = result.getString("login_name");
                    String doctor_name = result.getString("doctor_name");
                    String token = result.getString("token");
                    judgeToken(token, login_name, login_name, doctor_name, username, password,isUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void judgeToken(final String token, String doc_code, final String login_name, final String doctor_name, final String username, final String password, final int isUpdate){
        RequestParams params = new RequestParams(HttpUrls.JUDGE_TOKNE);
        params.addBodyParameter("token",token);
        params.addBodyParameter("doctor_id",doc_code);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(mContext,"自动登录登录失败",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext, LoginActivity.class).putExtra("isUpdate",isUpdate));
                    finish();
                    return;
                }
                SPUtils.put(mContext,LOGIN_DATA,"username",username);
                SPUtils.put(mContext,LOGIN_DATA,"password",password);
                SPUtils.put(getApplicationContext(),SAVE_TOKEN, "tokenID", token);
                SPUtils.put(getApplicationContext(), DOC_CODE, CODE, login_name);
                SPUtils.put(getApplicationContext(), DOC_NAME, NAME, doctor_name);
                JudgePushToken.judgeToken(mContext, token);
                try {
                    Thread.sleep(1000);
                    startActivity(new Intent(mContext, MainActivity.class).putExtra("isUpdate",isUpdate));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void checkUpdate() {

    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(imgs[position % imgs.length]);
        }
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(imgs[position % imgs.length], 0);
            return imgs[position % imgs.length];
        }
    }
    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }
}
