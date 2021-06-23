/**
 *  Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lexianmarket.R;
import com.example.administrator.lexianmarket.activity.AddLicence;
import com.example.administrator.lexianmarket.activity.JiaqiPayInfoActivity;
import com.example.administrator.lexianmarket.activity.MainActivity;
import com.example.administrator.lexianmarket.adapter.Car2QiangAdapter;
import com.example.administrator.lexianmarket.adapter.Car2TimeAdapter;
import com.example.administrator.lexianmarket.adapter.CarnoSelectAdapter;
import com.example.administrator.lexianmarket.bean.JiaqiCar;
import com.example.administrator.lexianmarket.bean.adapterbean.ItemEntity;
import com.example.administrator.lexianmarket.bean.city.Citys;
import com.example.administrator.lexianmarket.bean.user.Jiaqi;
import com.example.administrator.lexianmarket.helper.LoginHelper;
import com.example.administrator.lexianmarket.helper.Page;
import com.example.administrator.lexianmarket.helper.ResultHelper;
import com.example.administrator.lexianmarket.recyclerviewHelper.MyItemTouchHelperCallback;
import com.example.administrator.lexianmarket.recyclerviewHelper.OnStartDragListener;
import com.example.administrator.lexianmarket.service.JiaqiService;
import com.example.administrator.lexianmarket.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 王晨昕
 * @version 1.0
 */
public class JiaqiFragment extends Fragment{

    private EditText etJiaqiCarno;//加气车牌号
    private Button btnJiaqi,carnoListBt,btnAddlicence;//加气，选择车牌，添加车牌
    private CarnoSelectAdapter carAdapter;
    private static final int JIAQI_MESSAGE=1;//加气
    private static final int JIAQI_CARS=2;//查找车辆
    private static final int ASS_CARS=3;//添加车辆
    private TextView mTvNoLogin;//未登录页面
    private String content;//车牌号
    private String jiaqiCarno;//车牌号
    private String content2;//加气量
    private ScrollView scrollview;//登录后的加气页面
    int eventSelected=0;
    List<String> carnoList;//获取车辆
    AlertDialog alertDialog;
    View choiceView;

    public static List<ItemEntity> car2timeList = new ArrayList<ItemEntity>();
    public static List<ItemEntity> car2qiangList = new ArrayList<ItemEntity>();
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper mItemTouchHelper2;
    private Car2TimeAdapter car2TimeAdapter;
    private Car2QiangAdapter car2QiangAdapter;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            int whatInt = msg.what;
            switch (whatInt){
                case JIAQI_MESSAGE:
                    ResultHelper result= (ResultHelper) msg.obj;
                    showInfo(result);
                    break;
                case JIAQI_CARS:
                    result= (ResultHelper<List<Citys>>) msg.obj;
                    if(result.getCode()== Constant.CODE_SUCCESS){
                        bindCarsAdapterData(result);
                    }
                    break;
                case ASS_CARS:
                    result= (ResultHelper) msg.obj;
                    showAddCarInfo(result);
                    break;
            }
            super.handleMessage(msg);

