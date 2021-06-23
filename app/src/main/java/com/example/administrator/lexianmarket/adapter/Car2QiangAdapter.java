package com.example.administrator.lexianmarket.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.bean.adapterbean.ItemEntity;
import com.example.administrator.lexianmarket.fragment.JiaqiFragment;
import com.example.administrator.lexianmarket.recyclerviewHelper.IItemTouchHelperAdapter;
import com.example.administrator.lexianmarket.recyclerviewHelper.IItemTouchHelperViewHolder;

import java.util.Collections;
import java.util.List;

import static android.graphics.Color.*;
import static com.example.administrator.lexianmarket.R.drawable.shape_corner_yello;

public class Car2QiangAdapter extends
        RecyclerView.Adapter<Car2QiangAdapter.ItemViewHolder> implements IItemTouchHelperAdapter {


    Context context;
    private CarnoSelectAdapter carAdapter;


    public Car2QiangAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.jiaqi_bg2));
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.carno_view.setText(JiaqiFragment.car2qiangList.get(position).getLeftText());
        holder.qiang_view.setText(JiaqiFragment.car2qiangList.get(position).getRightText());

    }

    @Override
    public int getItemCount() {
        return JiaqiFragment.car2qiangList == null ? 0 : JiaqiFragment.car2qiangList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(JiaqiFragment.car2qiangList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        JiaqiFragment.car2qiangList.remove(position);
        notifyItemRemoved(position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements IItemTouchHelperViewHolder {
        private TextView carno_view;
        private TextView qiang_view;

        ItemViewHolder(View itemView) {
            super(itemView);
            carno_view = itemView.findViewById(R.id.item_list_text_left);
            qiang_view = itemView.findViewById(R.id.item_list_text_right);
        }

        @Override
        public void onItemSelected() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setTranslationZ(10);
            }
        }

        @Override
        public void onItemClear() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setTranslationZ(0);
            }
        }
    }

}
