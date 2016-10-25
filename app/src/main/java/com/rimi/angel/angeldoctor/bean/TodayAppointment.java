package com.rimi.angel.angeldoctor.bean;

/**
 * Created by Android on 2016/6/14.
 */
public class TodayAppointment {
    private String Name;

    private String BirthDate;

    private int BWeek;

    private String Phone;

    private String MakeDate;

    private String IcNo;

    private String Sex;

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getIcNo() {
        return IcNo;
    }

    public void setIcNo(String icNo) {
        IcNo = icNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public int getBWeek() {
        return BWeek;
    }

    public void setBWeek(int BWeek) {
        this.BWeek = BWeek;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMakeDate() {
        return MakeDate;
    }

    public void setMakeDate(String makeDate) {
        MakeDate = makeDate;
    }
}
