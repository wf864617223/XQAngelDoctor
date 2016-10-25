package com.rimi.angel.angeldoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hb.update.UpdateManager;
import com.rimi.angel.angeldoctor.AngelApplication;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.JudgePushToken;
import com.rimi.angel.angeldoctor.utils.SPUtils;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/6/3.
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.username_edt)
    EditText usernameEdt;
    @Bind(R.id.password_edt)
    EditText passwordEdt;
    @Bind(R.id.login_btn)
    Button loginBtn;

    private Context mContext;
    private InputMethodManager inputMethodManager;
    private static final String TAG = "LoginActivity_response";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        int isUpdate = getIntent().getIntExtra("isUpdate",3);
        if (isUpdate == 1){
            UpdateManager.newUpdate(this);
        }
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mContext = this;
    }
    @OnClick({R.id.login_btn})
    @Override
    public void onClick(View view) {
        final String username = usernameEdt.getText().toString();
        final String password = passwordEdt.getText().toString();
        switch (view.getId()){
            case R.id.login_btn:
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                inputMethodManager.hideSoftInputFromWindow(passwordEdt.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                CommonUtils.getLoading(mContext, "登录中");
                login(username, password);
                break;
        }
    }
    public void login(final String username, final String password){
        RequestParams params = new RequestParams(HttpUrls.LOGIN);
        params.addBodyParameter("method", "login");
        params.addBodyParameter("login_name", username);
        params.addBodyParameter("password", password);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    CommonUtils.dismiss();
                    Toast.makeText(mContext,"登录失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject job = new JSONObject(obj.toString());
                    JSONObject result = job.getJSONObject("result");
                    String login_name = result.getString("login_name");
                    String doctor_name = result.getString("doctor_name");
                    String token = result.getString("token");
                    judgeToken(token, login_name, login_name, doctor_name, username, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void judgeToken(final String token, String doc_code, final String login_name, final String doctor_name, final String username, final String password){
        RequestParams params = new RequestParams(HttpUrls.JUDGE_TOKNE);
        params.addBodyParameter("token",token);
        params.addBodyParameter("doctor_id",doc_code);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    Toast.makeText(mContext,"登录失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("result","ssssss");
                SPUtils.put(mContext,LOGIN_DATA,"username",username);
                SPUtils.put(mContext,LOGIN_DATA,"password",password);
                SPUtils.put(getApplicationContext(),SAVE_TOKEN, "tokenID", token);
                SPUtils.put(getApplicationContext(), DOC_CODE, CODE, login_name);
                SPUtils.put(getApplicationContext(), DOC_NAME, NAME, doctor_name);
                JudgePushToken.judgeToken(mContext, token);//同步推送token
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        usernameEdt.setText("");
        passwordEdt.setText("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AngelApplication.getInstance().exit();
            AngelApplication.getInstance().clearActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
