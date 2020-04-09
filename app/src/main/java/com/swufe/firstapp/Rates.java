package com.swufe.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Rates extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    float dollarRate = 0.1f;
    float euroRate = 0.2f;
    float wonRate = 0.3f;
    EditText rmb;
    TextView show;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        rmb = (EditText)findViewById(R.id.textInput_rates);
        show = (TextView)findViewById(R.id.textOut_rates);
        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);//这样也可以，但是不能改名，只能有一个
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);

        //开启子线程
        Thread thread = new Thread(this);//要有this，才能找到run()
        thread.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {//这样就不用单独创建一个类
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i(TAG,"handleMessage : getMessage msg = "+ str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };
    }
    public void onClick(View btn){
        Log.i(TAG,"onClick: ");
        String str = rmb.getText().toString();
        float r =0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
            //return;
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
        Log.i("woqu","requestcode="+requestCode);
        Log.i("woqu","resultcode="+resultCode);
        if (requestCode==1&&resultCode==2){
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
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();//保存，commit要等待，apply不用
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i("Run","running……");
        for(int i=1;i<3;i++){
            Log.i(TAG,"run: i= "+ i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
            //  msg.what = 5;//用于标记当前msg
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);
        //获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,"run : html=" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        int charsRead;
        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }
}
