package com.swufe.firstapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RateCalcActivity extends AppCompatActivity {
    String TAG = "rateCalc";
    float rate = 0f;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        //String title = getIntent().getStringExtra("tit1e");
        String title = getIntent().getStringExtra("title");
        rate = getIntent().getFloatExtra("rate" ,0f);
        Log.i(TAG,"onCreste: title = " + title);
        Log.i(TAG,"onCreate: rate=" + rate);
        ((TextView)findViewById(R.id.RateCalc_title)). setText(title);
        input = findViewById(R.id.RateCalc_input);
        input.addTextChangedListener(new TextWatcher() {//文本监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {//用户输入完成后
                TextView show = RateCalcActivity.this.findViewById(R.id.RateCalc_show);
                if (s.length() > 0) {
                    float val = Float.parseFloat(s.toString());
                    show.setText(val + "RMB==>" + (100/rate * val));
                }else{
                    show.setText("");
                }
            }
        });


    }
}
