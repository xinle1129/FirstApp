package com.swufe.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Rates extends AppCompatActivity {
    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    EditText rmb;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        rmb = (EditText)findViewById(R.id.textInput_rates);
        show = (TextView)findViewById(R.id.textOut_rates);
    }
    public void onClick(View btn){
        Log.i(TAG,"onClick: ");
        String str = rmb.getText().toString();
        float r =0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
        }
        if(btn.getId()==R.id.bntDollar_rates){
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.bntEuro_rates){
            show.setText(String.format("%.2f",r*euroRate));
        }else{
            show.setText(String.format("%.2f",r*wonRate));
        }
    }
    public void openOne(View btn){
        Intent config = new Intent(this,Config.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);
        //startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("woqu","cao");
        if (requestCode == 1 && requestCode == 2) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"dollar: "+ dollarRate);
            Log.i(TAG,"euro: "+ euroRate);
            Log.i(TAG,"won: "+ wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
