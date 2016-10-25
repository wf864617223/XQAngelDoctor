package com.rimi.angel.angeldoctor.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.MyPatientActivity;
import com.rimi.angel.angeldoctor.activity.PatientDataActivity;
import com.rimi.angel.angeldoctor.activity.UserinfoActivity;
import com.rimi.angel.angeldoctor.bean.MyPatient;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.PhoneCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/15.
 */
public class MyPatientAdapter extends BaseAdapter{

    private Context mContext;
    private List<MyPatient> list;
    private int type;
    private Resources res;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    });
    public MyPatientAdapter(Context mContext, List<MyPatient> list, int type){
        this.mContext = mContext;
        this.list = list;
        this.type = type;
        res = mContext.getResources();
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_mypatient, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        String isVip = list.get(position).getREBATE_TYPE();
        if (isVip.equals("是")){
            Drawable drawable = res.getDrawable(R.mipmap.vip);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.patientNameTv.setCompoundDrawables(null,null,drawable,null);
        }
        holder.patientNameTv.setText(judgeIsNull(list.get(position).getPATIENT_NAME()));
        holder.patientPhoneTv.setText(judgeIsNull(list.get(position).getPHONE()));
        holder.patientAgeTv.setText(res.getString(R.string.age) + CommonUtils.judgeBirth(list.get(position).getBIRTHDAY()));
        holder.patientBirthTv.setText(res.getString(R.string.birthday) + (list.get(position).getBIRTHDAY() == null ? "" : (list.get(position).getBIRTHDAY()).substring(0,11)));
        holder.sexTv.setText(res.getString(R.string.sex) + judgeIsNull(list.get(position).getPATIENT_SEX()));
        if ("女".equals(list.get(position).getPATIENT_SEX())){
            holder.headerImg.setImageDrawable(res.getDrawable(R.mipmap.girl));
        }else {
            holder.headerImg.setImageDrawable(res.getDrawable(R.mipmap.boy));
        }
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("".equals(MyPatientActivity.myPhone) ? "请先设置本机号码" : "是否拨打电话?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (TextUtils.isEmpty(MyPatientActivity.myPhone)){
                                   mContext.startActivity(new Intent(mContext, UserinfoActivity.class));
                                    return;
                              }
                                RequestParams params = new RequestParams(HttpUrls.GET_PHONE);
                                params.addBodyParameter("all", "true");
                                XHttpRequest.getInstance().httpGet(mContext, params, new MyCallBack() {
                                    @Override
                                    public void onCallBack(boolean isSuccess, Object obj) {
                                        if (!isSuccess) {
                                            return;
                                        }
                                        try {
                                            JSONObject response = new JSONObject(obj.toString());
                                            final Boolean isCall = response.getJSONObject("result").getJSONObject("hide_telephone").getBoolean("hide_doctor_telephone");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String result = PhoneCallBack.startCall(mContext, MyPatientActivity.myPhone, list.get(position).getPHONE(), isCall);
                                                    Message msg = new Message();
                                                    msg.obj = result;
                                                    handler.sendMessage(msg);
                                                }
                                            }).start();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create();
                dialog.show();


            }
        });
        holder.commonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PatientDataActivity.class);
                intent.putExtra("icNo",list.get(position).getIC_NO());
                intent.putExtra("type",list.get(position).getIC_TYPE());
                intent.putExtra("name",list.get(position).getPATIENT_NAME());
                intent.putExtra("birth",list.get(position).getBIRTHDAY());
                intent.putExtra("sex",list.get(position).getPATIENT_SEX());
                intent.putExtra("phone",list.get(position).getPHONE());
                intent.putExtra("vipType", list.get(position).getREBATE_TYPE());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder{
        @Bind(R.id.patient_name_tv)
        TextView patientNameTv;
        @Bind(R.id.patient_phone_tv)
        TextView patientPhoneTv;
        @Bind(R.id.patient_age_tv)
        TextView patientAgeTv;
        @Bind(R.id.patient_birth_tv)
        TextView patientBirthTv;
        @Bind(R.id.call_btn)
        ImageButton callBtn;
        @Bind(R.id.common_layout)
        ViewGroup commonLayout;
        @Bind(R.id.patient_sex_tv)
        TextView sexTv;
        @Bind(R.id.headimg)
        ImageView headerImg;
        private ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
    private String judgeIsNull(String str){
        return str == null ? "" : str;
    }
}
