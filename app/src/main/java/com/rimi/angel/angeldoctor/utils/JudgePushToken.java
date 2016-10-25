package com.rimi.angel.angeldoctor.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;

import org.xutils.http.RequestParams;

/**
 * Created by Android on 2016/6/21.
 */
public class JudgePushToken {

    public static void judgeToken(final Context mContext, String token){
        RequestParams params = new RequestParams(HttpUrls.JUDGE_PUSH_TOKEN);
        params.addBodyParameter("token", token);
        XHttpRequest.getInstance().httpPost(mContext, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
//                    Toast.makeText(mContext, "推送token同步失败", Toast.LENGTH_SHORT).show();
                    return;
                }
//                Toast.makeText(mContext, "推送token同步成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
