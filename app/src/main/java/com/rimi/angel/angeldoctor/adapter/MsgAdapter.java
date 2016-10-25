package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.bean.MsgData;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/16.
 */
public class MsgAdapter extends BaseAdapter {

    private Context mContext;
    private List<MsgData> mList;
    private Resources res;
    private OnAdapterClickListener listener;
    private OnAdapterLongClickListener longListener;

    public MsgAdapter(Context mContext, List<MsgData> mList){
        this.mContext = mContext;
        this.mList = mList;
        res = mContext.getResources();
    }
    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public MsgData getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if ("".equals(getItem(position).getReadTime())){
            holder.continer.setBackgroundColor(res.getColor(R.color.line));
        }
        holder.promptTv.setText(getItem(position).getType());
        holder.contentTv.setText(/*getItem(position).getTitle() + " : " +*/ getItem(position).getContent());
        holder.msgTiemTv.setText(CommonUtils.transforTime(getItem(position).getSendTime()));
        holder.commonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
        holder.commonLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longListener != null){
                    longListener.onLongClick(position);
                }
                return false;
            }
        });
        return view;
    }
    class ViewHolder{
        @Bind(R.id.prompt_tv)
        TextView promptTv;
        @Bind(R.id.content_tv)
        TextView contentTv;
        @Bind(R.id.msg_time_tv)
        TextView msgTiemTv;
        @Bind(R.id.continer)
        ViewGroup continer;
        @Bind(R.id.checkbox)
        CheckBox mCheckbox;
        @Bind(R.id.common_layout)
        ViewGroup commonLayout;
        private ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
    public void setOnAdapterClickListener(OnAdapterClickListener listener){
        this.listener = listener;
    }
    public interface OnAdapterClickListener{
        void onClick(int position);
    }
    public void setOnAdapterLongClickListener(OnAdapterLongClickListener longListener){
        this.longListener = longListener;
    }
    public interface OnAdapterLongClickListener{
        void onLongClick(int position);
    }
}
