package com.rimi.angel.angeldoctor.bean;

/**
 * Created by Android on 2016/6/20.
 */
public class AppointmentData {
    private String DoctorName;

    private String DoctorCode;

    private String Date;

    private int ID;

    private int Count;

    private int SuccessCount;

    public int getSuccessCount() {
        return SuccessCount;
    }

    public void setSuccessCount(int successCount) {
        SuccessCount = successCount;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDoctorCode() {
        return DoctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        DoctorCode = doctorCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
