package com.rimi.angel.angeldoctor;

import android.app.Activity;
import android.app.Application;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/5/31.
 */
public class AngelApplication extends Application{

    private static AngelApplication instance = null;

    public static AngelApplication getInstance(){
        if (null == instance){
            instance = new AngelApplication();
        }
        return instance;
    }
    private List<Activity> activityList = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        CrashHandler.getInstance().init(getApplicationContext());
    }
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void clearActivity(){
        activityList.clear();
    }
    public void exit(){
        if (0 == activityList.size()){
            return;
        }
        for (Activity ac :activityList) {
            ac.finish();
        }
    }
}
