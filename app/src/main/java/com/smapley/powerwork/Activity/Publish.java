package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableRow;

import com.dd.CircularProgressButton;
import com.smapley.powerwork.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by smapley on 2015/5/7.
 */
public class Publish extends Activity implements View.OnClickListener {

    private TableRow item1;
    private TableRow item2;
    private TableRow item3;
    private EditText content;
    private CircularProgressButton publish;
    private final int UPDATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.publish);
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
        initView();

    }

    private void initView() {
        item1 = (TableRow) findViewById(R.id.publish_item1);
        item2 = (TableRow) findViewById(R.id.publish_item2);
        item3 = (TableRow) findViewById(R.id.publish_item3);
        content = (EditText) findViewById(R.id.publish_content);
        publish = (CircularProgressButton) findViewById(R.id.publish_publish);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        publish.setOnClickListener(this);
        publish.setIndeterminateProgressMode(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_item1:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.tips))
                        .setContentText(getString(R.string.publish_item8))
                        .show();
                break;
            case R.id.publish_item2:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.tips))
                        .setContentText(getString(R.string.publish_item9))
                        .show();
                break;
            case R.id.publish_item3:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.tips))
                        .setContentText(getString(R.string.publish_item7))
                        .show();
                break;
            case R.id.publish_publish:
                if (publish.getProgress() == 100) {
                    publish.setProgress(0);
                } else {
                    publish.setProgress(50);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {

                            }
                            mhandler.obtainMessage(UPDATA).sendToTarget();
                        }
                    }).start();
                }
                break;
        }
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {

                switch (msg.what) {
                    case UPDATA:
                        publish.setProgress(100);
                        break;
                }

            } catch (Exception e) {

            }
        }
    };
}
