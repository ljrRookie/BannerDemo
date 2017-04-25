package com.ljr.banner.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by LinJiaRong on 2017/4/25.
 * TODO：
 */

public class MyAdapterTwo extends PagerAdapter {
    private List<ImageView> mImageViews;;
    public MyAdapterTwo(List<ImageView> images) {
        this.mImageViews=images;

    }

    /**
     * 得到图片的总数
     * 实现自动滑动：return Integer.MAX_VALUE; 返回最大数
     * @return
     */
    @Override
    public int getCount() {
        return mImageViews.size();
    }

    /**
     * 比较View和Object是否同一个实例
     * @param view 页面
     * @param object instantiateItem返回的结果
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 相当于getView()
     * @param container ViewPager自身
     * @param position 当前实例化页面的位置
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImageViews.get(position);
        container.addView(imageView);
    return imageView;
    }

    /**
     * 释放界面
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
