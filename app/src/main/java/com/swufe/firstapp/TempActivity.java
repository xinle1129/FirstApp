package com.swufe.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TempActivity extends AppCompatActivity implements View.OnClickListener{//继承该接口用于监听按钮
    TextView out;//创建全局变量
    EditText inp;//创建全局变量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        out = findViewById(R.id.textC);//获取控件textC
        inp = findViewById(R.id.textInput);//获取控件textInput
        Button btn = findViewById(R.id.convert);//获取空间convert
        btn.setOnClickListener(new View.OnClickListener(){//当点击按钮时触发
            @Override
            public void onClick(View v) {
                //Log.i("main","onClick called....");//测试用
                String str = inp.getText().toString();
                if(str.length() > 0){
                    float f = Float.parseFloat(inp.getText().toString());//获取输入的华氏度的大小
                    float c = (float) ((f-32)/1.8);//将华氏度转换为摄氏度
                    out.setText("Celsius：  "+(float)Math.round(c*10)/10+"  ℃");//将摄氏度保留1位小数，并输出
                }else{
                    Toast.makeText(TempActivity.this, "请输入温度", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @Override
    public void onClick(View v) {

    }
}
