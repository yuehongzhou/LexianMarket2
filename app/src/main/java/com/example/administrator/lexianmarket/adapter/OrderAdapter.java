package com.example.administrator.lexianmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.activity.GoodsListActivity;
import com.example.administrator.lexianmarket.bean.user.Order;
import com.example.administrator.lexianmarket.bean.user.Orders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void addNewOrder(List<Order> orderList) {
        this.orderList.addAll(orderList);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {

        if (orderList == null) {
            return 0;
        }
        return orderList.size();
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
        if (orderList == null) {
            return null;
        }

        return orderList.get(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHolder vw = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.order_list, null, false);

            vw = new OrderViewHolder();
            vw.tvId = (TextView) convertView.findViewById(R.id.tv_id);
            vw.tvJiaqiTime = (TextView) convertView.findViewById(R.id.tv_jiaqitime);
            vw.tvCarno = (TextView) convertView.findViewById(R.id.tv_carno);
            vw.tvJiaqiAmount = (TextView) convertView.findViewById(R.id.tv_jiaqi_amount);
            vw.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
            vw.llGoStore = (LinearLayout) convertView.findViewById(R.id.ll_go_store);
            vw.tvpayWay = (TextView) convertView.findViewById(R.id.tv_payway);
            convertView.setTag(vw);

        } else {
            vw = (OrderViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Order orders = orderList.get(position);
        vw.tvId.setText(orders.getJiaqi().getId().toString());
        vw.tvJiaqiTime.setText(sdf.format(new Date(Long.parseLong(String.valueOf(orders.getJiaqi().getJiaqiTime())))));
        vw.tvCarno.setText(orders.getJiaqi().getCarno() + "");
        vw.tvpayWay.setText(getPayWay(orders.getPay().getPayway()));
        vw.tvJiaqiAmount.setText(orders.getJiaqi().getAmount());
        vw.tvMoney.setText(orders.getPay().getMoney());

        bindData(vw.llGoStore, position);
        setListener(vw);

        return convertView;
    }

    private void setListener(OrderViewHolder vw) {
        vw.llGoStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Order orders = orderList.get(position);

                Intent intent = new Intent(context, GoodsListActivity.class);
                intent.putExtra("storeNo", orders.getJiaqi().getId());
                context.startActivity(intent);
            }
        });
    }

    private void bindData(View view, int position) {
        view.setTag(position);
    }

    private String getPayWay(String payway) {

        String result = "";

        switch (payway) {

            case "1":
                result = context.getString(R.string.radioButton_xuni);
                break;
            case "2":
                result = context.getString(R.string.radioButton_cash);
                break;
            case "3":
                result = context.getString(R.string.radioButton_etc);
                break;
            case "4":
                result = context.getString(R.string.radioButton_wugan);
                break;
        }

        return result;
    }

    private static class OrderViewHolder {

        public TextView tvId;
        public TextView tvCarno;
        public TextView tvJiaqiTime;
        public TextView tvJiaqiAmount;
        public TextView tvpayWay;
        public TextView tvMoney;
        private LinearLayout llGoStore;

    }
}
