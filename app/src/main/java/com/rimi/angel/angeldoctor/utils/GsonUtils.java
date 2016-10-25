package com.rimi.angel.angeldoctor.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Android on 2016/6/7.
 */
public class GsonUtils {
    public static Gson getGson(){
        return new GsonBuilder().create();
    }
}
