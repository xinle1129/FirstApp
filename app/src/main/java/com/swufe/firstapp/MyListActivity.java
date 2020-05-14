package com.swufe.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayAdapter adapter;
    List<String> data = new ArrayList<String>();
    @Override
    //利用自己创建的list布局
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        ListView listView = findViewById(R.id.mylist);
        for(int i=0;i<10;i++) {
            data.add("item" + i);
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//当前对象，布局，数据；泛型，这里数据项是String
        listView.setAdapter(adapter);//不能用setListAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));//若数据为空，则调用nodata
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.remove(parent.getItemAtPosition(position));
        //adapter.notifyDataSetChanged();通过adapter删除数据时，notifyDataSetChanged()会被remove自动调用
        // 这里用的是ArrayAdapter类型
    }
}
