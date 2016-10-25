package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.activity.PatientDataActivity;
import com.rimi.angel.angeldoctor.bean.Report;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/2.
 */
public class MainListAdapter extends BaseAdapter  {

    private Context mContext;
    private Resources res;
    private Drawable vip;
    private List<Report> list;

    public MainListAdapter(Context mContext, List<Report> list){
        this.mContext = mContext;
        res = mContext.getResources();
        this.list = list;
//        vip = res.getDrawable(R.mipmap.vip);
//        vip.setBounds(0,0,vip.getMinimumWidth(),vip.getMinimumHeight());
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
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_main,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if (!"".equals(list.get(position).getReport_time())){
            holder.chubaogaoImg.setVisibility(View.VISIBLE);
        }
        holder.inspectNameTv.setText(res.getString(R.string.inspect_name) + list.get(position).getPatName() );
        holder.inspectTitleTv.setText(res.getString(R.string.inspect_title) + list.get(position).getReport_Name());
        holder.inspectTimeTv.setText(res.getString(R.string.inspect_time) + CommonUtils.judgeTime(list.get(position).getCheck_time()));
        holder.commonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,PatientDataActivity.class);
                intent.putExtra("code",2);
                intent.putExtra("name",list.get(position).getPatName());
                intent.putExtra("code_id",list.get(position).getId());
                intent.putExtra("type",list.get(position).getType());
                intent.putExtra("icNo",list.get(position).getCardNo());
                intent.putExtra("source", list.get(position).getSource());
                mContext.startActivity(intent);
            }
        });
        return view;
    }
    class ViewHolder{
        @Bind(R.id.inspect_name_tv)
        TextView inspectNameTv;
        @Bind(R.id.inspect_title_tv)
        TextView inspectTitleTv;
        @Bind(R.id.inspect_time_tv)
        TextView inspectTimeTv;
        @Bind(R.id.chubaogao_img)
        ImageView chubaogaoImg;
        @Bind(R.id.common_btn)
        ViewGroup commonBtn;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
