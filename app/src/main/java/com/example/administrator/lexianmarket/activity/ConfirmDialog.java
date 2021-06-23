package com.example.administrator.lexianmarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.lexianmarket.R;

public class ConfirmDialog extends Activity {
    private String content;
    private String content2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_dialog);
        Button alert_dialog_btn_confirm = findViewById(R.id.alert_dialog_btn_confirm);
        Button alert_dialog_btn_cancel = findViewById(R.id.alert_dialog_btn_cancel);
        TextView alert_dialog_text = findViewById(R.id.alert_dialog_text);
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        content2 = intent.getStringExtra("content2");
        if (!TextUtils.isEmpty(content)) {
            alert_dialog_text.setText(content);
        }
    }

    public void cdListener(View v) {
        switch (v.getId()) {
            case R.id.alert_dialog_btn_confirm:
                /*Intent intent = new Intent(ConfirmDialog.this, JiaqiActivity.class);
                intent.putExtra("content", content);
                intent.putExtra("content2", content2);
                startActivity(intent);*/
                finish();
                break;
            case R.id.alert_dialog_btn_cancel:
                finish();
                break;
        }
    }
}