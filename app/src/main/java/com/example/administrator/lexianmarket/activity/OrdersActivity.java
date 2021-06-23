/**
 *  Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lexianmarket.CalendarActivity;
import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.adapter.OrderAdapter;
import com.example.administrator.lexianmarket.bean.user.Order;
import com.example.administrator.lexianmarket.bean.user.Orders;
import com.example.administrator.lexianmarket.helper.Page;
import com.example.administrator.lexianmarket.helper.ResultHelper;
import com.example.administrator.lexianmarket.service.OrdersService;
import com.example.administrator.lexianmarket.utils.Constant;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.support.design.widget.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 郝伟
 * @version 1.0
 */
public class OrdersActivity extends BaseActivity {
private ImageButton update;
    private Integer pageNo = 1;
    private static final int OBTAINED_ORDERS = 1;
    private static final int NO_MORE_DATA = 2;
    private PullToRefreshListView lvOrders,lvOrders2;
    private LinearLayout llLoad;
    private OrderAdapter adapter,adapter2;
    private String state;
    private TextView tvNoOrders;
    private Integer totalPageNum;
    private boolean isRefreshing;
    private TextView time_interval_tv;
    private View empty_tips,empty_tips2;
    private Button btn_pre;
    private Button btn_next;
    private Long start_time, end_time;
    private ImageView calendar_btn,refresh_btn;
    private String start,end;
    private String datefmt = "yyyy-MM-dd";
    private TextView tv_amount_name;
    private TextView tv_amount;
    private TextView tv_total;
    private TextView tv_total_name;
    private int mode;
    public static String str_mode = "mode";
    public static final int mode_none 	= 0;
    public static final int mode_month 	= 1;
    public static final int mode_week 	= 2;
    public static final int mode_day 	= 3;

    private TabLayout tabLayout;
    private String[] Ltitles = {"虚拟卡支付", "现金支付"};
    //配置默认选中第几项
     private int ItemWhat=0;
    private ViewPager viewPager;
    private List<View> viewList;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case OBTAINED_ORDERS:
                    ResultHelper<Page<Order>> result = (ResultHelper<Page<Order>>) msg.obj;

                    if (result.getCode() == Constant.CODE_SUCCESS) {

                        bindOrdersData(result);
                    }
                    setState();

                    break;
                case NO_MORE_DATA:
                    Toast.makeText(OrdersActivity.this,R.string.no_more, Toast.LENGTH_SHORT).show();
                    lvOrders.onRefreshComplete();
                    lvOrders2.onRefreshComplete();
                    break;

            }

            super.handleMessage(msg);
        }
    };

    private void setState() {
        llLoad.setVisibility(View.GONE);
        isRefreshing=false;
        lvOrders.onRefreshComplete();
        lvOrders2.onRefreshComplete();
    }

    private void bindOrdersData(ResultHelper<Page<Order>> result) {
        Page<Order> page = result.getData();

        pageNo = page.getPageNo();
        totalPageNum = page.getPageNums();
        List<Order> orderss = page.getData();
        List<Order> other = new ArrayList<>();
        List<Order> xuni = new ArrayList<>();
        for(Order orders:orderss){
            if(orders.getPay().getPayway().equals("2")){
                other.add(orders);
            }else if (orders.getPay().getPayway().equals("1")){
                xuni.add(orders);
            }
        }
//        System.out.println(other);
        String total = page.getTotal();
        String amount = page.getAmount();
        tv_total.setText(total);
        tv_amount.setText(amount);


        if (orderss != null && orderss.size() > 0) {
//            System.out.println(orderss);
            setTitleVisibility();
            tvNoOrders.setVisibility(View.GONE);
            empty_tips.setVisibility(View.GONE);
            empty_tips2.setVisibility(View.GONE);
            lvOrders.setVisibility(View.VISIBLE);
            lvOrders2.setVisibility(View.VISIBLE);
            if(pageNo==1){
                adapter.setOrderList(xuni);
                adapter2.setOrderList(other);
            }else{
                adapter.addNewOrder(xuni);
                adapter2.addNewOrder(other);
            }
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
        } else {
            setTitleVisibility();
            time_interval_tv.setVisibility(View.VISIBLE);
            empty_tips.setVisibility(View.VISIBLE);
            empty_tips2.setVisibility(View.VISIBLE);
            tv_total.setText("无");
            tv_amount.setText("无");

        }

    }
