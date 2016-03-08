package com.smapley.powerwork.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mingle.widget.LoadingView;
import com.smapley.powerwork.Euclid.EuclidActivity;
import com.smapley.powerwork.Euclid.EuclidListAdapter;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.HttpUtils;
import com.smapley.powerwork.Utils.MyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Smapley on 2015/4/9.
 */
public class Team extends EuclidActivity {

    private final int GETDATA = 1;
    private SharedPreferences usersp;
    private EuclidListAdapter euclidListAdapter;
    private List<Map<String, Object>> profilesList;
    private LoadingView loadingView;
    private TextView onemore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        usersp = getSharedPreferences(MyData.SP, MODE_PRIVATE);

        initView();

        getData();

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Team.this, HistoricalTask.class);
                intent.putExtra("Tm_id", profilesList.get(MyData.TEAMID).get("Tm_id").toString());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        loadingView = (LoadingView) findViewById(R.id.loadView);
        onemore = (TextView) findViewById(R.id.onemoretime);
        onemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

    }

    private void getData() {
        onemore.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = new HashMap();
                map.put("sessionid", MyData.getKey());
                map.put("username", usersp.getString("username", ""));
                mhandler.obtainMessage(GETDATA, HttpUtils.updata(map, MyData.URL_GETTEAMLIST)).sendToTarget();
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
                            List list = JSON.parseObject(map.get("Team").toString(), new TypeReference<List>() {
                            });

                            profilesList.addAll(list);
                            euclidListAdapter.mData = profilesList;
                            euclidListAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(Team.this, R.string.getfiled, Toast.LENGTH_SHORT).show();
                            onemore.setVisibility(View.VISIBLE);

                        }
                        break;
                }

            } catch (Exception e) {
                Toast.makeText(Team.this, R.string.connectfiled, Toast.LENGTH_SHORT).show();
                onemore.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected BaseAdapter getAdapter() {

        profilesList = new ArrayList<>();

        euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item, profilesList);
        return euclidListAdapter;

    }

}
