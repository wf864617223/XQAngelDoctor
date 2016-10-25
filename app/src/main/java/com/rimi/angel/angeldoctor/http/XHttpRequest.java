package com.rimi.angel.angeldoctor.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.log4j.spi.ThrowableInformation;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.concurrent.TimeoutException;

/**
 * Created by Android on 2016/6/6.
 * xUtils 网络请求封装
 */
public class XHttpRequest {

    private static XHttpRequest instance = null;
    public synchronized static XHttpRequest getInstance(){
        if (null == instance){
            instance = new XHttpRequest();
        }
        return instance;
    }
    //get请求
    public void httpGet(final Context mContext, RequestParams params, final MyCallBack myCallBack){
        x.http().get(params, new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("status") != 0){
                        myCallBack.onCallBack(false, result);
                        return;
                    }
                    myCallBack.onCallBack(true, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ConnectTimeoutException){
                    Toast.makeText(mContext, "网络请求超时", Toast.LENGTH_SHORT).show();
                }
                myCallBack.onCallBack(false,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    //Post请求
    public void httpPost(final Context mContext, RequestParams params, final MyCallBack myCallBack){
        x.http().post(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("result" ,result + "-------------");
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("status") != 0){
                        myCallBack.onCallBack(false, result);
                        return;
                    }
                    myCallBack.onCallBack(true, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ConnectTimeoutException){
                    Toast.makeText(mContext, "网络请求超时", Toast.LENGTH_SHORT).show();
                }
                myCallBack.onCallBack(false,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}