//    private  String sleepJavaTest() {
//
//        try {
//            Thread.sleep(3000);
//            lvOrders.setEmptyView(empty_tips);
//            return "当前函数成功的返回";
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return "执行异常";
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

        setAdapter();
        refreshTransactions();

    }


    private void setListener() {
        lvOrders.setOnItemClickListener(new OnGoDetailClickListener());
        lvOrders2.setOnItemClickListener(new OnGoDetailClickListener());
        ((TextView) findViewById(R.id.common_title_right_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, YearStatisticsActivity.class);
                startActivity(intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, CalendarActivity.class);
                startActivityForResult(intent,1);
            }
        });
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRefreshing){
                    OrdersService.getOrders(1+"",state,start,end,handler);
                    isRefreshing=true;
                }
            }
        });
        //是否选中监听
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中时进入，改变样式
                ItemSelect(tab);
                //onTabselected方法里面调用了viewPager的setCurrentItem 所以要想自定义OnTabSelectedListener，也加上mViewPager.setCurrentItem(tab.getPosition())就可以了
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中进入，改变样式
                ItemNoSelect(tab);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //重新选中

            }
        });
        lvOrders.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isRefreshing){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            OrdersService.getOrders(1+"",state,start,end,handler);
                            isRefreshing=true;
                        }
                    }.start();

                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(pageNo<totalPageNum){
                    if(!isRefreshing){
                        pageNo++;
                        OrdersService.getOrders(pageNo+"",state,start,end,handler);
                        isRefreshing=true;
                    }
                }else{
                    handler.sendEmptyMessage(NO_MORE_DATA);
                }
            }
        });
        lvOrders2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isRefreshing){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            OrdersService.getOrders(1+"",state,start,end,handler);
                            isRefreshing=true;
                        }
                    }.start();

                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(pageNo<totalPageNum){
                    if(!isRefreshing){
                        pageNo++;
                        OrdersService.getOrders(pageNo+"",state,start,end,handler);
                        isRefreshing=true;
                    }
                }else{
                    handler.sendEmptyMessage(NO_MORE_DATA);
                }
            }
        });
    }

    //某个项选中，改变其样式
    private void ItemSelect(TabLayout.Tab tab) {
        View customView = tab.getCustomView();
        TextView tabText = (TextView) customView.findViewById(R.id.item_text);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.bg_header));
