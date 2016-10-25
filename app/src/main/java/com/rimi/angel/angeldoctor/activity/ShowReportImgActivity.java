package com.rimi.angel.angeldoctor.activity;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.rimi.angel.angeldoctor.BaseActivity;
import com.rimi.angel.angeldoctor.R;
import com.rimi.angel.angeldoctor.utils.CommonUtils;
import com.rimi.angel.angeldoctor.utils.HttpLoadImg;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/6/22.
 */
public class ShowReportImgActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.cancelBtn)
    ImageButton backBtn;
    @Bind(R.id.mViewpager)
    ViewPager mViewpager;
    @Bind(R.id.shownum_tv)
    TextView showNumTv;

    private JSONArray listArray;
    private SubsamplingScaleImageView imgs[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report_img);
        ButterKnife.bind(this);
        try {
            CommonUtils.getLoading(this,"");
            listArray = new JSONArray(getIntent().getStringExtra("big_img"));
            imgs = new SubsamplingScaleImageView[listArray.length()];
            for (int i = 0; i < listArray.length(); i++) {
                SubsamplingScaleImageView img = new SubsamplingScaleImageView(this);
                imgs[i] = img;
                HttpLoadImg.downLoadImg(ShowReportImgActivity.this,listArray.getJSONObject(i).getString("imageUrl"),img);
            }
            mViewpager.setAdapter(new BigImgAdapter());
            CommonUtils.dismiss();
            showNumTv.setText("1/" + listArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showNumTv.setText((position + 1) + "/" +listArray.length());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @OnClick({R.id.cancelBtn})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancelBtn:
                finish();
                break;
        }
    }

    private class BigImgAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(imgs[position % imgs.length]);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = (SubsamplingScaleImageView)imgs[position % imgs.length];
            ((ViewPager)container).addView(view, 0);
            return imgs[position % imgs.length];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
