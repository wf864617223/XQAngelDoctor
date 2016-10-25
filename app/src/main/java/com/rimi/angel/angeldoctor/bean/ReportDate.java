package com.rimi.angel.angeldoctor.bean;

/**
 * Created by Android on 2016/6/16.
 */
public class ReportDate {

    private String ItemName;

    private String RESULT;

    private String REFERENCERANGE;

    private String UNIT;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getREFERENCERANGE() {
        return REFERENCERANGE;
    }

    public void setREFERENCERANGE(String REFERENCERANGE) {
        this.REFERENCERANGE = REFERENCERANGE;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }
}
