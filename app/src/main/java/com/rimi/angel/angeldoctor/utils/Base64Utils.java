package com.rimi.angel.angeldoctor.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Android on 2016/6/22.
 */
public class Base64Utils {
    public static String decode(byte[] bt, String codeType) throws UnsupportedEncodingException {
        String result = new String(Base64.decode(bt, Base64.DEFAULT),codeType);
        return result;
    }
}
