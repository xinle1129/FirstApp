package com.swufe.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {
    EditText inp;
    TextView out;
    String kind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        inp = findViewById(R.id.textInput_rate);
        out = findViewById(R.id.textOut_rate);
        Log.i("main","the2");
        //bntKind.setText("good");
        Intent intent = getIntent();
        if(intent.getStringExtra("kind")!= null){
            kind = intent.getStringExtra("kind");
            Button bntKind = findViewById(R.id.bntKind_rate);
            bntKind.setText(kind);
        }
    }

    public void onClickKind(View btn){
        kind = ((Button)btn).getText().toString();
        if(kind.equalsIgnoreCase("dollar")){
            out.setText("Dollar!Succeed!");
        } else if(kind.equalsIgnoreCase("yen")){
            out.setText("Yen!Succeed!");
        }
    }
    public void onClickChange(View btn){
        Intent rate = new Intent(this,Rate2Activity.class);
        startActivity(rate);
        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        Log.i("main","the1");
        //Button bntKind = findViewById(R.id.bntKind_rate);
        //bntKind.setText(kind);
    }
}
