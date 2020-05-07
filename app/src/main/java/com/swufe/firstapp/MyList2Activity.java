package com.swufe.firstapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable, AdapterView.OnItemClickListener {
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器
    private int msgWhat = 7;
    final String TAG = "MyList2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(listItemAdapter);

        //MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItems);
        //this.setListAdapter(myAdapter);

        Thread t = new Thread(this); //创建新线程
        t.start(); //开启线程
        handler = new Handler() {
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 7) {
                    List<HashMap<String, String>> list2 = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this, list2, // listItems数据源
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail}
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
//        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {//可以用匿名类
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        getListView().setOnItemClickListener(this);//需要继承AdapterView.OnItemClickListener
    }
    private void initListView(){
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate : " + i); //标题文字
            map.put("ItemDetail", "detail" + i); //详情描述
            listItems.add(map);
            //生成适配器的Item和动态数组对应的元素
            listItemAdapter = new SimpleAdapter(this, listItems, // listItems数据源
                    R.layout.list_item, // ListItem的XML布局实现
                    new String[]{"ItemTitle", "ItemDetail"},
                    new int[]{R.id.itemTitle, R.id.itemDetail}
            );
        }
    }

    @Override
    public void run() {
        Log.i("thread", "run.....");
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
        try {
            Document doc = Jsoup.connect("https://www.usd-cny.com/bankofchina.htm").get();
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
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                rateList.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = rateList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//view是行，parent是listview？
        Log. i(TAG, "onItemClick: parent=" + parent);
        Log.i(TAG,"onItemClick: view=" + view);
        Log. i(TAG, "onItemClick: position" + position);
        Log. i(TAG, "onItemClick: id=" + id);
        HashMap<String,String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);//获取数据项
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG,"onItemClick: titleStr=" + titleStr);
        Log.i(TAG,"onItemClick: detailStr=" + detailStr);
        TextView title = (TextView) view.findViewById(R.id. itemTitle);//获取布局控件
        TextView detail = (TextView) view.findViewById(R. id. itemDetail);
        String title2 = String.valueOf(title. getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG,"onItemClick: title2=" + title2);
        Log.i(TAG, "onItemClick: detail2=" + detail2);

        //打开新的页面，传入参数
        Intent rateCalc = new Intent(this, RateCalcActivity.class);
        rateCalc. putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);


    }
}