//        String stitle = tabText.getText().toString();
//        for(int i=0;i<Ltitles.length;i++){
//            if(Ltitles[i].equals(stitle)){
//                Toast.makeText(OrdersActivity.this,"xxx+"+i,Toast.LENGTH_SHORT).show();
//
//            }
//        }
    }
    //某个项非选中，改变其样式
    private void ItemNoSelect(TabLayout.Tab tab) {
        View customView = tab.getCustomView();
        TextView tabText = (TextView) customView.findViewById(R.id.item_text);
//        ImageView tabIcon = (ImageView) customView.findViewById(R.id.item_img);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        String stitle = tabText.getText().toString();
//        for(int i=0;i<Ltitles.length;i++){
//            if(Ltitles[i].equals(stitle)){
//                tabIcon.setImageResource(Limg[i]);
//            }
//        }
    }


    private void initData() {
        empty_tips.setVisibility(View.GONE);
        empty_tips2.setVisibility(View.GONE);
//        Intent intent = getIntent();
//        state = intent.getIntExtra("state", -1)+"";
//        if (state.equals("-1")) {
//            state=null;
            OrdersService.getOrders(pageNo + "", state,start,end, handler);

//        } else {
////            OrdersService.getOrders(pageNo + "", state + "",start,end, handler);
////        }

    }
    private void setAdapter(){
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //这个方法是返回总共有几个滑动的页面（）
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                //该方法判断是否由该对象生成界面。
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //这个方法返回一个对象，该对象表明PagerAapter选择哪个对象放在当前的ViewPager中。这里我们返回当前的页面
                viewPager.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //这个方法从viewPager中移动当前的view。（划过的时候）
                viewPager.removeView(viewList.get(position));
            }
        });
        //绑定
        tabLayout.setupWithViewPager(viewPager);

        //设置默认选中页，宏定义
        tabLayout.getTabAt(ItemWhat).select();
        viewPager.setOffscreenPageLimit(3); //设置向左和向右都缓存的页面个数
        //初始化菜单栏显示
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //寻找到控件
            View view = LayoutInflater.from(OrdersActivity.this).inflate(R.layout.item_view, null);
            LinearLayout mTabView = (LinearLayout) view.findViewById(R.id.item_view);
            TextView mTabText = (TextView) view.findViewById(R.id.item_text);
            mTabText.setText(Ltitles[i]);
            //设置不可点击
            // mTabView.setClickable(true);

            //更改选中项样式
            if(i==ItemWhat){
                mTabText.setTextColor(ContextCompat.getColor(this, R.color.bg_header));
            }

            //设置样式
            tabLayout.getTabAt(i).setCustomView(view);
        }
    }
    private void setdataAdapter() {

        adapter = new OrderAdapter(this);
        adapter2 = new OrderAdapter(this);
        lvOrders.setAdapter(adapter);
        lvOrders2.setAdapter(adapter2);
    }
    private void refreshTransactions(){
        setdataAdapter();
        setTimeIntervalText();
        setListener();
        initData();

    }
    private void setTitleVisibility(){
        tv_total.setVisibility(View.VISIBLE);
        tv_total_name.setVisibility(View.VISIBLE);
        tv_amount.setVisibility(View.VISIBLE);
        tv_amount_name.setVisibility(View.VISIBLE);
        time_interval_tv.setVisibility(View.VISIBLE);
        btn_pre.setVisibility(View.VISIBLE);
        btn_next.setVisibility(View.VISIBLE);
        calendar_btn.setVisibility(View.VISIBLE);
    }
    private void initViews() {
        setContentView(R.layout.activity_orders);
        ((TextView) findViewById(R.id.common_title_content)).setText(R.string.my_order);
        ((TextView) findViewById(R.id.common_title_right_text)).setText("统计");
        llLoad = (LinearLayout) findViewById(R.id.ll_load);

        tabLayout = (TabLayout) findViewById(R.id.tabs2);

        //找到ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //定义一个视图集合（用来装左右滑动的页面视图）
        viewList = new ArrayList<View>();
        //定义两个视图，两个视图都加载同一个布局文件list_view.ml
        View view1 = getLayoutInflater().inflate(R.layout.pulltorefreshlistview,null);
        View view2 = getLayoutInflater().inflate(R.layout.pulltorefreshlistview,null);
        //将两个视图添加到视图集合viewList中
        viewList.add(view1);
        viewList.add(view2);
        //为ViewPager设置适配器

        lvOrders = (PullToRefreshListView) view1.findViewById(R.id.lv_orders);
        lvOrders2 = (PullToRefreshListView) view2.findViewById(R.id.lv_orders);




//        lvOrders = (PullToRefreshListView) findViewById(R.id.lv_orders);
//        lvOrders.setVisibility(View.GONE);
        //设置可上拉刷新和下拉刷新
        lvOrders.setMode(PullToRefreshBase.Mode.BOTH);
        lvOrders2.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新时显示的文本
        ILoadingLayout startLayout = lvOrders.getLoadingLayoutProxy(true, false);
        ILoadingLayout startLayout2 = lvOrders2.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel(getString(R.string.down_refresh));
        startLayout.setRefreshingLabel(getString(R.string.refreshing));
        startLayout.setReleaseLabel(getString(R.string.loosen_refresh));
        startLayout2.setPullLabel(getString(R.string.down_refresh));
        startLayout2.setRefreshingLabel(getString(R.string.refreshing));
        startLayout2.setReleaseLabel(getString(R.string.loosen_refresh));
        ILoadingLayout endLayout = lvOrders.getLoadingLayoutProxy(false, true);
        ILoadingLayout endLayout2 = lvOrders.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel(getString(R.string.up_refreshing));
        endLayout.setRefreshingLabel(getString(R.string.refreshing));
        endLayout.setReleaseLabel(getString(R.string.loosen_refresh));
        endLayout2.setPullLabel(getString(R.string.up_refreshing));
        endLayout2.setRefreshingLabel(getString(R.string.refreshing));
        endLayout2.setReleaseLabel(getString(R.string.loosen_refresh));
        tvNoOrders = (TextView) findViewById(R.id.tv_no_orders);
        time_interval_tv = (TextView)findViewById(R.id.time_interval_tv);
        empty_tips = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_tip, null);
        empty_tips2 = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_tip, null);
        lvOrders.setEmptyView(empty_tips);
        lvOrders2.setEmptyView(empty_tips2);
        btn_next = (Button) findViewById(R.id.next_btn);
        btn_pre = (Button) findViewById(R.id.pre_btn);
        tv_total = findViewById(R.id.tv_total);
        tv_total_name = findViewById(R.id.tv_total_name);
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount_name = findViewById(R.id.tv_amount_name);
        calendar_btn = (ImageView) findViewById(R.id.calendar_btn);
        refresh_btn =  (ImageView) findViewById(R.id.refresh_btn);
        Intent intent = getIntent();
        start_time 	= intent.getLongExtra("startTime", 0);
        end_time = intent.getLongExtra("endTime", 0);
        mode = intent.getIntExtra(str_mode, mode_none);
        if (start_time == 0 || end_time == 0 || mode == mode_none) {
            Toast.makeText(this, getString(R.string.error_system_message), Toast.LENGTH_SHORT).show();
            finish();
        }


    }


    private class OnGoDetailClickListener implements
            android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Orders orders = (Orders) parent.getAdapter().getItem(position);

            Intent intent = new Intent(OrdersActivity.this, OrderDetailActivity.class);
            intent.putExtra("id", orders.getId());
            startActivity(intent);
        }
    }

    private void setTimeIntervalText() {
        if (start_time.equals(end_time)) {
            SimpleDateFormat sdf = new SimpleDateFormat(datefmt);
            time_interval_tv.setText(sdf.format(new Date(start_time)));
            start = sdf.format(new Date(start_time));
            end = sdf.format(new Date(start_time))+" "+"23:59:00";
        } else {
            Date date1 = new Date(start_time);
            Date date2 = new Date(end_time);
            SimpleDateFormat sdf = new SimpleDateFormat(datefmt);
            time_interval_tv.setText(sdf.format(date1) + "-" + sdf.format(date2));
            start = sdf.format(date1);
            end =  sdf.format(date2)+" "+"23:59:00";

        }
    }
    public void dtclick(View v){
        Date date1, date2;
        switch (v.getId()) {
            case R.id.pre_btn:
                switch (mode) {
                    case mode_day:
                        date1 = new Date(start_time);
                        date1.setDate(date1.getDate() - 1);
                        start_time = end_time = date1.getTime();
                        break;
                    case mode_week:
                        date1 = new Date(start_time);
                        date2 = new Date(end_time);
                        date1.setDate(date1.getDate() - 7);
                        date2.setDate(date2.getDate() - 7);
                        start_time = date1.getTime();
                        end_time = date2.getTime();
                        break;
                    case mode_month:
                        date1 = new Date(start_time);
                        date2 = new Date(end_time);
                        date1 = new Date(date1.getYear(), date1.getMonth() - 1, 1);
                        date2 = new Date(date2.getYear(), date2.getMonth(), 0);
                        start_time = date1.getTime();
                        end_time = date2.getTime();
                        break;
                }
                break;
            case R.id.next_btn:
                switch (mode) {
                    case mode_day:
                        date1 = new Date(start_time);
                        date1.setDate(date1.getDate() + 1);
                        start_time = end_time = date1.getTime();
                        break;
                    case mode_week:
                        date1 = new Date(start_time);
                        date2 = new Date(end_time);
                        date1.setDate(date1.getDate() + 7);
                        date2.setDate(date2.getDate() + 7);
                        start_time = date1.getTime();
                        end_time = date2.getTime();
                        break;
                    case mode_month:
                        date1 = new Date(start_time);
                        date2 = new Date(end_time);
                        date1 = new Date(date1.getYear(), date1.getMonth() + 1, 1);
                        date2 = new Date(date2.getYear(), date2.getMonth() + 2, 0);
                        start_time = date1.getTime();
                        end_time = date2.getTime();
                        break;
                }
                break;
        }
       refreshTransactions();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
         start_time = end_time = data.getLongExtra("result",1);
           refreshTransactions();
        }
    }

}
