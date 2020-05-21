package com.swufe.firstapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RatesListActivity extends ListActivity implements Runnable{
    String[] data = {"wait......"};
    Handler handler;
    String TAG = "List";
    private String logDate ="";
    private final String DATE_SP_KEY = "lastRateDateStr" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rates_list);//ListActivity中已经有一个页面布局，所以这里必须删去
        SharedPreferences sp = getSharedPreferences( "myrate", Context.MODE_PRIVATE);
        logDate = sp. getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);


        List<String> list1 = new ArrayList<String>();//链表的话不固定长度
        for(int i=1;i<100;i++){
            list1. add("item" + i);
        }
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//当前对象，布局，数据；泛型，这里数据项是String
        setListAdapter(adapter);//在ListActivity中已经写好了该方法，用来把当前界面交给adapter来管理，数据和listview绑定起来
        Thread thread = new Thread(this);//要有this，才能找到run()
        thread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {//这样就不用单独创建一个类
                super.handleMessage(msg);
                if(msg.what==7){
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RatesListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
            }
        };
    }


    @Override
    public void run() {
        //获取网络数据，放入List带回到主线程中
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> retlist = new ArrayList<String>();//该list是为了带回主线程
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd" )).format(new Date());
        Log.i("run","curDateStr:" + curDateStr +"logDate:" + logDate);
        if(curDateStr. equals(logDate)){
            //如果相等，则不从网络中获取数据
            Log.i("run","日期相等，从数据库中获取数据");
            RateManager dbManager = new RateManager(RatesListActivity.this);
            for(RateItem rateItem : dbManager.listAll()){
                retlist.add(rateItem.getCurName() +"-->" + rateItem.getCurRate());
            }
        }else {
            Log.i("run","日期不相等，从网络中获取数据");
            Document doc;
            try {
                List<RateItem> rateList = new ArrayList<RateItem>();//该list是为了写入数据库
                Log.i("run","运行1");
                doc = Jsoup.connect("https://www.usd-cny.com/bankofchina.htm").get();
                Log.i("run","运行2");
                Elements tables = doc.getElementsByTag("table");
                Element table1 = tables.get(0);

                Log.i(TAG, "table1=" + table1);
                Elements tds = table1.getElementsByTag("td");
                Log.i("run","运行3");
                for (int i = 0; i < tds.size(); i += 6) {
                    Element td1 = tds.get(i);//币种在每行第一列
                    Element td2 = tds.get(i + 5);//汇率在每行第六列
                    Log.i(TAG, "run: " + td1.text() + "=>" + td2.text());//text()和html()都可以,因为这里td之间只有文本
                    String str1 = td1.text();
                    String val = td2.text();
                    retlist.add(str1 + "==>" + val);
                    rateList.add(new RateItem(str1,val));
                }
                //把数据写入到数据库中
                RateManager manager = new RateManager(this);
                manager.deleteAll();
                manager.addAll(rateList);
                //更新记录日期
                SharedPreferences sp = getSharedPreferences( "myrate", Context .MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY,curDateStr);
                edit.commit();
                Log. i("run", "更新日期结束:"+ curDateStr);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message msg = handler.obtainMessage(7);
        //  msg.what = 5;//用于标记当前msg
        //msg.obj = "Hello from run()";
        msg.obj = retlist;
        handler.sendMessage(msg);

    }

}
