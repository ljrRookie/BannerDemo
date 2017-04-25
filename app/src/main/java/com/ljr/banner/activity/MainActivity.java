package com.ljr.banner.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljr.banner.R;
import com.ljr.banner.adapter.MyAdapter;
import com.ljr.banner.adapter.MyAdapterTwo;
import com.ljr.banner.utils.Utils;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.vp_one)
    ViewPager mVpOne;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ll_point_group)
    LinearLayout mLlPointGroup;
    @Bind(R.id.vp_two)
    ViewPager mVpTwo;
    @Bind(R.id.indicator)
    CircleIndicator mIndicator;
    @Bind(R.id.banner)
    com.youth.banner.Banner mBanner;
    private int prePosition = 0;
    private int[] images_one = new int[]{R.mipmap.index1, R.mipmap.index2, R.mipmap.index3, R.mipmap.index4};
    private int[] images_two = new int[]{R.mipmap.index01, R.mipmap.index02, R.mipmap.index03, R.mipmap.index04};

    private final String[] imageDescriptions = {"雪山美女", "校园美女", "山水美女", "汽车女郎"};
    private List<ImageView> mImageViews_two;
    private List<ImageView> mImageViews_one;


    /**
     * 发送消息实现定时自动滑动
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int one_item = mVpOne.getCurrentItem() + 1;
            mVpOne.setCurrentItem(one_item);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initOne();
        initTwo();
        initThree();
    }


    /**
     * ****************1.ViewPager***************
     */
    private void initOne() {
        mImageViews_one = new ArrayList<>();
        for (int i = 0; i < images_one.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images_one[i]);
            mImageViews_one.add(imageView);
            //添加小圆点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            //像素转换 ----在所有的手机都是8个像素
            int width = Utils.dip2px(this, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            if (i == 0) {
                point.setEnabled(true);//显示红色
            } else {
                point.setEnabled(false);//显示灰色
                params.leftMargin = width;
            }
            point.setLayoutParams(params);
            mLlPointGroup.addView(point);
        }
        mTvTitle.setText(imageDescriptions[0]);
        mVpOne.setAdapter(new MyAdapter(mImageViews_one));
        //监听ViewPager的页面变化
        mVpOne.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置中间位置。实现一开始可以左右滑动(位置要保证是页面数的整数倍)
        int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mImageViews_one.size();
        mVpOne.setCurrentItem(item);
        //发送消息。实现自动滑动
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    /**
     * ****************2.ViewPager+circleindicator***************
     */
    private void initTwo() {
        mImageViews_two = new ArrayList<>();
        for (int i = 0; i < images_two.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images_two[i]);
            mImageViews_two.add(imageView);
        }

        mVpTwo.setAdapter(new MyAdapterTwo(mImageViews_two));
        mIndicator.setViewPager(mVpTwo);
    }

    /**
     * ****************3.Banner***************
     */
    private void initThree() {

        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE	);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //图片地址
        ArrayList<String> imageURLs = new ArrayList<>();
        imageURLs.add("http://n.sinaimg.cn/comic/crawl/20160809/R9V6-fxutfpk5029190.jpg");
        imageURLs.add("http://www.suanchang.com/zb_users/upload/2017/03/201703231490259385104241.jpg");
        imageURLs.add("https://b-ssl.duitang.com/uploads/item/201406/24/20140624163743_XsWTZ.png");
        imageURLs.add("http://img1.gtimg.com/gamezone/pics/hv1/99/156/1937/125993304.jpg");

        mBanner.setImages(imageURLs);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Accordion);
        //设置标题集合（当banner样式有显示title时）
        String[] titles = new String[]{"刀剑神域", "火影忍者", "妖精的尾巴", "海贼王"};
        mBanner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滚动的时候回调这个方法
         *
         * @param position             当前页面的位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候调用这个方法
         *
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position % mImageViews_one.size();
            mTvTitle.setText(imageDescriptions[realPosition]);
            //把上一个高亮的设置为灰色
            mLlPointGroup.getChildAt(prePosition).setEnabled(false);
            //把当前的设置为高亮--红色
            mLlPointGroup.getChildAt(realPosition).setEnabled(true);
            prePosition = realPosition;
        }

        /**
         * 当页面滚动状态变化的时候回调这个方法
         * 静止 -》 滑动
         * 滑动 -》 静止
         * 静止 -》 拖拽
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class GlideImageLoader implements ImageLoaderInterface {
        @Override
        public void displayImage(Context context, Object path, View imageView) {
            /**
             常用的图片加载库：
             Universal Image Loader：一个强大的图片加载库，包含各种各样的配置，最老牌，使用也最广泛。
             Picasso: Square出品，必属精品。和OkHttp搭配起来更配呦！
             Volley ImageLoader：Google官方出品，可惜不能加载本地图片~
             Fresco：Facebook出的，天生骄傲！不是一般的强大。
             Glide：Google推荐的图片加载库，专注于流畅的滚动。
             */
            Glide.with(context).load(path).into((ImageView) imageView);        }

        @Override
        public View createImageView(Context context) {
            return null;
        }
    }
}
