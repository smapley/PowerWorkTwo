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
 * Created by smapley on 2015/4/30.
 */
public class Register extends Activity {

    private EditText username;
    private EditText password;
    private EditText password2;
    private EditText phone;
    private EditText email;
    private EditText realname;
    private final int UPDATA = 1;
    private HashMap<String, Object> upMap;
    private CircularProgressButton register;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        sharedPreferences = getSharedPreferences(MyData.SP, MODE_PRIVATE);

        initView();


    }

    private void initView() {
        username = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_passwork);
        password2 = (EditText) findViewById(R.id.register_passwork2);
        phone = (EditText) findViewById(R.id.register_phone);
        email = (EditText) findViewById(R.id.register_email);
        realname = (EditText) findViewById(R.id.register_truename);
        register = (CircularProgressButton) findViewById(R.id.register_reg);
        register.setIndeterminateProgressMode(true);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register.getProgress() == 0) {
                    register.setProgress(50);
                    final String usernameString = username.getText().toString();
                    final String passwordString = password.getText().toString();
                    final String password2String = password2.getText().toString();
                    final String phoneString = phone.getText().toString();
                    final String emailString = email.getText().toString();
                    final String realnameString = realname.getText().toString();
                    if (!usernameString.equals("") && !passwordString.equals("")
                            && !password2String.equals("") && !phoneString.equals("")
                            && !emailString.equals("") && !realnameString.equals("")) {

                        if (password2String.equals(passwordString)) {
                            register.setProgress(50);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    upMap = new HashMap();
                                    upMap.put("username", realnameString);
                                    upMap.put("password", passwordString);
                                    upMap.put("phone", phoneString);
                                    upMap.put("email", emailString);
                                    upMap.put("realname", realnameString);
                                    mhandler.obtainMessage(UPDATA, HttpUtils.updata(upMap, MyData.URL_REGISTER)).sendToTarget();
                                }
                            }).start();

                        } else {
                            register.setProgress(-1);
                            YoYo.with(Techniques.Tada)
                                    .duration(700).playOn(findViewById(R.id.register_passwork2));
                        }
                    } else {
                        register.setProgress(-1);
                        YoYo.AnimationComposer animationComposer = YoYo.with(Techniques.Tada)
                                .duration(700);
                        if (usernameString.equals("")) {
                            animationComposer.playOn(findViewById(R.id.register_name));
                        } else if (passwordString.equals("")) {
                            animationComposer.playOn(findViewById(R.id.register_passwork));
                        } else if (password2String.equals("")) {
                            animationComposer.playOn(findViewById(R.id.register_passwork2));
                        } else if (phoneString.equals("")) {
                            animationComposer.playOn(findViewById(R.id.register_phone));
                        } else if (emailString.equals("")) {
                            animationComposer.playOn(findViewById(R.id.register_email));
                        } else if (realnameString.equals("")) {
                            animationComposer.playOn(findViewById(R.id.register_truename));
                        }

                    }

                } else if (register.getProgress() == -1) {
                    register.setProgress(0);
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
                        Map map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (map.get("flag").toString().equals(MyData.SUCC)) {
                            if (upMap != null && !upMap.isEmpty()) {
                                for (Map.Entry<String, Object> entry : upMap.entrySet()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(entry.getKey(), entry.getValue().toString());
                                    editor.commit();
                                }
                            }
                            register.setProgress(100);
                            finish();
                        } else {
                            register.setProgress(-1);
                        }
                        break;
                }

            } catch (Exception e) {
                register.setProgress(-1);
            }
        }
    };
}
