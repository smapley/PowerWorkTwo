package com.smapley.powerwork.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.smapley.powerwork.Adapter.RefreshAdapter;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.HttpUtils;
import com.smapley.powerwork.Utils.MyData;
import com.yalantis.taurus.PullToRefreshView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smapley on 2015/5/1.
 */
public class MainFragment extends Fragment {

    private View containerView;
    public static final int REFRESH_DELAY = 4000;
    private PullToRefreshView mPullToRefreshView;
    private View contentView;
    public static int firstVisiblePosition = 0;
    public static int top = 0;
    public static int itemHeight = 0;
    private ListView listView;
    private RefreshAdapter adapter = null;
    private final int GETDATA = 1;
    private SharedPreferences usersp;
    private SharedPreferences mainlistsp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.main_fragment, container, false);
        usersp = getActivity().getSharedPreferences(MyData.SP, Context.MODE_PRIVATE);
        mainlistsp = getActivity().getSharedPreferences(MyData.SP_MAINLIST, Context.MODE_PRIVATE);
        initView(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }


    public void UpList(List sampleList) {

        if (sampleList != null && !sampleList.isEmpty()) {
            if (adapter == null) {
                adapter = new RefreshAdapter(getActivity(), sampleList);
                listView.setAdapter(adapter);


            } else {
                adapter.listitem = sampleList;
                adapter.notifyDataSetChanged();
                listView.setSelectionFromTop(firstVisiblePosition, top);
            }
        }
    }


    private void initView(View courentView) {
        listView = (ListView) courentView.findViewById(R.id.list_view);
        String mainString = mainlistsp.getString("data", "").toString();
        if (!mainString.equals("")) {
            List list = JSON.parseObject(mainString, new TypeReference<List>() {
            });
            UpList(list);
        }


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            /**
             * 滚动状态改变时调用
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    Log.i("asdfasdf", "--------------------->>>>>>>>" + getScrollY());
                }
            }

            /**
             * 滚动时调用
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mPullToRefreshView = (PullToRefreshView) courentView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        firstVisiblePosition = listView.getFirstVisiblePosition();
        top = c.getTop();
        itemHeight = c.getHeight();
        return -top + firstVisiblePosition * itemHeight;
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = new HashMap();
                map.put("sessionid", MyData.getKey());
                map.put("username", usersp.getString("username", ""));
                map.put("order", MyData.ORDER_FREETIME);
                mhandler.obtainMessage(GETDATA, HttpUtils.updata(map, MyData.URL_GETTASKLIST)).sendToTarget();
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
                        mPullToRefreshView.setRefreshing(false);
                        Map map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (map.get("flag").toString().equals(MyData.SUCC)) {
                            List list = JSON.parseObject(map.get("Task").toString(), new TypeReference<List>() {
                            });

                            if (list != null && !list.isEmpty()) {

                                SharedPreferences.Editor editor = mainlistsp.edit();
                                editor.putString("data", JSON.toJSONString(list));
                                editor.commit();
                                UpList(list);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                try {
                    Toast.makeText(getActivity(), R.string.connectfiled, Toast.LENGTH_SHORT).show();
                } catch (Exception e1) {

                }
            }
        }
    };
}



