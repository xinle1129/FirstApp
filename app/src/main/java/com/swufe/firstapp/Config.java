package com.swufe.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Config extends AppCompatActivity {
    EditText dollarText;
    EditText euroText;
    EditText wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        dollarText = (EditText)findViewById(R.id.textDollar_config);
        euroText = (EditText)findViewById(R.id.textEuro_config);
        wonText = (EditText)findViewById(R.id.textWon_config);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

    }
    public void save(View btn){
        Log.i("最上面","1");
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());
        Log.i("最上面","2");
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putFloat("key_dollar",newDollar);
        bundle.putFloat("key_euro",newEuro);
        bundle.putFloat("key_won",newWon);
        intent.putExtras(bundle);
        setResult(2,intent);
        Log.i("最上面","3");
        Log.i("最上面","1"+newDollar);
        Log.i("最上面","2"+newEuro);
        Log.i("最上面","3"+newWon);
        finish();
    }

}
