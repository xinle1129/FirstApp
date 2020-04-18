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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rates extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    float dollarRate = 0.1f;
    float euroRate = 0.2f;
    float wonRate = 0.3f;
    EditText rmb;
    TextView show;
    Handler handler;
    String date;
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
        date = sharedPreferences.getString("date","");


        //开启子线程
        Thread thread = new Thread(this);//要有this，才能找到run()
        thread.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {//这样就不用单独创建一个类
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                //获取当前时间日期
                String date0 = formatter.format(new Date(System.currentTimeMillis()));
                Log.i(TAG,"date:"+date);
                if(msg.what==5&&!date0.equals(date)){
                    //String str = (String) msg.obj;
                    Bundle bd1 = (Bundle)msg.obj;
                    //Log.i(TAG,"handleMessage : getMessage msg = "+ str);
                    //show.setText(str);
                    dollarRate = bd1.getFloat("dollar-rate");
                    euroRate = bd1.getFloat("euro-rate");
                    wonRate = bd1.getFloat("won-rate");
                    Log.i(TAG,"handleMessage : dollarRate = "+ dollarRate );
                    Log.i(TAG,"handleMessage : euroRate = "+ euroRate );
                    Log.i(TAG,"handleMessage : wonRate = "+ wonRate );

                    //更新date,保存date和汇率
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("date",date0);
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.commit();//保存，commit要等待，apply不用

                    Toast.makeText(Rates.this,"汇率已更新",Toast.LENGTH_SHORT).show();
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
        //用于保存获取的汇率
        Bundle bundle = new Bundle();
        //获取网络数据
//        URL url = null;
//        try {
//            url = new URL("https://www.usd-cny.com/bankofchina.htm");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//
//            String html = inputStream2String(in);
//            Log.i(TAG,"run : html=" + html);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);可以这样获取doc
            Log.i(TAG,"run :" +  doc.title());//能获取title
            Elements tables = doc.getElementsByTag("table");
//            int i = 1;
//            for(Element table : tables){
//                Log.i(TAG,"run: table["+i+"]=" + table);
//                i++;
//            }
            Element table1 = tables.get(0);
            Log.i(TAG,"table1="+table1);
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<tds .size();i+=6){
                Element td1 = tds .get(i);
                Element td2 = tds .get(i+5);
                Log.i(TAG,"run: " + td1. text() + "==>" + td2. text());//text()和html()都可以,因为这里td之间只有文本
                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //bundle中保存所获得的汇率
        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //  msg.what = 5;//用于标记当前msg
        //msg.obj = "Hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);
    }
    //
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
