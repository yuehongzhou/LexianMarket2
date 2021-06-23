package com.example.administrator.lexianmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lexianmarket.activity.MyCalendar;
import com.example.administrator.lexianmarket.activity.OrdersActivity;
import com.example.administrator.lexianmarket.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChoose extends AppCompatActivity {
    private ImageView iv;
    private TextView tv;
    private Button btn_look_today,btn_look_week,btn_look_month;
    private MyCalendar mMyCalendar;
    private ImageView commonTitleLeftBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_choose);
        ((TextView) findViewById(R.id.common_title_content)).setText("加气统计");
        commonTitleLeftBack = (ImageView) findViewById(R.id.common_title_left_back);
        iv = findViewById(R.id.imageView);
        tv = findViewById(R.id.textView);
        btn_look_today = (Button) findViewById(R.id.button);
        btn_look_week = (Button) findViewById(R.id.button2);
        btn_look_month = (Button) findViewById(R.id.button3);
        mMyCalendar= (MyCalendar) findViewById(R.id.mycalendar);
        mMyCalendar.setOnCalendarClick(new MyCalendar.MyCalendarclickListener() {
            @Override
            public void onCalendarItemClick(Date date) {
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                showNavExpenseActivity(date.getTime(), date.getTime(),OrdersActivity.mode_day);
            }
        });
        setListener();
    }
    private void setListener() {
        commonTitleLeftBack.setOnClickListener(new DateChoose.OnBackClickListener());
    }
    private class OnBackClickListener implements View.OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.activity_close_in_anim, R.anim.activity_close_out_anim);
        }
    }
    public void look(View v) {
        Long curtime = DateUtils.getCurrentTime();
        switch (v.getId()) {
            case R.id.button:
                showNavExpenseActivity(curtime, curtime,OrdersActivity.mode_day);
                break;
            case R.id.button2:
                showNavExpenseActivity(DateUtils.getFirstOfWeek(curtime),DateUtils.getLastOfWeek(curtime),OrdersActivity.mode_week);
                break;
            case R.id.button3:
                showNavExpenseActivity(DateUtils.getFirstOfMonth(curtime),DateUtils.getLastOfMonth(curtime),OrdersActivity.mode_month);
                break;
        }
    }
    private void showNavExpenseActivity(long startTime, long endTime,int mode)
    {
        Intent intent = new Intent(this, OrdersActivity.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra(OrdersActivity.str_mode, mode);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_in_anim, R.anim.activity_open_out_anim);
    }

}