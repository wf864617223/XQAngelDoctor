package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.PatientDataActivity;
import com.rimi.angel.angeldoctor.bean.TodayAppointmentTwo;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/14.
 */
public class TodayAppointTwoAdapter extends BaseAdapter {

    private Context mContext;
    private List<TodayAppointmentTwo> list;
    private Resources res;
    private Map<Integer,TodayAppointmentTwo> map;

    public TodayAppointTwoAdapter(Context mContext, List<TodayAppointmentTwo> list){
        this.mContext = mContext;
        this.list = list;
        res = mContext.getResources();
        map = new HashMap<Integer, TodayAppointmentTwo>();
    }
    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public TodayAppointmentTwo getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        map.put(position, getItem(position));
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_today_appoint, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.sexTv.setText(res.getString(R.string.sex) + (getItem(position).getSEX() == null ? "暂无" : getItem(position).getSEX()));
        holder.patNameTv.setText(getItem(position).getNAME());
        holder.appointTimeTv.setText("科室 : " + getItem(position).getDEPT_NAME());
        holder.ageTv.setText(res.getString(R.string.age) + (getItem(position).getBIRTHDAY() == null ? "暂无" : CommonUtils.judgeBirth(getItem(position).getBIRTHDAY())));
        holder.headImg.setImageDrawable(res.getDrawable(R.mipmap.girl));
        String icNo = map.get(position).getIC_CODE();
        if (!"".equals(icNo)){
            holder.nextBtn.setVisibility(View.VISIBLE);
        }else {
            holder.nextBtn.setVisibility(View.GONE);
        }
        holder.commonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String icNo = getItem(position).getIC_CODE();
                if (null == icNo || "".equals(icNo)){
                    return;
                }
//                holder.nextBtn.setVisibility(View.VISIBLE);
                Intent intent = new Intent(mContext,PatientDataActivity.class);
                intent.putExtra("icNo",icNo);
                intent.putExtra("name",list.get(position).getNAME());
                intent.putExtra("birth",list.get(position).getBIRTHDAY());
                intent.putExtra("phone",list.get(position).getPHONE());
                intent.putExtra("sex", getItem(position).getSEX() == null ? "暂无" : getItem(position).getSEX());
                mContext.startActivity(intent);
            }
        });
        return view;
    }
    class ViewHolder{
        @Bind(R.id.patient_name_tv)
        TextView patNameTv;
        @Bind(R.id.appointtime_tv)
        TextView appointTimeTv;
        @Bind(R.id.age_tv)
        TextView ageTv;
        @Bind(R.id.headimg)
        CircleImageView headImg;
        @Bind(R.id.common_layout)
        ViewGroup commonLayout;
        @Bind(R.id.sex_tv)
        TextView sexTv;
        @Bind(R.id.next_btn)
        ImageView nextBtn;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
