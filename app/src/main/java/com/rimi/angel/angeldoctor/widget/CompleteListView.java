package com.rimi.angel.angeldoctor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;
public class CompleteListView extends ListView {
    private Context mAppContext;
	public CompleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CompleteListView(Context context) {
        super(context);
    }
    public CompleteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	mAppContext=(Context)getContext().getApplicationContext();
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
       
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}