            if(whatInt == JIAQI_MESSAGE){
                Intent intent = new Intent();
                //加气成功，跳转到支付页面
                ComponentName comp = new ComponentName(getActivity(), JiaqiPayInfoActivity.class);
                intent.setComponent(comp);
                intent.putExtra("carno",jiaqiCarno);
                startActivity(intent);
            }

        }
    };

    private void showInfo(ResultHelper result) {

        if(result.getCode()== Constant.CODE_SUCCESS){
            LoginHelper.isLogin=true;
            Toast.makeText(getActivity(), "加气信息填写成功", Toast.LENGTH_SHORT).show();
            System.out.println(result.getData());
        }else if(result.getCode()== Constant.CODE_PHONE_USED){
            Toast.makeText(getActivity(), "加气失败", Toast.LENGTH_SHORT).show();
        }
    }


    private void showAddCarInfo(ResultHelper result) {

        if(result.getCode()== Constant.CODE_SUCCESS){
            LoginHelper.isLogin=true;
            Toast.makeText(getActivity(), "添加车牌成功", Toast.LENGTH_SHORT).show();
            System.out.println(result.getData());
        }else if(result.getCode()== Constant.INSERT_ERROR){
            Toast.makeText(getActivity(), "添加车牌失败", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        String chepai = ((MainActivity)context).chepai;
        if(chepai!=null) {
            etJiaqiCarno.setText(chepai);
        }

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_jiaqi, container, false);

            initView(rootView);
            setListener();
            initData();
            initView2(rootView);


        btnAddlicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddLicence.class);
                startActivity(intent);
            }
        });
        /*Intent intent = getActivity().getIntent();
        content = intent.getStringExtra("content");
        content2 = intent.getStringExtra("content2");
        if (!TextUtils.isEmpty(content)) {
            etJiaqiCarno.setText(content);
        }*/
        return rootView;
    }


    @Override
    public void onResume() {
        initData();
        super.onResume();
    }


    private void initView(View rootView) {
            mTvNoLogin = (TextView) rootView.findViewById(R.id.tv_no_login);        //未登录的页面
            scrollview = (ScrollView) rootView.findViewById(R.id.scrollview);       //登录后的加气页面
            /*etJiaqiAmount = (EditText) rootView.findViewById(R.id.et_jiaqi_amount); //加气量
            etJiaqiCarno = (EditText) rootView.findViewById(R.id.et_jiaqi_carno);   //车牌号*/
            btnJiaqi = (Button) rootView.findViewById(R.id.btn_jiaqi);              //支付
            btnAddlicence = (Button) rootView.findViewById(R.id.btn_addlicence);    //添加车牌
            /*carnoListBt = (Button) rootView.findViewById(R.id.carnoListBt);         //选择车牌*/
            choiceView = getChoiceView();
            alertDialog = new AlertDialog.Builder(getActivity())
                    .setView(choiceView)
                    .create();

    }

    /*private void initViews(View rootView) {
        spinnerCar = (Spinner) rootView.findViewById(R.id.car_spinner);

    }*/

    private void initData() {

        car2timeList = new ArrayList<ItemEntity>();
        String [] times = {"10:00"};
            String [] cars = {"豫S10000"};
            for (int i=0;i<cars.length;i++) {
                ItemEntity item = new ItemEntity();
                item.setLeftText(cars[i]);
                item.setRightText(times[i]);
                car2timeList.add(item);
            }


        car2qiangList = new ArrayList<ItemEntity>();

        if(LoginHelper.isLogin){
            JiaqiService.getJiaqiCars("1","1590567478","1655372096",JIAQI_CARS,handler);
        }else {
            mTvNoLogin.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        }

    }


    private void initView2(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false));
        car2TimeAdapter = new Car2TimeAdapter(rootView.getContext());

        recyclerView.setAdapter(car2TimeAdapter);
        mItemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(car2TimeAdapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        RecyclerView recyclerView2 = rootView.findViewById(R.id.main_recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false));
        car2QiangAdapter = new Car2QiangAdapter(rootView.getContext());

        recyclerView2.setAdapter(car2QiangAdapter);
        mItemTouchHelper2 = new ItemTouchHelper(new MyItemTouchHelperCallback(car2QiangAdapter));
        mItemTouchHelper2.attachToRecyclerView(recyclerView2);

        car2TimeAdapter.setCar2QiangAdapter(car2QiangAdapter);

        Bundle b = getArguments();
        if(b!=null) {
            String chepai = b.getString("chepai");
            if(chepai!=null) {
                /*etJiaqiCarno.setText(chepai);*/

                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setLeftText(chepai);

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                itemEntity.setRightText(hour+":"+minute);

                car2timeList.add(itemEntity);
                car2TimeAdapter.notifyDataSetChanged();
                JiaqiService.add_car(chepai,ASS_CARS, handler);
            }
        }

    }


    private View getChoiceView() {

        //R.layout.dialog_choice就是GridView所在的那个布局，下面有介绍
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.car_dialog_choice, null);
        GridView gv = (GridView) view.findViewById(R.id.gv_choice);
        //GridView的数据源，直接从strings.xml中加载过来

//        data = Arrays.asList(getResources().getStringArray(R.array.event));
        carnoList = new ArrayList<String>();
        carAdapter = new CarnoSelectAdapter(getActivity(), carnoList);
        gv.setAdapter(carAdapter);

        //监听点击事件，点击以后，之前的选中应该变为未选中
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                eventSelected = position;
                //将选择的内容设置到底部的按钮上去
                etJiaqiCarno.setText(carnoList.get(position).toString());

                alertDialog.dismiss();
                carAdapter.changeState(position);
            }
        });
        return view;
    }

    private void bindCarsAdapterData(ResultHelper<Page<JiaqiCar>> result) {

        Page<JiaqiCar> page = result.getData();
        int pageNo = page.getPageNo();
        int totalPageNum = page.getPageNums();
        List<JiaqiCar> cars = page.getData();
        if (cars != null && cars.size() > 0) {

            /*String [] times = {"10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40"};
            String [] cars = {"豫S10000","豫S10001","豫S10002","豫S10003","豫S10004","豫S10005","豫S10006","豫S10007","豫S10008"};
            for (int i=0;i<cars.length;i++) {
                ItemEntity item = new ItemEntity();
                item.setLeftText(cars[i]);
                item.setRightText(times[i]);
                car2timeList.add(item);
            }*/

            for(JiaqiCar car : cars){
                String carno = car.getCarno();
                long l_intime = car.getInTime();
                SimpleDateFormat format =  new SimpleDateFormat( "HH:mm" );
                String str_intime = format.format(l_intime);

                ItemEntity item = new ItemEntity();
                item.setLeftText(carno);
                item.setRightText(str_intime);
                car2timeList.add(item);
            }

            if(car2TimeAdapter!=null)
                car2TimeAdapter.notifyDataSetChanged();

        }

        mTvNoLogin.setVisibility(View.GONE);//未登录页面不可见
        scrollview.setVisibility(View.VISIBLE);//加气页面可见


    }

    private void setListener() {
        btnJiaqi.setOnClickListener(new JiaqiFragment.OnRegisterListener());

        /*
        carnoListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        */
    }

    private class OnRegisterListener implements View.OnClickListener{
        //加气

        @Override
        public void onClick(View v) {
            boolean isValid = checkParams();
            if(isValid){
                //etJiaqiAmount etJiaqiCarno btnJiaqi
                String jiaqiAmount = "10";
                jiaqiCarno = etJiaqiCarno.getText().toString();

                Jiaqi jiaqi = new Jiaqi();
                jiaqi.setAmount(jiaqiAmount);
                jiaqi.setCarno(jiaqiCarno);

                JiaqiService.jiaqi(jiaqi,JIAQI_MESSAGE,handler);
            }
        }
    }

    private boolean checkParams() {
        String jiaqiCarno = etJiaqiCarno.getText().toString();

        if("".equals(jiaqiCarno)){
            Toast.makeText(getActivity(), "加气失败！车牌号为空",
                    Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }
}