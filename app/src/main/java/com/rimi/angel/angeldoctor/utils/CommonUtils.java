package com.rimi.angel.angeldoctor.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.rimi.angel.angeldoctor.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Android on 2016/5/31.
 * 常用工具类
 */
public class CommonUtils {
    private static ProgressDialog dialog;

    public static String getNowDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String nowDate = format.format(date);
        return nowDate;
    }
    public static String getNowDateCommon(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String nowDate = format.format(date);
        return nowDate;
    }

    /**
     * 年月日转换成月日
     * @param date
     * @return
     */
    public static String transforMonth(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date date1 = format.parse(date, pos);
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
        String str = format1.format(date1);
        return str;
    }
    /**
     * 转换时间戳
     * @param time
     * @return
     */
    public static String transforTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        String nowDate = format.format(date);
        return nowDate;
    }
    public static String judgeTime(String date){
        int length = date.length();
        int start = date.indexOf(" ") + 1;
        if (start == 1 ){
            return date + "";
        }
        String mustStr = date.substring(0, start - 1);
        String judeStr = date.substring(start, length);
        if (judeStr.equals("0:00:00") || judeStr.equals("00:00:00")){
            judeStr = "";
            return mustStr + judeStr;
        }
        return mustStr + judeStr;
    }

    /**
     * 获取当前的上一周的具体时间
     * @return
     */
    public static String getbeforeWeekDate(){
        Calendar calendar = Calendar.getInstance();
        String beforeWeek = null;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day > 7){
            beforeWeek = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day - 7);
        }else {
           if (month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                beforeWeek = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(day - 7 + 30);
            }else if (month == 1){
                beforeWeek = String.valueOf(year - 1) + "-" + String.valueOf(12) + "-" + String.valueOf(day - 7 + 31);
            }else if (month == 3){
                if (year % 4 == 0){
                    beforeWeek = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(day - 7 + 29);
                }else {
                    beforeWeek = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(day - 7 + 28);
                }
            }else {
                beforeWeek = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(day - 7 + 31);
            }
        }
        return beforeWeek;
    }

    /**
     * 转换成年龄
     * @param birth
     * @return
     */
    public static int judgeBirth(String birth){
        if (null == birth || "".equals(birth)){
            return 0;
        }
        int age ;
        int year = Integer.parseInt(birth.substring(0,4));
        int month = Integer.parseInt(birth.substring(5,7));
        int day = Integer.parseInt(birth.substring(8,10));

        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        age = Math.abs(nowYear - year);
        if (nowMonth < month){
            age = age - 1;
        }else if (nowMonth == month){
            if (nowDay < day){
                age = age - 1;
            }
        }
        return age;
    }

    /**
     * 判断是否是double
     * @param str
     * @return
     */
    public static boolean isDouble(String str){
        boolean bCheckResult = true;
        try
        {
            Double dCheckValue = Double.parseDouble(str);
            if (dCheckValue instanceof Double == false)
            {
                bCheckResult = false;
            }
        }
        catch(NumberFormatException e)
        {
            bCheckResult = false;
        }
        return bCheckResult;
    }
    public static void getLoading(Context mContext, String str){
        dialog = new ProgressDialog(mContext, R.style.NoBackDialog);
//        dialog.setCancelable(false);
        dialog.setMessage(null == str  ? "" : str);
        dialog.show();
    }
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
	   /*
	    * 可接受的电话格式有：
	    */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
	   /*
	    * 可接受的电话格式有：
	    */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if(matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        boolean eee = isValid;
        return isValid;
    }

    public static void dismiss(){
        dialog.dismiss();
    }
    public static void setDialogMsg(String str){
        dialog.setMessage(str);
    }
}
