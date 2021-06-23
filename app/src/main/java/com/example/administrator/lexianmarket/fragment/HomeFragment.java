/**
 * Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.administrator.lexianmarket.DateChoose;
import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.activity.SpecialCommodityActivity;
import com.example.administrator.lexianmarket.adapter.CarouselAdapter;
import com.example.administrator.lexianmarket.helper.LoginHelper;

import java.util.Random;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 王晨昕
 * @version 1.0
 */
public class HomeFragment extends Fragment {
    // 轮播图片相关的定义
    private ViewPager carouselContainer;     //轮播容器
    private CarouselAdapter carouselAdapter;
    private int[] carouselImageIDs =
            {R.drawable.piciture_one, R.drawable.picture_two, R.drawable.picture_three,
                    R.drawable.picture_four, R.drawable.picture_five};
    private final int carouselCount = 5;
    private int currentCarouselIndex = 0;
    private LinearLayout carouselIndicatorsContainer;   //红点
    private final int MSG_CAROUSEL_SWITCH = 0x200;
    private boolean isStop = false;     // 判断轮播是否正在播放
    private LinearLayout llGoOrders;
    private ImageView ivGoOnSale;
    private ImageView ivGoNewSale;
    private ImageView ivGoRecommended;
    private ImageView ivGoSelected;
    private ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(rootView);
        setListener();
        initData();
        return rootView;
    }
    private void setListener() {

        View.OnClickListener listener = new OnGoOthersClickListener();
        llGoOrders.setOnClickListener(listener);//4.服务统计
        View.OnClickListener specListener = new onGoSpecListener();
        ivGoOnSale.setOnClickListener(specListener);//左下角图片
        ivGoNewSale.setOnClickListener(specListener);//右下角图片
        ivGoRecommended.setOnClickListener(specListener);//右下角图片
        ivGoSelected.setOnClickListener(specListener);//右下角图片
        View.OnTouchListener viewPagerListener = new onViewPagerTouchListener();
        carouselContainer.setOnTouchListener(viewPagerListener);//轮播图片
    }

    private void initView(View view) {

        carouselContainer = (ViewPager) view.findViewById(R.id.carousel_container);
        carouselIndicatorsContainer = (LinearLayout) view
                .findViewById(R.id.carousel_indicators_container);

        scrollView = (ScrollView) view.findViewById(R.id.home_category_scrollview);//轮播图片
        llGoOrders = (LinearLayout) view.findViewById(R.id.ll_go_orders); //4.服务统计
        ivGoOnSale = (ImageView) view.findViewById(R.id.iv_go_on_sale); //左下角图片
        ivGoNewSale = (ImageView) view.findViewById(R.id.iv_go_new_sale); //右下角图片
        ivGoRecommended = (ImageView) view.findViewById(R.id.iv_go_recommended);//右下角图片
        ivGoSelected = (ImageView) view.findViewById(R.id.iv_go_selected);//右下角图片

    }

    private void initData() {
        // 初始化Carousel组件
        Random random = new Random(System.currentTimeMillis());
        currentCarouselIndex = random.nextInt(carouselCount);       // 选定从随机的序号开始轮播
        carouselAdapter = new CarouselAdapter(getActivity(), carouselImageIDs);
        carouselContainer.setAdapter(carouselAdapter);
        carouselContainer.addOnPageChangeListener(new OnImagePagerChangeListener());
        carouselContainer.setCurrentItem(currentCarouselIndex);
        toggleImageIndicator();
        carouselHandler.sendEmptyMessageDelayed(MSG_CAROUSEL_SWITCH, 2500);     // 发起轮播

        // 使scrollview定位到顶部
        scrollView.smoothScrollTo(0, 20);
    }

    private Handler carouselHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CAROUSEL_SWITCH:
                    removeMessages(MSG_CAROUSEL_SWITCH);
                    sendEmptyMessageDelayed(MSG_CAROUSEL_SWITCH, 1000);
                    toggleImagePager();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        carouselHandler.removeMessages(MSG_CAROUSEL_SWITCH);
        isStop = true;
        super.onPause();
    }

    @Override
    public void onResume() {
//        if(LoginHelper.isLogin){
//            initData();
//        }else{
//            ivUser.setImageResource(R.mipmap.mine_default_user_icon);
//            tvUsername.setText(R.string.login_first);
//            tvBalance.setText("0.00");
//        }
        if (isStop) {
            isStop = false;
            carouselHandler.sendEmptyMessageDelayed(MSG_CAROUSEL_SWITCH, 2500);
        }
        super.onResume();
    }

    private class OnImagePagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) { //position就是当前滑动到的页面
            carouselHandler.removeMessages(MSG_CAROUSEL_SWITCH);
            carouselHandler.sendEmptyMessageDelayed(MSG_CAROUSEL_SWITCH, 2000);
            toggleImageIndicator();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void toggleImageIndicator() {

        int index = carouselContainer.getCurrentItem();
        for (int i = 0; i < carouselCount; i++) {
            ImageView indicator = (ImageView) carouselIndicatorsContainer.getChildAt(i);
            if (i == index) {
                indicator.setImageResource(R.drawable.carousel_indicator_active);
            } else {
                indicator.setImageResource(R.drawable.carousel_indicator_inactive);
            }
        }

    }

    public void toggleImagePager() {
        int next = (carouselContainer.getCurrentItem() + 1) % carouselCount;
        carouselContainer.setCurrentItem(next);
        toggleImageIndicator();
    }

    private class OnGoOthersClickListener implements View.OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (v.getId() != R.id.ll_go_category) {
                if (LoginHelper.isLogin) {
                    goOthersActivity(v.getId());
                } else {
                    //未登录不能查看4服务统计，其他都可以切换到对应的fragment
                    Toast.makeText(getActivity(), R.string.login_first, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
     //已登录，查看4服务统计
    private void goOthersActivity(int id) {
        Intent intent = null;
        //            case R.id.ll_go_collection:
        //                getFragmentManager().beginTransaction().replace(R.id.fragment_content,new StoreFragment()).commit();
        //
        //                break;
        if (id == R.id.ll_go_orders) {
            intent = new Intent(getActivity(), DateChoose.class);
            if (intent != null) {
                startActivity(intent);
            }
        }
    }

    private class onGoSpecListener implements View.OnClickListener {
        //查看商品信息
        @Override
        public void onClick(View v) {
            goSpecActivity(v.getId());
        }
    }

    private void goSpecActivity(int id) {
        //跳转到商品信息界面
        int flag = 0;
        String title = "";
        switch (id) {
            case R.id.iv_go_on_sale:
                flag = 1;
                title = getString(R.string.specail_first);
                break;
            case R.id.iv_go_selected:
                flag = 2;
                title = getString(R.string.specail_two);
                break;
            case R.id.iv_go_recommended:
                flag = 3;
                title = getString(R.string.special_third);
                break;
            case R.id.iv_go_new_sale:
                flag = 4;
                title = getString(R.string.special_fourth);
                break;
        }
        Intent intent = new Intent(getActivity(), SpecialCommodityActivity.class);
        intent.putExtra("id", flag);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private class onViewPagerTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    carouselHandler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_UP:
                    carouselHandler.sendEmptyMessageDelayed(MSG_CAROUSEL_SWITCH, 2000);
                    break;
            }
            return false;
        }
    }
}
