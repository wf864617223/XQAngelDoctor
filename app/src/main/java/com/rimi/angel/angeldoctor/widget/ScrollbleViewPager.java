package com.rimi.angel.angeldoctor.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Android on 2016/6/13.
 */
public class ScrollbleViewPager extends ViewPager {

    private boolean scrollble = true;
    public ScrollbleViewPager(Context context) {
        super(context);
    }

    public ScrollbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble){
            return  true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
