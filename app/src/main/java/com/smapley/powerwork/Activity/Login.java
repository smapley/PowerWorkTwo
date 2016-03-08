package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by Smapley on 2015/4/12.
 */
public class Login extends Activity {

    private EditText name;
    private EditText password;
    private TextView register;
    private CircularProgressButton login;
    private TextView forget;
    private SharedPreferences sharedPreferences;
    private HashMap<String, Object> upMap;
    private final int UPDATA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        sharedPreferences = getSharedPreferences(MyData.SP, MODE_PRIVATE);

        initView();

    }

    private void initView() {




        name = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_passwork);
        register = (TextView) findViewById(R.id.login_reg);
        login = (CircularProgressButton) findViewById(R.id.login_login);
        forget = (TextView) findViewById(R.id.login_forget);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Forget.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        login.setIndeterminateProgressMode(true);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login.getProgress() == 0) {
                    login.setProgress(50);
                    final String nameString = name.getText().toString();
                    final String passString = password.getText().toString();
                    if (!nameString.equals("")) {
                        if (!passString.equals("")) {
                            login.setProgress(50);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    upMap = new HashMap();
                                    upMap.put("username", nameString);
                                    upMap.put("password", passString);
                                    mhandler.obtainMessage(UPDATA, HttpUtils.updata(upMap, MyData.URL_LOGIN)).sendToTarget();
                                }
                            }).start();

                        } else {
                            login.setProgress(-1);
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(findViewById(R.id.login_passwork));
                        }
                    } else {
                        login.setProgress(-1);
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.login_name));

                    }
                } else if (login.getProgress() == -1) {
                    login.setProgress(0);
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
                            if (map != null && !map.isEmpty()) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(entry.getKey(), entry.getValue().toString());
                                    editor.commit();
                                }
                            }
                            login.setProgress(100);
                            finish();
                        } else {
                            login.setProgress(-1);
                        }

                        break;
                }
            } catch (Exception e) {
                login.setProgress(-1);
            }
        }
    };
}
