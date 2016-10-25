package com.rimi.angel.angeldoctor.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by Android on 2016/8/2.
 */
public class ErrorHandler {

    private Context mContext;

    public ErrorHandler(Context mContext){
        this.mContext = mContext;
    }
    public  Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(mContext,"呼叫失败" + msg.obj.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    });
}
