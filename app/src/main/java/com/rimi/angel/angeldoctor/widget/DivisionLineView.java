package com.rimi.angel.angeldoctor.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rimi.angel.angeldoctor.R;

/**
 * Created by Android on 2016/6/7.
 * 分割线
 */
public class DivisionLineView extends View {

    public DivisionLineView(Context context) {
        super(context);
    }

    public DivisionLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DivisionLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DivisionLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.WHITE);// 设置分割线颜色
        p.setStrokeWidth(2.5f);
        canvas.drawLine(10, 10, canvas.getWidth() - 10, canvas.getHeight() - 10, p);
    }
}
