package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mingle.widget.LoadingView;
import com.smapley.powerwork.Adapter.HistoricalAdapter;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.HttpUtils;
import com.smapley.powerwork.Utils.MyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smapley on 2015/5/5.
 */
public class HistoricalTask extends Activity {

    private final int GETDATA = 1;
    private SharedPreferences usersp;
    private ListView listView;
    private LinearLayout item1;
    private LinearLayout item2;
    private ImageView tag1;
    private ImageView tag2;
    private LoadingView loadingView;
    private TextView onemoretime;
    private HistoricalAdapter historicalAdapter;
    private List<Map> list1 = new ArrayList<>();
    private List<Map> list2 = new ArrayList<>();
    private String Tm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.historicaltask);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        usersp = getSharedPreferences(MyData.SP, MODE_PRIVATE);
        Tm_id = getIntent().getStringExtra("Tm_id");
        initView();
        getData();
    }

    private void initView() {

        item1 = (LinearLayout) findViewById(R.id.his_item1);
        item2 = (LinearLayout) findViewById(R.id.his_item2);
        listView = (ListView) findViewById(R.id.his_list);
        listView.setDivider(null);
        loadingView = (LoadingView) findViewById(R.id.loadView);
        onemoretime = (TextView) findViewById(R.id.onemoretime);
        onemoretime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        tag1 = (ImageView) findViewById(R.id.his_tag1);
        tag2 = (ImageView) findViewById(R.id.his_tag2);
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.GONE);
                MyData.YEAR = "";
                MyData.MONTH = "";
                MyData.DAY = "";
                historicalAdapter.listitem.clear();
                historicalAdapter.listitem.addAll(list1);
                Log.i("his", list1.toString());
                Log.i("his", list2.toString());
                Log.i("his", historicalAdapter.listitem.toString());
                historicalAdapter.notifyDataSetChanged();
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag1.setVisibility(View.GONE);
                tag2.setVisibility(View.VISIBLE);
                MyData.YEAR = "";
                MyData.MONTH = "";
                MyData.DAY = "";
                historicalAdapter.listitem.clear();
                historicalAdapter.listitem.addAll(list2);
                Log.i("his", list1.toString());
                Log.i("his", list2.toString());
                Log.i("his", historicalAdapter.listitem.toString());
                historicalAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getData() {
        loadingView.setVisibility(View.VISIBLE);
        onemoretime.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = new HashMap();
                map.put("sessionid", MyData.getKey());
                map.put("username", usersp.getString("username", ""));
                map.put("order", MyData.ORDER_ENDTIME);
                if (Tm_id.equals("")) {
                    mhandler.obtainMessage(GETDATA, HttpUtils.updata(map, MyData.URL_GETHISTASK)).sendToTarget();
                } else {
                    map.put("teamid", Tm_id);
                    mhandler.obtainMessage(GETDATA, HttpUtils.updata(map, MyData.URL_GETTEAMTASK)).sendToTarget();

                }
            }
        }).start();
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case GETDATA:
                        loadingView.setVisibility(View.GONE);
                        Map map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (map.get("flag").toString().equals(MyData.SUCC)) {
                            List<Map> list = JSON.parseObject(map.get("Task").toString(), new TypeReference<List>() {
                            });
                            if (list != null && !list.isEmpty()) {
                                MyData.YEAR = "";
                                MyData.MONTH = "";
                                MyData.DAY = "";
                                for (int i = 0; i < list.size(); i++) {
                                    if (Integer.parseInt(list.get(i).get("FreeTime").toString()) > 0) {
                                        list1.add(list.get(i));
                                    } else {
                                        list2.add(list.get(i));
                                    }
                                }
                                Log.i("his", list1.toString());
                                Log.i("his", list2.toString());
                                List<Map> list3 = new ArrayList<>();
                                list3.addAll(list1);
                                historicalAdapter = new HistoricalAdapter(HistoricalTask.this, list3);
                                listView.setAdapter(historicalAdapter);
                            }
                        } else {
                            onemoretime.setVisibility(View.VISIBLE);
                        }

                        break;
                }

            } catch (Exception e) {
                loadingView.setVisibility(View.GONE);
                onemoretime.setVisibility(View.VISIBLE);
                Toast.makeText(HistoricalTask.this, R.string.connectfiled, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
