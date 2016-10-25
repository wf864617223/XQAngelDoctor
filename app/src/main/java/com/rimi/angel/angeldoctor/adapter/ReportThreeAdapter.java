package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/20.
 */
public class ReportThreeAdapter extends BaseAdapter {

    private Context mContext;
    private JSONObject job;

    public ReportThreeAdapter(Context mContext, JSONObject job){
        this.job = job;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return 23;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_report_three, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if (i == 0){
            holder.titleTv.setText("主治医师:");
            holder.contentTv.setText(judgeNull(job.optString("DOCTOR_NAME")));
        }else if (i == 1){
            holder.titleTv.setText("医生代号:");
            holder.contentTv.setText(judgeNull(job.optString("DOCTOR_CODE")));
        }else if (i == 2){
            holder.titleTv.setText("主述:");
            holder.contentTv.setText(judgeNull(job.optString("主述")));
        }else if (i == 3){
            holder.titleTv.setText("高危因素评估:");
            holder.contentTv.setText(judgeNull(job.optString("高危因素评估")));
        }else if (i == 4){
            holder.titleTv.setText("营养评估:");
            holder.contentTv.setText(judgeNull(job.optString("营养评估")));
        }else if (i == 5){
            holder.titleTv.setText("心里评估:");
            holder.contentTv.setText(judgeNull(job.optString("心里评估")));
        }else if (i == 6){
            holder.titleTv.setText("初步诊断:");
            holder.contentTv.setText(judgeNull(job.optString("初步诊断")));
        }else if (i == 7){
            holder.titleTv.setText("体格检查:");
            holder.contentTv.setText(judgeNull(job.optString("体格检查")));
        }else if (i == 8){
            holder.titleTv.setText("疼痛评估:");
            holder.contentTv.setText(judgeNull(job.optString("疼痛评估")));
        }else if (i == 9){
            holder.titleTv.setText("现病史:");
            holder.contentTv.setText(judgeNull(job.optString("现病史")));
        }else if (i == 10){
            holder.titleTv.setText("过敏史:");
            holder.contentTv.setText(judgeNull(job.optString("过敏史")));
        }else if (i == 11){
            holder.titleTv.setText("建议:");
            holder.contentTv.setText(judgeNull(job.optString("建议")));
        }else if (i == 12){
            holder.titleTv.setText("近期使用药物:");
            holder.contentTv.setText(judgeNull(job.optString("近期使用药物")));
        }else if (i == 13){
            holder.titleTv.setText("处理:");
            holder.contentTv.setText(judgeNull(job.optString("处理")));
        }else if (i == 14){
            holder.titleTv.setText("跌倒评估:");
            holder.contentTv.setText(judgeNull(job.optString("跌倒评估")));
        }else if (i == 15){
            holder.titleTv.setText("VISIT_NO:");
            holder.contentTv.setText(judgeNull(job.optString("VISIT_NO")));
        }else if (i == 16){
            holder.titleTv.setText("辅助检查:");
            holder.contentTv.setText(judgeNull(job.optString("辅助检查")));
        }else if (i == 17){
            holder.titleTv.setText("语音评估:");
            holder.contentTv.setText(judgeNull(job.optString("语音评估")));
        }else if (i == 18){
            holder.titleTv.setText("一般评估:");
            holder.contentTv.setText(judgeNull(job.optString("一般评估")));
        }else if (i == 19){
            holder.titleTv.setText("社会经济评估:");
            holder.contentTv.setText(judgeNull(job.optString("社会经济评估")));
        }else if (i == 20){
            holder.titleTv.setText("月经史:");
            holder.contentTv.setText(judgeNull(job.optString("月经史")));
        }else if (i == 21){
            holder.titleTv.setText("个人史:");
            holder.contentTv.setText(judgeNull(job.optString("个人史")));
        }else if (i == 22){
            holder.titleTv.setText("既往史:");
            holder.contentTv.setText(judgeNull(job.optString("既往史")));
        }

        return view;
    }
    class ViewHolder{
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.content_tv)
        TextView contentTv;
        private ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
    private String judgeNull(String str){
        return str.equals("null") ? "无" : str;
    }
}
