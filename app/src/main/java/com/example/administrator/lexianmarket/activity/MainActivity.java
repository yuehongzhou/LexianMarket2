/**
 * Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.bean.user.User;
import com.example.administrator.lexianmarket.fragment.OrderFragment;
import com.example.administrator.lexianmarket.fragment.CategoryFragment;
import com.example.administrator.lexianmarket.fragment.HomeFragment;
import com.example.administrator.lexianmarket.fragment.MineFragment;
import com.example.administrator.lexianmarket.fragment.JiaqiFragment;
import com.example.administrator.lexianmarket.helper.LoginHelper;
import com.example.administrator.lexianmarket.helper.ResultHelper;
import com.example.administrator.lexianmarket.service.UserService;
import com.example.administrator.lexianmarket.utils.Constant;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 王晨昕
 * @version 1.0
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Fragment[] fragments;               // 记录所有Fragments对象的数组
    private Fragment currentFragment;           //当前的Fragment
    private static int currentFragmentIndex;

    public static String chepai = "";

    private LinearLayout fragmentIndicatorsContainer;   // Fragments菜单项容器
    private LinearLayout[] fragmentIndicators;  // 记录所有Fragments菜单项对象的数组

    // 定义Fragments菜单在激活和非激活状态下的图片
    private final int[] ICON_SELECT_RESID = {R.mipmap.m_home_active,
            R.mipmap.m_category_active, R.mipmap.m_jiaqi_active, R.mipmap.m_order_active,
            R.mipmap.m_mine_active};
    private final int[] ICON_UNSELECT_RESID = {R.mipmap.m_home_inactive,
            R.mipmap.m_category_inactive, R.mipmap.m_jiaqi_inactive,
            R.mipmap.m_order_inactive, R.mipmap.m_mine_inactive};
    // 定义Fragments菜单文字在激活和非激活状态下的颜色
    private int colorUnSelect;
    private int colorSelect;
    private static final int LOGIN_RESPONSE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case LOGIN_RESPONSE:

                    ResultHelper<User> result = (ResultHelper<User>) msg.obj;
                    if (result.getCode() == Constant.CODE_SUCCESS) {       //1
                        LoginHelper.isLogin = true;
                    } else {
                        LoginHelper.isLogin = false;
                    }
                    break;

            }


            super.handleMessage(msg);
        }
    };

    long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        Intent intent = getIntent();
        if(intent!=null) {
            int id = intent.getIntExtra("id", 0);//获取intent值
            chepai = intent.getStringExtra("chepai");
            if(id==2) {
                Bundle bundle = new Bundle();
                //3.存入数据到Bundle对象中
                bundle.putString("chepai",chepai);
                updateSelectedFragment(2, bundle);
            }
        }



    }

    public void initView() {
        fragmentIndicatorsContainer =
                (LinearLayout) findViewById(R.id.fragment_indicators_container);
        fragmentIndicators = new LinearLayout[fragmentIndicatorsContainer.getChildCount()];//getChildCount()方法仅返回其所包含的直接控件的数量5
        for (int i = 0; i < fragmentIndicators.length; i++) {
            fragmentIndicators[i] = (LinearLayout) fragmentIndicatorsContainer.getChildAt(i);
            fragmentIndicators[i].setTag(Integer.valueOf(i));
            fragmentIndicators[i].setOnClickListener(this);  //非匿名
        }

        colorUnSelect = getResources().getColor(R.color.text_inactive);
        colorSelect = getResources().getColor(R.color.text_active);
    }

    public void initData() {
        fragments = new Fragment[5];
        fragments[0] = new HomeFragment();
        fragments[1] = new CategoryFragment();
        fragments[2] = new JiaqiFragment();
        fragments[3] = new OrderFragment();
        fragments[4] = new MineFragment();

        if (LoginHelper.isLogin == false) {
            SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//数据存储方式，文件名userinfo
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                String phone = sp.getString("USER_NAME", "");
                String password = sp.getString("PASSWORD", "");
                UserService.signIn(phone, password, handler);
            }
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, fragments[0]).commit();
        ((ImageView) fragmentIndicators[0].getChildAt(0)).setImageResource(ICON_SELECT_RESID[0]);
        ((TextView) fragmentIndicators[0].getChildAt(1)).setTextColor(colorSelect);

        currentFragmentIndex = 0;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (R.id.ll_go_cart == view.getId()) {
            updateSelectedFragment(2);//车辆加气
            return;
        } else if (R.id.ll_go_search == id) {
            updateSelectedFragment(1);//商品列表
            return;
        } else if (R.id.ll_go_category == id) {
            updateSelectedFragment(3);//我的订单
            return;
        }
        int tag = (int) view.getTag();
        if (tag != currentFragmentIndex) {
            updateSelectedFragment(tag);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) >= 2000) {
                Toast.makeText(this, R.string.app_exit_indicator, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 切换不同的Fragment后，要刷新菜单图片和文字状态
    private void updateSelectedFragment(int position) {
        ((ImageView) fragmentIndicators[currentFragmentIndex]
                .getChildAt(0)).setImageResource(ICON_UNSELECT_RESID[currentFragmentIndex]);//非激活
        ((TextView) fragmentIndicators[currentFragmentIndex].getChildAt(1))
                .setTextColor(colorUnSelect);

        ((ImageView) fragmentIndicators[position].getChildAt(0))
                .setImageResource(ICON_SELECT_RESID[position]);//激活
        ((TextView) fragmentIndicators[position].getChildAt(1)).setTextColor(colorSelect);

        switchFragment(fragments[currentFragmentIndex], fragments[position], position);
    }

    // 切换不同的Fragment后，要刷新菜单图片和文字状态
    private void updateSelectedFragment(int position, Bundle bundle) {
        ((ImageView) fragmentIndicators[currentFragmentIndex]
                .getChildAt(0)).setImageResource(ICON_UNSELECT_RESID[currentFragmentIndex]);//非激活
        ((TextView) fragmentIndicators[currentFragmentIndex].getChildAt(1))
                .setTextColor(colorUnSelect);

        ((ImageView) fragmentIndicators[position].getChildAt(0))
                .setImageResource(ICON_SELECT_RESID[position]);//激活
        ((TextView) fragmentIndicators[position].getChildAt(1)).setTextColor(colorSelect);

        Fragment frag = fragments[position];

        frag.setArguments(bundle);

        switchFragment(fragments[currentFragmentIndex], frag, position);
    }

    /**
     * 切换fragment
     * @param from     当前Fragment
     * @param to       目标Fragment
     * @param position 当前的位置
     */
    private void switchFragment(Fragment from, Fragment to, int position) {
        if (currentFragment != to) {
            currentFragment = to;

            if (!to.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(from)
                        .add(R.id.fragment_content, to).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(from).show(to).commit();
            }
            currentFragmentIndex = position;
        }
    }


}
