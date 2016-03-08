package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dd.CircularProgressButton;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.HttpUtils;
import com.smapley.powerwork.Utils.MyData;
import com.smapley.powerwork.animation.Techniques;
import com.smapley.powerwork.animation.YoYo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by smapley on 2015/5/1.
 */
public class Forget extends Activity {

    private final int UPDATA = 1;
    private SharedPreferences sharedPreferences;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText phone;
    private CircularProgressButton forget;
    private HashMap<String, Object> upMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forget);
        sharedPreferences = getSharedPreferences(MyData.SP, MODE_PRIVATE);
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.forget_name);
        password = (EditText) findViewById(R.id.forget_passwork);
        email = (EditText) findViewById(R.id.forget_email);
        phone = (EditText) findViewById(R.id.forget_phone);
        forget = (CircularProgressButton) findViewById(R.id.forget_forget);
        forget.setIndeterminateProgressMode(true);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forget.getProgress() == 0) {
                    forget.setProgress(50);
                    final String nameString = username.getText().toString();
                    final String passString = password.getText().toString();
                    final String emailString = email.getText().toString();
                    final String phoneString = phone.getText().toString();
                    if (!nameString.equals("")) {
                        if (!passString.equals("")) {
                            if (!phoneString.equals("")) {
                                if (!emailString.equals("")) {
                                    forget.setProgress(50);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            upMap = new HashMap();
                                            upMap.put("username", nameString);
                                            upMap.put("password", passString);
                                            upMap.put("email", emailString);
                                            upMap.put("phone", phoneString);
                                            mhandler.obtainMessage(UPDATA, HttpUtils.updata(upMap, MyData.URL_FORGET)).sendToTarget();
                                        }
                                    }).start();
                                } else {
                                    forget.setProgress(-1);
                                    YoYo.with(Techniques.Tada)
                                            .duration(700)
                                            .playOn(findViewById(R.id.forget_email));
                                }
                            } else {
                                forget.setProgress(-1);
                                YoYo.with(Techniques.Tada)
                                        .duration(700)
                                        .playOn(findViewById(R.id.forget_phone));
                            }
                        } else {
                            forget.setProgress(-1);
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(findViewById(R.id.forget_passwork));
                        }
                    } else {
                        forget.setProgress(-1);
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.forget_name));

                    }
                } else if (forget.getProgress() == -1) {
                    forget.setProgress(0);
                }
            }
        });

    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case UPDATA:
                        Map<String, Object> map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (map.get("flag").toString().equals(MyData.SUCC)) {
                            if (upMap != null && !upMap.isEmpty()) {
                                for (Map.Entry<String, Object> entry : upMap.entrySet()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(entry.getKey(), entry.getValue().toString());
                                    editor.commit();
                                }
                            }

                            forget.setProgress(100);
                            finish();
                        } else {
                            forget.setProgress(-1);
                        }

                        break;
                }
            } catch (Exception e) {
                forget.setProgress(-1);
            }
        }
    };

}
