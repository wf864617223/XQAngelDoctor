package com.rimi.angel.angeldoctor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/6.
 */
public class MsgCommonActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.content_tv)
    TextView contentTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_common);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        titleTv.setText(title);
        contentTv.setText(content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    @OnClick({R.id.back_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
