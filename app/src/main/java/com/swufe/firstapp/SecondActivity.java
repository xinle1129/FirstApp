package com.swufe.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    final String TAG = "second";
    TextView score;
    TextView score2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        score = (TextView)findViewById(R.id.score1_second);
        score2 = (TextView)findViewById(R.id.score2_second);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {//保存数据0000
        super.onSaveInstanceState(outState);
        String scorea = (String) score.getText();
        String scoreb = (String) score2.getText();

        Log.i(TAG,"onSaveInstanceState:");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {//获取数据0000
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");

        Log.i(TAG,"onRestoreInstanceState:");
        score.setText(scorea);
        score2.setText(scoreb);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart:");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause:");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume:");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop:");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart:");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy:");
    }

    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn1_1_second){
            showScore1(1);
        }else{
            showScore2(1);
        }
    }
    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn2_1_second){
            showScore1(2);
        }else{
            showScore2(2);
        }
    }
    public void btnAdd3(View btn) {
        if(btn.getId()==R.id.btn3_1_second){
            showScore1(3);
        }else{
            showScore2(3);
        }
    }
    public void btnReset(View btn){
        score. setText("0");
        score2.setText("0");
    }

    private void showScore1(int inc){
        Log.i("show", "inc=" + inc);
        String oldScore = (String) score.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        score.setText("" + newScore);
    }
    private void showScore2(int inc){
        Log.i("show", "inc=" + inc);
        String oldScore = (String) score2.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        score2.setText("" + newScore);
    }

    }
