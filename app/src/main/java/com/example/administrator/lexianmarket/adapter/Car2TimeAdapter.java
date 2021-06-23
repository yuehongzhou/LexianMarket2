package com.example.administrator.lexianmarket.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.app.AlertDialog;


import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.bean.adapterbean.ItemEntity;
import com.example.administrator.lexianmarket.fragment.JiaqiFragment;
import com.example.administrator.lexianmarket.recyclerviewHelper.IItemTouchHelperAdapter;
import com.example.administrator.lexianmarket.recyclerviewHelper.IItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Car2TimeAdapter extends
        RecyclerView.Adapter<Car2TimeAdapter.ItemViewHolder> implements IItemTouchHelperAdapter {

    Car2QiangAdapter car2qiangAdapter;

    ItemEntity current_itemEntity = new ItemEntity();

    Context context;
    AlertDialog alertDialog;
    private CarnoSelectAdapter carAdapter;

    public void setCar2QiangAdapter(Car2QiangAdapter car2qiangAdapter) {
        this.car2qiangAdapter = car2qiangAdapter;
    }

    public Car2TimeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.jiaqi_bg1));
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.time_view.setText(JiaqiFragment.car2timeList.get(position).getRightText());
        holder.carno_view.setText(JiaqiFragment.car2timeList.get(position).getLeftText());

    }

    @Override
    public int getItemCount() {
        return JiaqiFragment.car2timeList == null ? 0 : JiaqiFragment.car2timeList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(JiaqiFragment.car2timeList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        ItemEntity car_item = JiaqiFragment.car2timeList.get(position);
        current_itemEntity = new ItemEntity();
        if(car_item!=null){
            String carno = JiaqiFragment.car2timeList.get(position).getLeftText();
            current_itemEntity.setLeftText(carno);
        }

        JiaqiFragment.car2timeList.remove(position);
        notifyItemRemoved(position);

        View choiceView = getChoiceView();
        alertDialog = new AlertDialog.Builder(context)
                .setView(choiceView)
                .create();
        alertDialog.show();
    }

    private View getChoiceView() {

        //R.layout.dialog_choice就是GridView所在的那个布局，下面有介绍
        View view = LayoutInflater.from(context).inflate(R.layout.car_dialog_choice, null);
        GridView gv = (GridView) view.findViewById(R.id.gv_choice);
        //GridView的数据源，直接从strings.xml中加载过来

        final ArrayList<String> carnoList = new ArrayList<String>();
        carnoList.add("1号枪");
        carnoList.add("2号枪");
        carnoList.add("3号枪");
        carnoList.add("4号枪");
        carnoList.add("5号枪");
        carnoList.add("6号枪");
        carnoList.add("7号枪");
        carnoList.add("8号枪");
        carnoList.add("9号枪");
        carnoList.add("10号枪");
        carAdapter = new CarnoSelectAdapter(context, carnoList);
        gv.setAdapter(carAdapter);

        //监听点击事件，点击以后，之前的选中应该变为未选中
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String qiang_no = carnoList.get(position);
                //将选择的内容设置到底部的按钮上去
                /*etJiaqiCarno.setText(carnoList.get(position).toString());*/
                Toast.makeText(context, "你单选了" +qiang_no, Toast.LENGTH_LONG).show();

                if(current_itemEntity!=null && current_itemEntity.getLeftText()!=null && !current_itemEntity.getLeftText().equals("")){
                    current_itemEntity.setRightText(qiang_no);

                    if(JiaqiFragment.car2qiangList!=null){
                        JiaqiFragment.car2qiangList.add(current_itemEntity);
                    }
                }

                alertDialog.dismiss();
                carAdapter.changeState(position);

                car2qiangAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements IItemTouchHelperViewHolder {
        private TextView carno_view;
        private TextView time_view;

        ItemViewHolder(View itemView) {
            super(itemView);
            carno_view = itemView.findViewById(R.id.item_list_text_left);
            time_view = itemView.findViewById(R.id.item_list_text_right);
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
