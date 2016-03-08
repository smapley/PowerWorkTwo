package com.smapley.powerwork.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.HttpUtils;
import com.smapley.powerwork.Utils.MyData;
import com.smapley.powerwork.Utils.bitmap.GetBitmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/4/17.
 */
public class TaskFragment extends Fragment {

    private View countView;
    private final int GETDATA = 1;
    private ImageView item0;
    private TextView item1;
    private TextView item2;
    private TextView item3;
    private TextView item4;
    private GetBitmap getBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        countView = inflater.inflate(R.layout.task, container, false);
        initView(countView);
        getBitmap = new GetBitmap(getActivity());
        getData();
        return countView;
    }

    private void initView(View view) {
        item0 = (ImageView) view.findViewById(R.id.task_item0);
        item1 = (TextView) view.findViewById(R.id.task_item1);
        item2 = (TextView) view.findViewById(R.id.task_item2);
        item3 = (TextView) view.findViewById(R.id.task_item3);
        item4 = (TextView) view.findViewById(R.id.task_item4);
    }

    private void upUI(Map map) {
        getBitmap.getBitmap(map.get("TmImage").toString(), item0);
        item1.setText(map.get("TeamName").toString());
        item2.setText(map.get("StartTime").toString().substring(5, 16) + "  " + map.get("EndTime").toString().substring(5, 16));
        item3.setText(map.get("TaskName").toString());
        item4.setText(map.get("Description").toString());
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = new HashMap();
                map.put("sessionid", MyData.getKey());
                map.put("taskid", MyData.TaskId);

                mhandler.obtainMessage(GETDATA, HttpUtils.updata(map, MyData.URL_GETTASK)).sendToTarget();

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
                        Map map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (map != null && !map.isEmpty()) {
                            if (map.get("flag").toString().equals(MyData.SUCC)) {
                                List<Map> list = JSON.parseObject(map.get("Task").toString(), new TypeReference<List<Map>>() {
                                });
                                if (!list.isEmpty() && list != null) {
                                    upUI(list.get(0));
                                } else {
                                    Toast.makeText(getActivity(), R.string.getfiled, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), R.string.getfiled, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.getfiled, Toast.LENGTH_SHORT).show();
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
