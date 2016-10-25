package com.rimi.angel.angeldoctor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.rimi.angel.angeldoctor.utils.SPUtils;

/**
 * Created by Android on 2016/5/31.
 */
public class BaseActivity extends FragmentActivity {
    //登录信息
    public static final String LOGIN_DATA = "loginData";
    //token
    public static final String SAVE_TOKEN = "save_token";

    public static final String DOC_CODE = "doctor_code";

    public static final String DOC_NAME = "doctor_name";

    public static final String PHONE = "doc_phone";

    public static final String IS_NOTIFY = "is_notify";

    public static final String SAVE_SHOW_PHONE = "show_phone";

    public static final String SHOW_PHONE = "phone";

    public static final String VOICE = "voice";

    public static final String DATA = "data";

    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String IC_NO = "icNo";

    public static final String VIP_TYPE = "1";

    public static final String NOT_VIP_TYPE = "2";

    public static final String ALL_TYPE = "3";

    public static final String USER_STATE = "user_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"5DfG0EPzGtIETs4NDT6LgHdY");
        if(networkInfo == null || !networkInfo.isAvailable()){
            Toast.makeText(this,"当前无网络连接",Toast.LENGTH_SHORT).show();
        }
        else {

        }
        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
    //获取医生code
    public String getDocCode(){
        return (String) SPUtils.get(this,DOC_CODE,CODE,"");
    }
    //获取医生姓名
    public String getDocName(){
        return (String) SPUtils.get(this,DOC_NAME,NAME,"");
    }
    //获取token
    public String getToken(){
        return (String) SPUtils.get(this,SAVE_TOKEN,"tokenID","");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
