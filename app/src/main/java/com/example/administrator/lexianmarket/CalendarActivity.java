package com.example.administrator.lexianmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.lexianmarket.activity.MyCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends Activity {

    private MyCalendar mMyCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mMyCalendar= (MyCalendar) findViewById(R.id.mycalendar);
        mMyCalendar.setOnCalendarClick(new MyCalendar.MyCalendarclickListener() {
            @Override
            public void onCalendarItemClick(Date date) {
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                Toast.makeText(getApplicationContext(),format.format(date),Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("result", date.getTime());
                CalendarActivity.this.setResult(1, intent);
                CalendarActivity.this.finish();
            }

        });

    }
}