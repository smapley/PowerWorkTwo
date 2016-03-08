package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.MyData;
import com.smapley.powerwork.Utils.bitmap.GetPic_inSampleSize;
import com.smapley.powerwork.Utils.bitmap.ImageFileCache;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by smapley on 2015/5/2.
 */
public class Set extends Activity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;


    private TableRow tableRow1;
    private TableRow tableRow2;
    private TableRow tableRow3;
    private TableRow tableRow4;
    private TableRow tableRow5;
    private TextView name;
    private ImageView userpic;
    private CircularProgressButton logout;
    private Uri imageUri = Uri.fromFile(new File(ImageFileCache.getDirectory(),
            "user_pic.jpg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set);
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


        sharedPreferences = getSharedPreferences(MyData.SP, MODE_PRIVATE);

        initView();
    }


    private void initView() {

        tableRow1 = (TableRow) findViewById(R.id.set_table1);
        tableRow2 = (TableRow) findViewById(R.id.set_table2);
        tableRow3 = (TableRow) findViewById(R.id.set_table3);
        tableRow4 = (TableRow) findViewById(R.id.set_table4);
        tableRow5 = (TableRow) findViewById(R.id.set_table5);
        logout = (CircularProgressButton) findViewById(R.id.set_logout);
        if(sharedPreferences.getString("username","").equals("") ){
            logout.setProgress(-1);
        }
        userpic = (ImageView) findViewById(R.id.set_userpic);
        try {
            Bitmap newBitmap = GetPic_inSampleSize.decodeSampledBitmapFromUrl(
                    ImageFileCache.getDirectory() + "user_pic.jpg", 500, 500);
            // 将处理过的图片显示在界面上，并保存到本地
            userpic.setImageBitmap(newBitmap);
        } catch (Exception e) {

        }
        name = (TextView) findViewById(R.id.set_name);
        name.setText(sharedPreferences.getString("name", ""));

        tableRow1.setOnClickListener(this);
        tableRow2.setOnClickListener(this);
        tableRow3.setOnClickListener(this);
        tableRow4.setOnClickListener(this);
        tableRow5.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.set_table1:
                new SweetAlertDialog(this)
                        .setTitleText(getString(R.string.set_item9))
                        .setCancelText(getString(R.string.dialog_cancel))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .showEditext(true)
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText(getString(R.string.set_item10))
                                        .setContentText(sDialog.getEditext())
                                        .setConfirmText(getString(R.string.dialog_ok))
                                        .showCancelButton(false)
                                        .showEditext(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                name.setText(sDialog.getEditext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", sDialog.getEditext());
                                editor.commit();
                            }
                        })
                        .show();
                break;
            case R.id.set_table2:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                intent.putExtra("scale", true);
                intent.putExtra("crop", "true");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 0);
                break;
            case R.id.set_table3:
                break;
            case R.id.set_table4:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.set_item11))
                        .setContentText(getString(R.string.set_item12))
                        .setCancelText(getString(R.string.dialog_cancel))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText(getString(R.string.dialog_cancel))
                                        .setContentText("")
                                        .setConfirmText(getString(R.string.dialog_ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("")
                                        .setContentText(getString(R.string.set_item13))
                                        .setConfirmText(getString(R.string.dialog_ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            }
                        })
                        .show();
                break;
            case R.id.set_table5:
                break;
            case R.id.set_logout:
                if (logout.getProgress() == -1) {
                    startActivity(new Intent(Set.this, Login.class));
                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.set_item14))
                            .setCancelText(getString(R.string.dialog_cancel))
                            .setConfirmText(getString(R.string.dialog_ok))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText(getString(R.string.set_item15))
                                            .setContentText("")
                                            .setConfirmText(getString(R.string.dialog_ok))
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    logout.setProgress(-1);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                }
                            })
                            .show();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bitmap newBitmap = GetPic_inSampleSize.decodeSampledBitmapFromUrl(
                    ImageFileCache.getDirectory() + "user_pic.jpg", 500, 500);
            // 将处理过的图片显示在界面上，并保存到本地
            userpic.setImageBitmap(newBitmap);
        }
    }
}
