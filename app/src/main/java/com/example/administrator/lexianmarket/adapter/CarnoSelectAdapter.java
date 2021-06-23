package com.example.administrator.lexianmarket.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.bean.JiaqiCar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhz on 2020/6/4.
 */

public class CarnoSelectAdapter extends BaseAdapter {

    List<Boolean> list;
    List<String> data;
    Context context;
    LayoutInflater mInflater;
    Activity activity;
    int lastPosition=-1;

    public CarnoSelectAdapter(Context context, List<String> data) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        mInflater = LayoutInflater.from(context);
        list = new ArrayList<Boolean>();
        activity = (Activity) context;

        for(int i=0;i<data.size();i++)
        {
            list.add(new Boolean(false));
        }
    }

    public void setCars(List<String> cars) {
        this.data = cars;
        list = new ArrayList<Boolean>();
        for(int i=0;i<data.size();i++)
        {
            list.add(new Boolean(false));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.car_choice_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_choice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!"".equals(data.get(position))) {
            viewHolder.title.setText(data.get(position));
        }
        if (list.get(position) == true) {
            viewHolder.title.setBackgroundDrawable(activity.getResources()
                    .getDrawable(R.drawable.button_shape_pressed));
        } else {
            viewHolder.title.setBackgroundDrawable(activity.getResources()
                    .getDrawable(R.drawable.button_shape_normal));
        }


        return convertView;
    }

    /**
     * 修改选中时的状态
     *
     * @param position
     */
    public void changeState(int position) {
        if (lastPosition != -1) {
            list.set(lastPosition, false);// 取消上一次的选中状态
        }
        list.set(position, !list.get(position));// 设置这一次的选中状态
        lastPosition = position; // 记录本次选中的位置
        notifyDataSetChanged(); // 通知适配器进行更新
    }

    class ViewHolder {
        TextView title;
    }
}

