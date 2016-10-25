package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.bean.ReportDate;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/16.
 */
public class ReportCommonAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReportDate> list;

    public ReportCommonAdapter(Context mContext, List<ReportDate> list){
        this.mContext = mContext;
        this.list = list;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_report_common, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        String repNum = list.get(position).getRESULT();
        String normalNum = list.get(position).getREFERENCERANGE();
        if (null != repNum || null != normalNum){
            boolean isDob = CommonUtils.isDouble(repNum);
            if (normalNum.contains("～") & isDob){
                int index = list.get(position).getREFERENCERANGE().indexOf("～");
                int length = list.get(position).getREFERENCERANGE().length();
                String minRepNum = list.get(position).getREFERENCERANGE().substring(0,index);
                String maxRepNum = list.get(position).getREFERENCERANGE().substring(index + 1,length);
                Double min = Double.parseDouble(minRepNum);
                Double max = Double.parseDouble(maxRepNum);
                Double req = Double.parseDouble(repNum);
                if (req < min | req > max){
                    holder.reportNumTv.setTextColor(Color.RED);
                    holder.danweiTv.setTextColor(Color.RED);
                }
            }

            holder.reportCommonName.setText(list.get(position).getItemName());
            holder.danweiTv.setText(list.get(position).getUNIT());
            holder.reportNumTv.setText("报告值 :" + list.get(position).getRESULT() );
            holder.normalNumTv.setText("(正常参考值 : " + list.get(position).getREFERENCERANGE()  + ")");
        }
        return view;
    }
    class ViewHolder{
        @Bind(R.id.report_common_name)
        TextView reportCommonName;
        @Bind(R.id.report_num_tv)
        TextView reportNumTv;
        @Bind(R.id.normal_num_tv)
        TextView normalNumTv;
        @Bind(R.id.danwei_tv)
        TextView danweiTv;
        private ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
