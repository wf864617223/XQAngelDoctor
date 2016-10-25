package com.rimi.angel.angeldoctor.utils;

/**
 * 时间倒序排序
 * Created by Administrator on 2016/6/27.
 */

import com.rimi.angel.angeldoctor.bean.AppointmentDataTwo;

import java.util.Comparator;

public class SortTool2 implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        AppointmentDataTwo visitList1 = (AppointmentDataTwo) o1;
        AppointmentDataTwo visitList2 = (AppointmentDataTwo) o2;
        int flag = visitList2.getAPPOINTMENT().compareTo(visitList1.getAPPOINTMENT());
        return flag;
    }

}
