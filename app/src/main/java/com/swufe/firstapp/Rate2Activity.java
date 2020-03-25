package com.swufe.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Rate2Activity extends AppCompatActivity {
    String kind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate2);
    }
    public void onClick(View btn){
        Button bntkind = findViewById(R.id.bntKind_rate);
        kind = ((Button)btn).getText().toString();
        Log.i("main",kind);
        //bntkind.setText(kind);行不通
        Intent rate = new Intent(this,RateActivity.class);
        rate.putExtra("kind",kind);
        startActivity(rate);
    }
}
