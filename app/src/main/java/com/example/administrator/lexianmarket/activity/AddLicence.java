package com.example.administrator.lexianmarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.administrator.lexianmarket.R;

public class AddLicence extends Activity implements LicensePlateView.InputListener {
    private LicensePlateView mPlateView;
    private Button button;
    private boolean isActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_licence);
        mPlateView = findViewById(R.id.activity_lpv);
        RelativeLayout mContainer = findViewById(R.id.main_rl_container);
        button = findViewById(R.id.btn_change);
        Button btn_confirm = findViewById(R.id.ok);
        Button btn_clean = findViewById(R.id.btn_clean);

        Intent intent = getIntent();
        mPlateView.setInputListener(this);

//        System.out.println(LicensePlateView.string);

        mPlateView.setKeyboardContainerLayout(mContainer);
        mPlateView.hideLastView();
        mPlateView.onSetTextColor(R.color.colorAccent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActive) {
                    isActive = false;
                    button.setText("+ 新能源车牌");
                    mPlateView.hideLastView();
                } else {
                    isActive = true;
                    button.setText("切换为普通汽车");
                    mPlateView.showLastView();
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editContent = mPlateView.getEditContent();
                /*Intent intent=new Intent(AddLicence.this,ConfirmDialog.class);
                intent.putExtra("content",editContent);
                intent.putExtra("content2",content);
                startActivity(intent);*/

                Intent intent=new Intent(AddLicence.this,MainActivity.class);
                intent.putExtra("id",2);
                intent.putExtra("chepai",editContent);
                startActivity(intent);
            }
        });
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlateView.clearEditText();
            }
        });
    }

    @Override
    public void inputComplete(String content) {

    }

    @Override
    public void deleteContent() {

    }
}