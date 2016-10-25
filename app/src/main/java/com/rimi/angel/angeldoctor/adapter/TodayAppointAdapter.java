package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.NowAppointmentActivity;
import com.rimi.angel.angeldoctor.activity.PatientDataActivity;
import com.rimi.angel.angeldoctor.bean.TodayAppointment;
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
public class TodayAppointAdapter extends BaseAdapter {

    private Context mContext;
    private List<TodayAppointment> list;
    private Resources res;
    private Map<Integer,TodayAppointment> map;

    public  TodayAppointAdapter(Context mContext, List<TodayAppointment> list){
        this.mContext = mContext;
        this.list = list;
        res = mContext.getResources();
//        map = NowAppointmentActivity.map;
        map = new HashMap<Integer, TodayAppointment>();
    }
    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public TodayAppointment getItem(int i) {
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
        holder.sexTv.setText(res.getString(R.string.sex) + (getItem(position).getSex() == null ? "暂无" : getItem(position).getSex()));
        holder.patNameTv.setText(getItem(position).getName());
        holder.appointTimeTv.setText(res.getString(R.string.appoint_time) + " :" + getItem(position).getMakeDate());
        holder.ageTv.setText(res.getString(R.string.age) + (getItem(position).getBirthDate() == null ? "暂无" : CommonUtils.judgeBirth(getItem(position).getBirthDate())) + "   怀孕" + getItem(position).getBWeek() + "周");
        holder.headImg.setImageDrawable(res.getDrawable(R.mipmap.girl));
        String icNo = map.get(position).getIcNo();
        if (!"".equals(icNo)){
            holder.nextBtn.setVisibility(View.VISIBLE);
        }else {
            holder.nextBtn.setVisibility(View.GONE);
        }
        holder.commonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String icNo = getItem(position).getIcNo();
                if (null == icNo || "".equals(icNo)){
                    return;
                }
//                holder.nextBtn.setVisibility(View.VISIBLE);
                Intent intent = new Intent(mContext,PatientDataActivity.class);
                intent.putExtra("icNo",icNo);
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("birth",list.get(position).getBirthDate());
                intent.putExtra("phone",list.get(position).getPhone());
                intent.putExtra("sex", getItem(position).getSex() == null ? "暂无" : getItem(position).getSex());
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
