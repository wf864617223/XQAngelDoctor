package com.rimi.angel.angeldoctor.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.update.UpdateManager;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.utils.CheckVersionUtils;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/5/31.
 * 关于APP
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.check_version)
    Button checkVersionBtn;
    @Bind(R.id.version_code_tv)
    TextView versionCodeTv;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // To do something.
                    Log.d("GeneralUpdateLib", "There's no new version here.");
                    Toast.makeText(AboutActivity.this,"已是最新版,无须更新",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    // Find new version.
                    UpdateManager.newUpdate(AboutActivity.this);
                    break;
                default:
                    break;

            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        versionCodeTv.setText("V " + CheckVersionUtils.getVersion(this));
    }
    @OnClick({R.id.back_btn,R.id.check_version})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.check_version:
                new  Thread(rv).start();
                break;
        }
    }


    private Runnable rv = new Runnable() {
        @Override
        public void run() {
            boolean isUpdate = UpdateManager
                    .getUpdateInfo( AboutActivity.this,
                            "http://app.cdangel.com/checkversioncode.json",
                            true);
            if (isUpdate) {
                myHandler.sendEmptyMessage(1);
            } else {
                myHandler.sendEmptyMessage(0);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
