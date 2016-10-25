package com.rimi.angel.angeldoctor.utils;

/**
 * 时间倒序排序
 * Created by Administrator on 2016/6/27.
 */

import com.rimi.angel.angeldoctor.bean.PatientData;

import java.util.Comparator;

public class SortTool implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        PatientData visitList1 = (PatientData) o1;
        PatientData visitList2 = (PatientData) o2;
        int flag = visitList2.getVISIT_DATE().compareTo(visitList1.getVISIT_DATE());
        return flag;
    }

}
