package com.dropweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdviceActivity extends AppCompatActivity {

    String advice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        final EditText editText = (EditText) findViewById(R.id.advice_text);


        Button adviceButton = (Button) findViewById(R.id.advice_button);
        adviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advice = editText.getText().toString();
                if (advice.equals("")) {
                    Toast.makeText(AdviceActivity.this, "反馈内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdviceActivity.this, "感谢您的反馈，我们会尽快处理！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdviceActivity.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdviceActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
