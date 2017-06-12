package com.dropweather.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dropweather.android.gson.Weather;
import com.dropweather.android.util.HttpUtil;
import com.dropweather.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by YLX on 2017/6/3.
 */

public class AreaManagementActivity extends AppCompatActivity {

    private List<String> weatherIds = new ArrayList<String>();

    private List<String> cities = new LinkedList<String>();

    private List<String> infos = new LinkedList<String>();

    private List<String> degrees = new LinkedList<String>();

    private List<String> pm25s = new LinkedList<String>();

    private List<String> updates = new LinkedList<String>();

    private Button addButton;

    private Intent intent;

    private ListView listView;

    private String[] ids;

    private int point;

    private int finger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_management);

        requestWeathers();

        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AreaManagementActivity.this, AddAreaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AreaManagementActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Request weather information based on the array of weather_ids.
     */
    public void requestWeathers(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set mWeatherIds = new HashSet();
        mWeatherIds = prefs.getStringSet("weatherIds", new HashSet());

        Log.d("areas", mWeatherIds.toString());

        if (!mWeatherIds.isEmpty()) {
            ids = (String[]) mWeatherIds.toArray(new String[0]);
            for (int i = 0; i < ids.length; i++) {
                point = i;
                Log.d("weatherss", point+"request");
                requestWeather(ids[i]);
            }
        }
    }

    /**
     * Request weather information based on weather id.
     *
     * @param weatherId
     */
    public void requestWeather(String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
            weatherId + "&key=3e66bcc91b8f48fd9271efe27a476c69";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather mWeather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mWeather != null && "ok".equals(mWeather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AreaManagementActivity.this).edit();
                            Set weathers = new HashSet();
                            weathers.add(responseText);
                            Log.d("add", responseText);
                            editor.putStringSet("weathers", weathers);
                            editor.apply();
                            addWeatherInfo();
                        } else {
                            Toast.makeText(AreaManagementActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AreaManagementActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * Add the weather info in the arrayLists.
     */
    public void addWeatherInfo()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set weathers = new HashSet();
        weathers = prefs.getStringSet("weathers", new HashSet());
        String[] mWeathers = (String[]) weathers.toArray(new String[0]);
        Log.d("show", mWeathers.length+"id"+ids.length);
        for (int i = 0; i < mWeathers.length; i++) {
            finger = i;
            Weather weather = Utility.handleWeatherResponse(mWeathers[i]);
            Log.d("show", weather.basic.cityName+finger);
            weatherIds.add(finger, weather.basic.weatherId);
            cities.add(finger, weather.basic.cityName);
            infos.add(finger, weather.now.more.info);
            degrees.add(finger, weather.now.temperature + "℃");
            pm25s.add(finger, weather.aqi.city.pm25);
            updates.add(finger, weather.basic.update.updateTime.split(" ")[1]);
        }
        showWeatherInfo();
    }

    /**
     * Show the weather info in the listView.
     */
    public void showWeatherInfo()
    {
        Log.d("cities", cities.toString());
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < cities.size(); i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("city", cities.get(i));
            listItem.put("info", "天气:" + infos.get(i));
            listItem.put("degree", degrees.get(i));
            listItem.put("pm25", "PM2.5:" + pm25s.get(i));
            listItem.put("update", updates.get(i));
            listItems.add(listItem);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.city_item,
            new String[] {"city", "info","degree", "pm25", "update"},
            new int[] {R.id.title_city, R.id.info_text, R.id.degree_text, R.id.pm25_text, R.id.title_update_time});
        listView = (ListView) findViewById(R.id.area_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AreaManagementActivity.this);
                dialog.setTitle("删除");
                dialog.setMessage("确认删除该城市？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listItems.remove(position);
                        SimpleAdapter adapter = new SimpleAdapter(AreaManagementActivity.this, listItems, R.layout.city_item,
                            new String[] {"city", "info","degree", "pm25", "update"},
                            new int[] {R.id.title_city, R.id.info_text, R.id.degree_text, R.id.pm25_text, R.id.title_update_time});
                        listView = (ListView) findViewById(R.id.area_list);
                        listView.setAdapter(adapter);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
    }
}
