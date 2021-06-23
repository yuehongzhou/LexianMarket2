/**
 *  Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.service;

import android.os.Handler;
import android.os.Message;

import com.example.administrator.lexianmarket.bean.JiaqiCar;
import com.example.administrator.lexianmarket.bean.city.Citys;
import com.example.administrator.lexianmarket.bean.user.Jiaqi;
import com.example.administrator.lexianmarket.bean.user.Orders;
import com.example.administrator.lexianmarket.bean.user.Pay;
import com.example.administrator.lexianmarket.bean.user.User;
import com.example.administrator.lexianmarket.helper.CashParams;
import com.example.administrator.lexianmarket.helper.Page;
import com.example.administrator.lexianmarket.helper.ResultHelper;
import com.example.administrator.lexianmarket.utils.HttpClientUtils;
import com.example.administrator.lexianmarket.utils.UrlConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 郝伟
 * @version 1.0
 */
public class JiaqiService {


    public static void jiaqi(Jiaqi jiaqi,final int type, final Handler handler){
        String url = UrlConstant.PROJECT_URL+"user/jiaqi/jiaqi.do";
        HttpClientUtils client = new HttpClientUtils();
        Map<String,String> params =new HashMap<>();

        params.put("amount",jiaqi.getAmount());
        params.put("carno",jiaqi.getCarno());
        client.post(url,params,new HttpClientUtils.ServiceCallback() {
            @Override
            public void onFailure(HttpException e, String errorMessage) {

            }
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ResultHelper resultHelper=gson.fromJson(result,
                        new TypeToken<ResultHelper>() {}.getType());
                Message msg=new Message();
                msg.obj=resultHelper;
                msg.what=type;
                handler.sendMessage(msg);
            }
        });
    }


    public static void add_car(String carno,final int type, final Handler handler){
        String url = UrlConstant.PROJECT_URL+"user/jiaqi/add_car.do";
        HttpClientUtils client = new HttpClientUtils();
        Map<String,String> params =new HashMap<>();

        params.put("carno",carno);
        client.post(url,params,new HttpClientUtils.ServiceCallback() {
            @Override
            public void onFailure(HttpException e, String errorMessage) {

            }
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ResultHelper resultHelper=gson.fromJson(result,
                        new TypeToken<ResultHelper>() {}.getType());
                Message msg=new Message();
                msg.obj=resultHelper;
                msg.what=type;
                handler.sendMessage(msg);
            }
        });
    }


    public static void payway(Pay payWay, final int type, final Handler handler){
        String url = UrlConstant.PROJECT_URL+"user/jiaqi/jiaqii.do";
        HttpClientUtils client = new HttpClientUtils();
        Map<String,String> paramss =new HashMap<>();
        paramss.put("payway",payWay.getPayway());
        paramss.put("state",payWay.getState().toString());
        paramss.put("money",payWay.getMoney());
        client.post(url,paramss,new HttpClientUtils.ServiceCallback() {
            @Override
            public void onFailure(HttpException e, String errorMessage) {

            }
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ResultHelper resultHelper=gson.fromJson(result,
                        new TypeToken<ResultHelper>() {}.getType());
                Message msg=new Message();
                msg.obj=resultHelper;
                msg.what=type;
                handler.sendMessage(msg);
            }
        });
    }



    public static void getJiaqiCars(String pageNo,String intime,String outtime,final int type,final Handler handler){
        String url = UrlConstant.PROJECT_URL+"user/jiaqi/jiaqicars.do";
        HttpClientUtils client = new HttpClientUtils();

        Map<String,String> params =new HashMap<>();
        params.put("pageNo",pageNo);
        params.put("intime",intime);
        params.put("outtime",outtime);

        client.post(url,params,new HttpClientUtils.ServiceCallback() {
            @Override
            public void onFailure(HttpException e, String errorMessage) {

            }
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ResultHelper<Page<JiaqiCar>> resultHelper=gson.fromJson(result,
                        new TypeToken<ResultHelper<Page<JiaqiCar>>>() {}.getType());
                Message msg=new Message();
                msg.obj=resultHelper;
                msg.what=type;
                handler.sendMessage(msg);
            }
        });

    }

}
