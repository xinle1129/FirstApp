package com.swufe.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Rates extends AppCompatActivity {
    private final String TAG = "Rate";
    float dollarRate = 0.1f;
    float euroRate = 0.2f;
    float wonRate = 0.3f;
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
        openConfig();
    }

    private void openConfig() {
        Intent config = new Intent(this, Config.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);
        //startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("woqu","cao");
        Log.i("woqu","requestcode"+requestCode);
        Log.i("woqu","resultcode"+resultCode);
        if (requestCode==1 && requestCode==2){
            Log.i("woqu","cao1");
            Bundle bundle = data.getExtras();
            Log.i("woqu","cao2");
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i("woqu","cao3");
            Log.i("tag","dollar: "+ dollarRate);
            Log.i(TAG,"euro: "+ euroRate);
            Log.i(TAG,"won: "+ wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
