package com.rimi.angel.angeldoctor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/5/31.
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.cancelBtn)
    Button cancelBtn;
    @Bind(R.id.finishBtn)
    Button finishBtn;
    @Bind(R.id.oldpassword_edt)
    EditText oldPasswordEdt;
    @Bind(R.id.newpassword_edt)
    EditText newPasswordEdt;
    @Bind(R.id.again_input_edt)
    EditText againInputEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.cancelBtn,R.id.finishBtn})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.cancelBtn:
                finish();
                break;
            case R.id.finishBtn:
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
