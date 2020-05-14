package com.swufe.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements Runnable,AdapterView.OnItemClickListener{
    Handler handler;
    String TAG = "Search";
    private List<HashMap<String, String>> listItems;
    private SimpleAdapter listItemAdapter; // 适配器
    private int msgWhat = 7;
    EditText input;
    int week;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final ListView listView = findViewById(R.id.search_list);
        input = findViewById(R.id.search_input);
        final String str = input.toString();

        SharedPreferences sharedPreferences = getSharedPreferences("mysearch", Activity.MODE_PRIVATE);
        String s1="ItemTitle",s2="ItemDetail",s3="Url",text,time,url;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
        for(int i =0;i<20;i++){
            HashMap<String, String> map1 = new HashMap<String, String>();
            text = sharedPreferences.getString(s1.concat(String.valueOf(i)),"");
            time = sharedPreferences.getString(s2.concat(String.valueOf(i)),"");
            url = sharedPreferences.getString(s3.concat(String.valueOf(i)),"");
            map1.put("ItemTitle",text);
            map1.put("ItemDetail",time);
            map1.put("Url",url);
            rateList.add(map1);//直接用listItems居然不行
            i++;
        }
        listItems = rateList;
        listItemAdapter = new SimpleAdapter(SearchActivity.this, listItems, // listItems数据源
                R.layout.list_item, // ListItem的XML布局实现
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail}
        );
        listView.setAdapter(listItemAdapter);

        Date date = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        week = calendar.get(Calendar.WEEK_OF_YEAR);

        if(week != sharedPreferences.getInt("Week",0)){
            Thread thread = new Thread(this);//要有this，才能找到run()
            thread.start();
        }else {
            Toast.makeText(SearchActivity.this,"无需更新",Toast.LENGTH_SHORT).show();
        }

        handler = new Handler() {
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 7) {
                    List<HashMap<String, String>> list2 = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(SearchActivity.this, list2, // listItems数据源
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail}
                    );
                    listView.setAdapter(listItemAdapter);
                   // setListAdapter(listItemAdapter);
                    SharedPreferences sharedPreferences = getSharedPreferences("mysearch", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int i=0;
                    String s1="ItemTitle",s2="ItemDetail",s3="Url",text,time,url;
                    for(HashMap<String, String> map0 : list2){
                        text = map0.get("ItemTitle");
                        time = map0.get("ItemDetail");
                        url = map0.get("Url");
                        editor.putString(s1.concat(String.valueOf(i)),text);
                        editor.putString(s2.concat(String.valueOf(i)),time);
                        editor.putString(s3.concat(String.valueOf(i)),url);
                        i++;
                    }
                    editor.putInt("Week",week);
                    editor.commit();
                    Toast.makeText(SearchActivity.this,"通知已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

        listView.setOnItemClickListener(this);
    }

    @Override
    public void run() {
        Bundle bundle =new Bundle();
        Document doc = null;
        //String str = input.getText().toString();
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
        try {
            doc = Jsoup.connect("https://it.swufe.edu.cn/index/tzgg.htm").get();
            Elements lis = doc.getElementsByTag("li");
//            int i0 = 1;
//            for(Element table : lis){
//                Log.i(TAG,"run: a["+i0+"]=" + table.getElementsByTag("a").attr("href"));
//                i0++;
//
//            }
            for(int j=65;j<85;j++){
                Elements spans = lis.get(j).getElementsByTag("span");
                String url0 = lis.get(j).getElementsByTag("a").attr("href");
                String url = "https://it.swufe.edu.cn/".concat(url0.substring(2));
                Log.i(TAG,"run: url["+j+"]=" + url);
                Element span1 = spans.get(0);
                Element span2 = spans.get(1);
                String text = span1.text();
                String time = span2.text();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle",text);
                map.put("ItemDetail",time);
                map.put("Url",url);
                rateList.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = rateList;
        listItems = rateList;
        handler.sendMessage(msg);
    }
    public void onClick(View btn){
        String str = input.getText().toString();
        if(str.length()==0){
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
        }else {
            List<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
            for(HashMap<String, String> map :listItems){
                Log.i(TAG,"listItem"+map);
                if(map.get("ItemTitle").contains(str)){
                    list1.add(map);
                }
            }
            if(list1.isEmpty()){
                Toast.makeText(this,"未查询到相关内容",Toast.LENGTH_SHORT).show();
            }else{
                listItemAdapter = new SimpleAdapter(SearchActivity.this, list1, // listItems数据源
                        R.layout.list_item, // ListItem的XML布局实现
                        new String[]{"ItemTitle", "ItemDetail"},
                        new int[]{R.id.itemTitle, R.id.itemDetail}
                );
                final ListView listView = findViewById(R.id.search_list);
                listView.setAdapter(listItemAdapter);
                Toast.makeText(this,"查询成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ListView listView = findViewById(R.id.search_list);
        HashMap<String,String> map = (HashMap<String, String>) listView.getItemAtPosition(position);//获取数据项
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        String url = map.get("Url");
        Log.i(TAG,"onItemClick: titleStr=" + titleStr);
        Log.i(TAG,"onItemClick: detailStr=" + detailStr);
        Log.i(TAG,"onItemClick: titleStr=" + url);
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);


    }


}
