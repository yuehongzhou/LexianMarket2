package com.example.administrator.lexianmarket.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.bean.user.Trolley;
import com.example.administrator.lexianmarket.helper.CashItem;
import com.example.administrator.lexianmarket.helper.CashParams;
import com.example.administrator.lexianmarket.view.ConfirmPayTypeDialog;
import com.example.administrator.lexianmarket.view.PayDialog.PayDialog;

import java.util.ArrayList;
import java.util.List;

public class ConfirmExitDialog extends Dialog {
    private Button btnPayConfirm;

    public ConfirmExitDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_exit);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
        initView();
        setListener();
    }

    private void initView() {
        btnPayConfirm = (Button) findViewById(R.id.btn_pay_confirm);
    }

    private void setListener() {
        btnPayConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
    }
}
