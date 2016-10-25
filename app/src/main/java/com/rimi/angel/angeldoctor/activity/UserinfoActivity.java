package com.rimi.angel.angeldoctor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.kyleduo.switchbutton.SwitchButton;
import com.rimi.angel.angeldoctor.AngelApplication;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.HttpLoadImg;
import com.rimi.angel.angeldoctor.utils.SPUtils;
import com.rimi.angel.angeldoctor.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author  misy
 * 个人中心
 */
public class UserinfoActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.home_btn)
    ImageButton homeBtn;
    @Bind(R.id.change_password_view)
    ViewGroup changePasswordView;
    @Bind(R.id.feedback_view)
    ViewGroup feedbackView;
    @Bind(R.id.about_view)
    ViewGroup aboutView;
    @Bind(R.id.exit)
    Button exitBtn;
    @Bind(R.id.showmsg_img)
    ImageView showMsgImg;
    @Bind(R.id.headimg)
    CircleImageView headImg;
    @Bind(R.id.doctor_name)
    TextView docName;
    @Bind(R.id.doctor_phone)
    TextView docPhone;
    @Bind(R.id.red_point)
    CircleImageView redPointImg;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.switch_btn)
    SwitchButton switchBtn;

    private static final int RESULT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        String phone = (String) SPUtils.get(this,PHONE,"phone","");
        if (!"".equals(phone)){
            phoneTv.setText(phone);
        }
        boolean isNotify = (boolean) SPUtils.get(getApplicationContext(),IS_NOTIFY,VOICE,false);
        switchBtn.setChecked(isNotify);
//        if (isNotify){
//
//        }else {
//
//        }
        AngelApplication.getInstance().addActivity(this);
        CommonUtils.getLoading(this, "加载中.....");
        boolean hasNews = getIntent().getBooleanExtra("hasNews",true);
        if (hasNews){
            redPointImg.setVisibility(View.VISIBLE);
        }else {
            redPointImg.setVisibility(View.GONE);
        }
        loadData();
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){//打开消息免打扰
                    PushManager.setNoDisturbMode(UserinfoActivity.this,0,0,23,59);
                    SPUtils.put(getApplicationContext(),IS_NOTIFY,VOICE,true);
                }else {//关闭
                    PushManager.setNoDisturbMode(UserinfoActivity.this,0,0,0,0);
                    SPUtils.put(getApplicationContext(),IS_NOTIFY,VOICE,false);
                }
            }
        });
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.USERINFO);
        params.addBodyParameter("code", getDocCode());
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONObject result = response.optJSONObject("result");
                    docName.setText(result.optString("Name"));
                    docPhone.setText(getResources().getString(R.string.doc_code) + result.optString("code"));
                    HttpLoadImg.loadImg(UserinfoActivity.this,result.optString("url"),headImg);
                    result.optString("Position");
                    result.optString("subject");
                    result.optString("come");
                    result.optString("introduction");
                    result.optString("Qualifications");
                    result.optString("Specialty");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @OnClick({R.id.home_btn,R.id.change_password_view,R.id.feedback_view,R.id.about_view,R.id.exit,R.id.showmsg_img,R.id.headimg})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.home_btn:
//                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.change_password_view://设置本机号
//                startActivity(new Intent(this,ChangePasswordActivity.class));
                LayoutInflater factory = LayoutInflater.from(UserinfoActivity.this);//提示框
                final View view = factory.inflate(R.layout.dialog_edt, null);//这里必须是final的
                final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象
                new AlertDialog.Builder(UserinfoActivity.this)
                        .setTitle("请设置本机号码")//提示框标题
                        .setView(view)
                        .setPositiveButton("确定",//提示框的两个按钮
                                new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String phone = edit.getText().toString().trim();
                                        if (!CommonUtils.isPhoneNumberValid(phone)){
                                            Toast.makeText(UserinfoActivity.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        SPUtils.put(getApplicationContext(),PHONE,"phone",phone);
                                        phoneTv.setText(phone);
                                        //事件
                                    }
                                }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.feedback_view:// 意见反馈
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
            case R.id.about_view://关于APP
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.showmsg_img://查看消息通知
                startActivity(new Intent(this,ShowMsgActivity.class));
                break;
            case R.id.headimg://修改头像
                break;
            case R.id.exit://退出登录
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否退出登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SPUtils.clear(UserinfoActivity.this,LOGIN_DATA);
                                SPUtils.clear(UserinfoActivity.this,SAVE_TOKEN);
                                AngelApplication.getInstance().exit();
                                AngelApplication.getInstance().clearActivity();
                                startActivity(new Intent(UserinfoActivity.this,LoginActivity.class));
                                Toast.makeText(UserinfoActivity.this,"已退出",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
