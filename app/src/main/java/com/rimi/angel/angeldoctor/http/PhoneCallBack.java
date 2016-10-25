package com.rimi.angel.angeldoctor.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Android on 2016/6/24.
 */
public class PhoneCallBack {
    //子账号
    private static String subAccountSid = "34d8f76c3e8e11e6bb9bac853d9f54f2";
    //子账号Token
    private static String subAccountToken = "90ea8943df9af651ae51a9b6e403c41a";
    //应用ID
    private static String appId = "8a216da855826478015595c6f9be0e03";

    private static String showPhone;

    public static String startCall(Context mContext,String from,String to, boolean isCall){
//        CommonUtils.getLoading(mContext,"正在回拨请稍后...");
        if (isCall){//判断用户需求是否是通过容联云OR直接拨打
            showPhone = (String) SPUtils.get(mContext, BaseActivity.SAVE_SHOW_PHONE,BaseActivity.SHOW_PHONE,"");
            HashMap<String, Object> result = null;
            CCPRestSDK restAPI = new CCPRestSDK();
            restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
            restAPI.setSubAccount(subAccountSid, subAccountToken);// 初始化子帐号和子帐号TOKEN
            restAPI.setAppId(appId);// 初始化应用ID13808013567
//        result = restAPI.callback("13219865300", "15928414675", showPhone, showPhone, "", "", "","","","", "", "", "", "");
            result = restAPI.callback(from, to, showPhone, showPhone, "", "", "","","","", "", "", "", "");
            System.out.println("SDKTestCallback result=" + result);
            if("000000".equals(result.get("statusCode"))){
                //正常返回输出data包体信息（map）
                HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for(String key:keySet){
                    Object object = data.get(key);
                    System.out.println(key +" = "+object);
                }
                return "拨号成功";
            }else{
                //异常返回输出错误码和错误信息
                System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
                return result.get("statusMsg").toString();
            }
        }else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + to));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return "拨号成功";
        }

    }
    private static boolean getPhone(Context mContext){
        final boolean[] isTrue = {true};
        RequestParams params = new RequestParams(HttpUrls.GET_PHONE);
        params.addBodyParameter("all","true");
        XHttpRequest.getInstance().httpGet(mContext, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    isTrue[0] = response.getJSONObject("result").getJSONObject("hide_telephone").getBoolean("hide_doctor_telephone");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return isTrue[0];
    }


}
