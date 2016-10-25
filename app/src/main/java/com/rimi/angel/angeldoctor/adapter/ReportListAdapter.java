package com.rimi.angel.angeldoctor.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.fragment.ReportListFragment;
import com.rimi.angel.angeldoctor.utils.CommonUtils;

import org.json.JSONArray;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/6/15.
 */
public class ReportListAdapter extends BaseAdapter {

    private Context mContext;
    private JSONArray result;
    private Resources res;
    private Drawable drawable;

    public ReportListAdapter(Context mContext, JSONArray result){
        this.mContext = mContext;
        this.result = result;
        res = mContext.getResources();
    }

    @Override
    public int getCount() {
        return null == result ? 0 : result.length();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_reportlist, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        String resss = result.optJSONObject(position).optString("check_time");
        String reportTime = result.optJSONObject(position).optString("report_time");
        holder.explainTv.setText(res.getString(R.string.inspect_time) + CommonUtils.judgeTime(resss));
//        holder.timeTv.setText(res.getString(R.string.report_time) + reportTime);
        holder.titleTv.setText(res.getString(R.string.inspect_title) + result.optJSONObject(position).optString("report_Name"));
//        if (position % 3 == 0){
//            holder.resultTv.setText(res.getString(R.string.normal));
//        }else if (position % 3 == 1){
//            holder.resultTv.setText(res.getString(R.string.unnormal));
//            drawable = res.getDrawable(R.mipmap.error);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.resultTv.setCompoundDrawables(null, drawable, null, null);
//        }else {
//            holder.resultTv.setText(res.getString(R.string.un_deal));
//            drawable = res.getDrawable(R.mipmap.undeal);
//
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.resultTv.setCompoundDrawables(null, drawable, null, null);
//        }

        return view;
    }
    class ViewHolder{
        @Bind(R.id.inspect_title_tv)
        TextView titleTv;
        @Bind(R.id.inspect_explain_tv)
        TextView explainTv;
        @Bind(R.id.inspect_time_tv)
        TextView timeTv;
//        @Bind(R.id.report_result_tv)
//        TextView resultTv;
        private ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
