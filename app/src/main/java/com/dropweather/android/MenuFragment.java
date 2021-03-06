package com.dropweather.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YLX on 2017/6/3.
 */

public class MenuFragment extends Fragment {

    private int[] icons = {R.drawable.ic_city, R.drawable.ic_switch,
        R.drawable.ic_help, R.drawable.ic_advice, R.drawable.ic_info};

    private String[] names = {"城市管理", "切换城市", "帮助", "反馈", "关于"};

    private ListView listView;

    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("icon", icons[i]);
            listItem.put("name", names[i]);
            listItems.add(listItem);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems,
            R.layout.menu_item, new String[] {"icon", "name"},
            new int[] {R.id.menu_icon, R.id.menu_name});
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), AreaManagementActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 1:
                        intent = new Intent(getActivity(), ChooseAreaActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 2:
                        intent = new Intent(getActivity(), HelpActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 3:
                        intent = new Intent(getActivity(), AdviceActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 4:
                        intent = new Intent(getActivity(), InfoActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                }
            }
        });
    }
}
