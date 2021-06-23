package com.example.administrator.lexianmarket.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.activity.GoodsListActivity;
import com.example.administrator.lexianmarket.bean.user.Order;
import com.example.administrator.lexianmarket.bean.user.Orders;
import com.example.administrator.lexianmarket.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TotalAdapter extends BaseAdapter {
    private Context context;
    private List<Order> ordersList;

    public TotalAdapter(Context context) {
        this.context = context;
    }

    public void setTotalList(List<Order> ordersList) {
        this.ordersList = ordersList;
    }
    public void addNewTotal(List<Order> ordersList){
        this.ordersList.addAll(ordersList);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {

        if(ordersList==null){
            return 0;
        }
        return ordersList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        if(ordersList==null){
            return null;
        }

        return ordersList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TotalViewHolder vw=null;

        if(convertView==null){
            convertView=LayoutInflater.from(context)
                    .inflate(R.layout.adapter_cart,null,false);

            vw=new TotalViewHolder();
            vw.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            vw.tvCarno =(TextView) convertView.findViewById(R.id.tv_carno);
            vw.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
            vw.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
            vw.ivdetail = (ImageView) convertView.findViewById(R.id.iv_detail);
            convertView.setTag(vw);

        }else{
            vw= (TotalViewHolder) convertView.getTag();
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Order orders=ordersList.get(position);
        vw.tvCarno.setText(orders.getJiaqi().getCarno());
        vw.tvTime.setText(format.format(new Date(Long.parseLong(String.valueOf(orders.getJiaqi().getJiaqiTime())))));
        vw.tvMoney.setText(getPayWay(orders.getPay().getPayway()));
        vw.tvAmount.setText(orders.getJiaqi().getAmount());

        bindData(vw.ivdetail,position);
        setListener(vw);

        return convertView;
    }

    private void setListener(TotalViewHolder vw) {
        vw.ivdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= (int) v.getTag();
                Order orders=ordersList.get(position);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_detail, null);
                final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
                TextView tvJiaqiId = (TextView)view.findViewById(R.id.tv_jiaqi_id);
                TextView tvCarno = (TextView)view.findViewById(R.id.tv_carno);
                TextView tvJiaqiAmount = (TextView)view.findViewById(R.id.tv_jiaqi_amount);
                TextView tvJiaqiTime = (TextView)view.findViewById(R.id.tv_jiaqi_time);
                TextView tvTotalPrice = (TextView)view.findViewById(R.id.tv_total_price);
                TextView tvPayWay = (TextView)view.findViewById(R.id.tv_payWay);
                TextView tvPayDate = (TextView)view.findViewById(R.id.tv_pay_date);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                tvJiaqiId.setText(orders.getJiaqi().getId().toString());
                tvCarno.setText(orders.getJiaqi().getCarno());
                tvJiaqiAmount.setText(orders.getJiaqi().getAmount());
                tvJiaqiTime.setText(sdf.format(new Date(Long.parseLong(String.valueOf(orders.getJiaqi().getJiaqiTime())))));
                tvTotalPrice.setText(orders.getPay().getMoney());
                tvPayWay.setText(orders.getPay().getPayway());
                dialog.show();

                dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(context)/10*9), (ScreenUtils.getScreenHeight(context)/3*1));

                //Intent intent=new Intent(context, GoodsListActivity.class);
                //intent.putExtra("storeNo",orders.getStoreNo());
                //context.startActivity(intent);
            }
        });
    }

    private void bindData(View view, int position) {
        view.setTag(position);
    }

    private String getPayWay(String payway){

        String result="";

        switch (payway){

            case "1":
                result=context.getString(R.string.radioButton_xuni);
                break;
            case "2":
                result=context.getString(R.string.radioButton_cash);
                break;
            case "3":
                result=context.getString(R.string.radioButton_etc);
                break;
            case "4":
                result=context.getString(R.string.radioButton_wugan);
                break;
        }

        return result;
    }
    private static class TotalViewHolder{

        public TextView tvTime;
        public TextView tvCarno;
        public TextView tvAmount;
        public TextView tvMoney;
        private ImageView ivdetail;

    }

}
