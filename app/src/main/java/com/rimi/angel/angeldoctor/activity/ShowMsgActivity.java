package com.rimi.angel.angeldoctor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.adapter.MsgAdapter;
import com.rimi.angel.angeldoctor.bean.MsgData;
import com.rimi.angel.angeldoctor.http.HttpUrls;
import com.rimi.angel.angeldoctor.http.MyCallBack;
import com.rimi.angel.angeldoctor.http.XHttpRequest;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.GsonUtils;
import com.rimi.angel.angeldoctor.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Android on 2016/5/31.
 * 查看消息通知
 */
public class ShowMsgActivity extends BaseActivity implements View.OnClickListener,MsgAdapter.OnAdapterClickListener,MsgAdapter.OnAdapterLongClickListener{

    @Bind(R.id.backBtn)
    ImageButton backBtn;
    @Bind(R.id.homeBtn)
    ImageButton homeBtn;
    @Bind(R.id.mListview)
    ListView mListView;
    @Bind(R.id.tishi_tv)
    TextView tishiTv;

    private List<MsgData> mList = null;
    private MsgAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmsg);
        ButterKnife.bind(this);
        CommonUtils.getLoading(this, "加载中.....");
        loadData();
//        mListView.setOnItemLongClickListener(this);
//        mListView.setOnItemClickListener(this);
    }
    @OnClick({R.id.backBtn,R.id.homeBtn})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                finish();
                break;
            case R.id.homeBtn:
                if (0 == mList.size() | null == mList){
                    Toast.makeText(ShowMsgActivity.this,"无消息可清除",Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog dialog = new AlertDialog.Builder(ShowMsgActivity.this)
                        .setTitle("提示")
                        .setMessage("是否清空消息列表?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    CommonUtils.getLoading(ShowMsgActivity.this,"正在清空消息列表");
                                    deleteAllMsg(null);
                                    mListView.setAdapter(new MsgAdapter(ShowMsgActivity.this, null));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                break;
        }
    }

    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.MSG_LIST);
        params.addBodyParameter("token", getToken());
        params.addBodyParameter("pageNumber", "1");
        params.addBodyParameter("pageSize", "10");
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    CommonUtils.dismiss();
                    if (!isSuccess){
                      if (response.getString("status").equals("401")){
                          homeBtn.setVisibility(View.GONE);
                      }
                       return;
                    }


                    JSONObject result = response.getJSONObject("result");
                    if (null == result){
                        return;
                    }
                    Gson gson = GsonUtils.getGson();
                    mList = gson.fromJson(result.getJSONArray("list").toString(), new TypeToken<List<MsgData>>(){}.getType());
                    if (mList.size() == 0){
                        tishiTv.setVisibility(View.VISIBLE);
                        homeBtn.setVisibility(View.GONE);
                    }
                    adapter = new MsgAdapter(ShowMsgActivity.this, mList);
                    mListView.setAdapter(adapter);
                    adapter.setOnAdapterClickListener(ShowMsgActivity.this);
                    adapter.setOnAdapterLongClickListener(ShowMsgActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void deleteAllMsg(String id) throws JSONException {
        JSONArray array = new JSONArray();
        for (int i = 0; i < mList.size(); i++) {
            array.put(i,mList.get(i).getId());
        }
        RequestParams params = new RequestParams(HttpUrls.DELETE_MSG);
        params.addBodyParameter("token",getToken());
        if (null != id){
            params.addBodyParameter("ids",id);
            params.addBodyParameter("all","false");
        }else {
            params.addBodyParameter("all","true");
        }
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
            }
        });
    }
    private void readMsg(String id){
        RequestParams params = new RequestParams(HttpUrls.READ_MSG);
        params.addBodyParameter("token",getToken());
        params.addBodyParameter("id", id);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
//    @Override
//    public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int positon, long l) {
//            new AlertDialog.Builder(this)
//                    .setTitle("提示")
//                    .setMessage("是否删除该消息")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            try {
//                                String id = String.valueOf(mList.get(positon).getId());
//                                deleteAllMsg(id);
//                                mList.remove(positon);
//                                adapter.notifyDataSetChanged();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    })
//                    .setNegativeButton("取消",null)
//                    .create()
//                    .show();
//
//
//        return false;
//    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, MsgCommonActivity.class);
//        intent.putExtra("title",mList.get(position).getTitle());
//        intent.putExtra("content",mList.get(position).getContent());
//        startActivity(intent);
//    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, MsgCommonActivity.class);
        intent.putExtra("title",mList.get(position).getTitle());
        intent.putExtra("content",mList.get(position).getContent());
        startActivity(intent);
    }

    @Override
    public void onLongClick(final int position) {
        new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否删除该消息")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                String id = String.valueOf(mList.get(position).getId());
                                deleteAllMsg(id);
                                mList.remove(position);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("取消",null)
                    .create()
                    .show();
    }
}
