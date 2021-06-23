/**
 *  Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.activity.CommodityDetailActivity;
import com.example.administrator.lexianmarket.activity.ConfirmOrdersActitvity;
import com.example.administrator.lexianmarket.adapter.CartAdapter;
import com.example.administrator.lexianmarket.adapter.OrdersAdapter;
import com.example.administrator.lexianmarket.adapter.TotalAdapter;
import com.example.administrator.lexianmarket.bean.user.Order;
import com.example.administrator.lexianmarket.bean.user.Orders;
import com.example.administrator.lexianmarket.bean.user.Trolley;
import com.example.administrator.lexianmarket.helper.LoginHelper;
import com.example.administrator.lexianmarket.helper.Page;
import com.example.administrator.lexianmarket.helper.ResultHelper;
import com.example.administrator.lexianmarket.service.CartService;
import com.example.administrator.lexianmarket.service.OrdersService;
import com.example.administrator.lexianmarket.utils.Constant;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
public class OrderFragment extends Fragment {

    private static final int OBTAIN_CART=1;//获取全部订单
    private static final String TAG="CartFragment";
    private TextView mTvNoLogin;//未登录页面
    private PullToRefreshListView mLvCart;//订单列表
    private String state;//支付状态,0:未支付，1：已支付
    private Integer pageNo=1;
    private Integer totalPageNum=1;
    private LinearLayout llLoad;//正在加载页面
    private boolean isRefreshing;
    private static final int NO_MORE_DATA = 2;
    private TotalAdapter totaladapter;
    private CalendarView calendarView;//日历
    private  String start;//日期开始
    private  String end ;//日期结束

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case OBTAIN_CART:

//                    ResultHelper<List<Trolley>> result= (ResultHelper<List<Trolley>>) msg.obj;
//                    if(result.getCode()== Constant.CODE_SUCCESS){
//                        bindCartData(result);
//                    }
//                    setState();
                    ResultHelper<Page<Order>> result = (ResultHelper<Page<Order>>) msg.obj;

                    if (result.getCode() == Constant.CODE_SUCCESS) {

                        bindOrdersData(result);
                    }
                    setState();
                    break;
                case NO_MORE_DATA:
                    Toast.makeText(getContext(),R.string.no_more, Toast.LENGTH_SHORT).show();
                    mLvCart.onRefreshComplete();
                    break;
            }

            super.handleMessage(msg);
        }
    };
    private void setState() {
        llLoad.setVisibility(View.GONE);
        isRefreshing=false;
        mLvCart.onRefreshComplete();
    }

    private void bindOrdersData(ResultHelper<Page<Order>> result) {
        Page<Order> page = result.getData();

        pageNo = page.getPageNo();
        totalPageNum = page.getPageNums();
        List<Order> orderss = page.getData();
        if (orderss != null && orderss.size() > 0) {
            if (pageNo == 1) {
                totaladapter.setTotalList(orderss);

            } else {
                totaladapter.addNewTotal(orderss);

            }
            totaladapter.notifyDataSetChanged();
            mTvNoLogin.setVisibility(View.GONE);
            mLvCart.setVisibility(View.VISIBLE);
        } else {
            mTvNoLogin.setVisibility(View.GONE);
            mLvCart.setVisibility(View.GONE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_cart, container, false);

        initView(rootView);

        setAdapter();
        setListener();

        initData();

        return rootView;
    }

    private void setListener() {

//        mLvCart.setOnItemClickListener(new OnGoCommodityClickListener());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getActivity(),
//                        "您的生日是"+year+"年"+(month+1)+"月"+dayOfMonth+"日", Toast.LENGTH_LONG).show();
//                c.set(year,month,dayOfMonth);
//                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
                int month0 = month+1;
                String newDate = year+"-"+month0+"-"+dayOfMonth;
                 start =newDate;
                 end = newDate+" "+"23:59:00";
                OrdersService.getOrders(1+"",state,start,end,handler);
            }
        });
        mLvCart.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isRefreshing){
//                    CartService.getTrolleys(pageNo+"",handler);
                    OrdersService.getOrders(1+"",state,start,end,handler);
                    isRefreshing=true;
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(pageNo<totalPageNum){
                    if(!isRefreshing){
                        pageNo++;
//                        CartService.getTrolleys(pageNo+"",handler);
                        OrdersService.getOrders(1+"",state,start,end,handler);
                        isRefreshing=true;
                    }
                }else{
                    handler.sendEmptyMessage(NO_MORE_DATA);
                }
            }
        });
    }

    private void setAdapter() {
        totaladapter=new TotalAdapter(getActivity());
        mLvCart.setAdapter(totaladapter);
    }

    @Override
    public void onResume() {
        llLoad.setVisibility(View.VISIBLE);
        initData();
        super.onResume();
    }

    private void initData() {
        if(LoginHelper.isLogin){
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            start = sdf.format(date);
            end = sdf.format(date)+" "+"23:59:00";
            OrdersService.getOrders(1+"",state,start,end,handler);
        }else{
            llLoad.setVisibility(View.GONE);
            mTvNoLogin.setVisibility(View.VISIBLE);
            mLvCart.setVisibility(View.GONE);
        }

    }

    private void initView(View rootView) {

        mLvCart = (PullToRefreshListView) rootView.findViewById(R.id.lv_cart);
        mLvCart.setVisibility(View.GONE);
        //设置可上拉刷新和下拉刷新
        mLvCart.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新时显示的文本
        ILoadingLayout startLayout = mLvCart.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel(getString(R.string.down_refresh));
        startLayout.setRefreshingLabel(getString(R.string.refreshing));
        startLayout.setReleaseLabel(getString(R.string.loosen_refresh));
        ILoadingLayout endLayout = mLvCart.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel(getString(R.string.up_refreshing));
        endLayout.setRefreshingLabel(getString(R.string.refreshing));
        endLayout.setReleaseLabel(getString(R.string.loosen_refresh));
        calendarView =rootView.findViewById(R.id.calendarView);
        mTvNoLogin = (TextView) rootView.findViewById(R.id.tv_no_login);
        llLoad = (LinearLayout) rootView.findViewById(R.id.ll_load);
    }



//    private void goCash() {
//        Intent intent=new Intent(getActivity(), ConfirmOrdersActitvity.class);
//        ArrayList<Trolley> selectedTrolleys=new ArrayList<>();
//
//        for(Trolley t:adapter.getTrolleys()){
//            if(t.isSelected()){
//                selectedTrolleys.add(t);
//            }
//        }
//        if(selectedTrolleys.size()==0){
//            Toast.makeText(getContext(), R.string.select_commodity, Toast.LENGTH_SHORT).show();
//        }
//        intent.putExtra("trolleys",selectedTrolleys);
//        startActivity(intent);
//    }
//
//    private void clearCart() {
//        new AlertDialog.Builder(getActivity())
//                .setTitle(R.string.point_out)
//                .setMessage(R.string.sure_clean_cart)
//                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        adapter.setTrolleys(null);
//                        adapter.notifyDataSetChanged();
//                        rlBottom.setVisibility(View.GONE);
//                        mLvCart.setVisibility(View.GONE);
//                        mTvNoLogin.setVisibility(View.GONE);
//                        mTvNoCart.setVisibility(View.VISIBLE);
//                    }
//                })
//                .setNegativeButton(R.string.cancel,null)
//                .show();
//        CartService.clearTrolley();
//    }
//
//    private void deleteSelectedItems() {
//        List<String> id=new ArrayList<>();
//        List<Trolley> newTrolleys=new ArrayList<>();
//        List<Trolley> trolleys=adapter.getTrolleys();
//        for(Trolley t:trolleys){
//            if(t.isSelected()){
//                id.add(t.getId()+"");
//            }else{
//                newTrolleys.add(t);
//            }
//        }
//
//        adapter.setTrolleys(newTrolleys);
//        adapter.notifyDataSetChanged();
//
//        if(newTrolleys==null||newTrolleys.size()==0){
//            rlBottom.setVisibility(View.GONE);
//            mLvCart.setVisibility(View.GONE);
//            mTvNoLogin.setVisibility(View.GONE);
//            mTvNoCart.setVisibility(View.VISIBLE);
//        }
//
//        for(String tid:id){
//            CartService.deleteTrolley(tid);
//        }
//
//    }
//    private class OnGoCommodityClickListener implements android.widget.AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Trolley t= (Trolley) parent.getAdapter().getItem(position);
//            Intent intent=new Intent(getActivity(),CommodityDetailActivity.class);
//            intent.putExtra("commodityNo",t.getCommodityNo());
//            intent.putExtra("storeNo",t.getStoreNo());
//            Log.e(TAG,t.getCommodityNo());
//            Log.e(TAG,t.getStoreNo());
//            startActivity(intent);
//        }
//    }
}
