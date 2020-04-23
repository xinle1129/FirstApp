package com.swufe.firstapp;

import android.app.ListActivity;
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
import java.util.ArrayList;
import java.util.List;

public class RatesListActivity extends ListActivity implements Runnable{
    String[] data = {"wait......"};
    Handler handler;
    String TAG = "List";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rates_list);//ListActivity中已经有一个页面布局，所以这里必须删去
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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> retlist = new ArrayList<String>();
        Document doc;
        try {
            doc = Jsoup.connect("https://www.usd-cny.com/bankofchina.htm").get();
            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(0);
            Log.i(TAG,"table1="+table1);
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<tds .size();i+=6){
                Element td1 = tds .get(i);//币种在每行第一列
                Element td2 = tds .get(i+5);//汇率在每行第六列
                Log.i(TAG,"run: " + td1. text() + "==>" + td2. text());//text()和html()都可以,因为这里td之间只有文本
                String str1 = td1.text();
                String val = td2.text();
                retlist.add(str1+"==>"+val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        //  msg.what = 5;//用于标记当前msg
        //msg.obj = "Hello from run()";
        msg.obj = retlist;
        handler.sendMessage(msg);

    }

}
