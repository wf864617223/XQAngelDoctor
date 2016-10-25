package com.rimi.angel.angeldoctor.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/5/31.
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.cancelBtn)
    ImageButton cancelBnt;
    @Bind(R.id.commitBtn)
    Button commitBtn;
    @Bind(R.id.input_feedback_edt)
    EditText inputFeedbackEdt;
    @Bind(R.id.edit_state_tv)
    TextView stateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        inputFeedbackEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 140){
                    Toast.makeText(FeedbackActivity.this,"已超出限制字数",Toast.LENGTH_SHORT).show();
                }
                stateTv.setText(editable.length() + "/140");
            }
        });
    }
    @OnClick({R.id.cancelBtn,R.id.commitBtn})
    @Override
    public void onClick(View view) {
        String content = inputFeedbackEdt.getText().toString();
        int id = view.getId();
        switch (id){
            case R.id.cancelBtn:
                finish();
                break;
            case R.id.commitBtn:
                if (TextUtils.isEmpty(content) || "".equals(content.trim())){
                    Toast.makeText(this,"请输入反馈内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                CommonUtils.getLoading(this, "提交中.....");
                commitFeedback(inputFeedbackEdt.getText().toString());
                break;
        }
    }
    private void commitFeedback(String content){
        RequestParams params = new RequestParams(HttpUrls.FEEDBACK);
        params.addBodyParameter("title", "标题");
        params.addBodyParameter("content", content);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    Toast.makeText(FeedbackActivity.this, "提交不成功", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
