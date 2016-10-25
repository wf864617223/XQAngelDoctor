package com.rimi.angel.angeldoctor.custom;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Android on 2016/6/22.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
