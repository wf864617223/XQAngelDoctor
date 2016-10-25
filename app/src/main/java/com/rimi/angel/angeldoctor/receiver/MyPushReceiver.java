package com.rimi.angel.angeldoctor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushMessageReceiver;

import java.util.List;

/**
 * Created by Android on 2016/6/21.
 */
public class MyPushReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Log.d("result", s2);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {
    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
    }

    @Override
    public void onMessage(Context context, String s, String s1) {
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
    }

}
