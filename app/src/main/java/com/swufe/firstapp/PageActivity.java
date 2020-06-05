package com.swufe.firstapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class PageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        ViewPager viewPager = findViewById(R.id.viewpager);
        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        TabLayout tablayout = findViewById(R.id.sliding_tabs);
        tablayout.setupWithViewPager(viewPager);

    }



}
