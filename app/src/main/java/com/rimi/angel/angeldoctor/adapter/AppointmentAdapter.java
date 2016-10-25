package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.bean.AppointmentDataTwo;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/1.
 */
public class AppointmentAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppointmentDataTwo> list;
    private Resources res;

    public AppointmentAdapter(Context mContext,List<AppointmentDataTwo> list){
        this.mContext = mContext;
        this.list = list;
        res = mContext.getResources();
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_appointment,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if (i % 2 == 0){
            holder.commonTv.setBackgroundColor(res.getColor(R.color.even_line));
            holder.countTv.setBackgroundColor(res.getColor(R.color.even_line));
            holder.keshiTv.setBackgroundColor(res.getColor(R.color.even_line));
        }else {
            holder.commonTv.setBackgroundColor(res.getColor(R.color.single_line));
            holder.countTv.setBackgroundColor(res.getColor(R.color.single_line));
            holder.keshiTv.setBackgroundColor(res.getColor(R.color.single_line));
        }
        int num = list.get(i).getNUM();
        holder.commonTv.setText(CommonUtils.transforMonth(list.get(i).getAPPOINTMENT()));
        holder.countTv.setText(num == 0 ? "暂无" : num + "");
        holder.keshiTv.setText(list.get(i).getDEPT_NAME());
        return view;
    }
    class ViewHolder{
        @Bind(R.id.common_tv)
        TextView commonTv;
        @Bind(R.id.count_tv)
        TextView countTv;
        @Bind(R.id.keshi_tv)
        TextView keshiTv;
        private ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
