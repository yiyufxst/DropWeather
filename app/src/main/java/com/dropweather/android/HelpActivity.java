package com.dropweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private String text = "1.进入Drop天气后，选择需要天气信息的城市\n" +
            "2.天气页面的标题右边按钮可以切换城市\n" +
            "3.天气页面下拉可以刷新天气信息\n" +
            "4.天气页面左边按钮可弹出侧滑菜单，选择相关内容\n" +
            "5.天气页面侧滑菜单，选择城市管理，可以添加城市\n" +
            "6.天气页面侧滑菜单，选择切换城市，可切换天气页面显示的城市\n" +
            "7.天气页面侧滑菜单，选择帮助，可显示Drop天气应用的使用帮助\n" +
            "8.天气页面侧滑菜单，选择反馈，可提交对Drop天气应用使用情况的反馈\n" +
            "9.天气页面侧滑菜单，选择关于，可显示Drop天气应用版本信息\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView textView = (TextView) findViewById(R.id.help_text);
        textView.setText(text);

        Button backButton = (Button) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
