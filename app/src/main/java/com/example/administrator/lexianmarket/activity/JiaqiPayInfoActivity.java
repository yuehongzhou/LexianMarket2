package com.example.administrator.lexianmarket.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.lexianmarket.DateChoose;
import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.bean.city.Citys;
import com.example.administrator.lexianmarket.bean.user.Jiaqi;
import com.example.administrator.lexianmarket.bean.user.Pay;
import com.example.administrator.lexianmarket.helper.LoginHelper;
import com.example.administrator.lexianmarket.helper.ResultHelper;
import com.example.administrator.lexianmarket.service.JiaqiService;
import com.example.administrator.lexianmarket.utils.Constant;
import com.example.administrator.lexianmarket.utils.ScreenUtils;

import java.util.List;

public class JiaqiPayInfoActivity extends AppCompatActivity {

    EditText payMoneyEdit,payMethodEdit;
    private RadioGroup radioGroup=null;
    private Button pay_btn_jiaqi;
    private String payway;
    private static final int PAY_MESSAGE=1;
    private String carno;
    private Integer state = 1;
    private ImageView commonTitleLeftBack;
    private String money;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            int whatInt = msg.what;
            switch (whatInt){
                case PAY_MESSAGE:
                    ResultHelper result= (ResultHelper) msg.obj;
                    showInfo(result);
                    break;
            }
            super.handleMessage(msg);

        }
    };
    private void showInfo(ResultHelper result) {

        if(result.getCode()== Constant.CODE_SUCCESS){
            LoginHelper.isLogin=true;
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            finish();
        }else if(result.getCode()== Constant.CODE_PHONE_USED){
            Toast.makeText(this, "加气失败", Toast.LENGTH_SHORT).show();
        }else if(result.getCode()== Constant.CODE_NO_PAY){
            Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiaqi_pay_info);
        payMoneyEdit = (EditText)findViewById(R.id.pay_et_jiaqi_money);
        payMoneyEdit.setText("123.45");
        commonTitleLeftBack = (ImageView) findViewById(R.id.common_title_left_back);
        pay_btn_jiaqi = (Button)findViewById(R.id.pay_btn_jiaqi);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        RadioButton radioButton_wugan = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton radioButton_xuni = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton radioButton_etc = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton radioButton_cash = (RadioButton) findViewById(R.id.radioButton4);
        radioGroup.setOnCheckedChangeListener(listen);
        pay_btn_jiaqi.setOnClickListener(onClick);
        commonTitleLeftBack.setOnClickListener(new OnBackClickListener());
        /*payMethodEdit = (EditText)findViewById(R.id.pay_et_jiaqi_method);
        payMethodEdit.setText("请选择");*/

//        pay_et_jiaqi_method
        Intent intent = getIntent();
        carno = intent.getStringExtra("carno");
        System.out.println(carno);

        System.out.println();

    }
    private class OnBackClickListener implements View.OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            showDialog();
            //finish();
            //overridePendingTransition(R.anim.activity_close_in_anim, R.anim.activity_close_out_anim);
        }
    }
    //初始化并弹出对话框方法
    private void showDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_exit,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        Button btnPayConfirm = view.findViewById(R.id.btn_pay_confirm);

        btnPayConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = payMoneyEdit.getText().toString();
                Pay payWay = new Pay();
                payWay.setPayway("0");
                payWay.setMoney("0");
                payWay.setState(2);
                JiaqiService.payway(payWay,PAY_MESSAGE,handler);
                dialog.dismiss();
            }
        });
        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(this)/5*4), (ScreenUtils.getScreenHeight(this)/4*1));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private RadioGroup.OnCheckedChangeListener listen=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id= group.getCheckedRadioButtonId();
            switch (id) {
                case R.id.radioButton1:
                   payway = "4";
                    break;
                case R.id.radioButton2:
                    payway = "1";
                    break;
                case R.id.radioButton3:
                    payway = "3";
                    break;
                case R.id.radioButton4:
                    payway = "2";
                    break;
                default:
                    break;
            }
        }
    };
    private View.OnClickListener onClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           money = payMoneyEdit.getText().toString();
           Pay payWay = new Pay();
           payWay.setPayway(payway);
           payWay.setMoney(money);
            payWay.setState(state);
           JiaqiService.payway(payWay,PAY_MESSAGE,handler);
        }
    };


}
