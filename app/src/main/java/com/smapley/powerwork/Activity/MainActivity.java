package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smapley.powerwork.Fragment.MainFragment;
import com.smapley.powerwork.R;
import com.smapley.powerwork.Widge.SlidingMenu;
import com.smapley.powerwork.animation.MainDisplayNextView;


public class MainActivity extends Activity {

    private SlidingMenu mMenu;
    private ImageView Menu1, Menu2, Menu3, Menu4;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

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
        initSlidingMenu();

    }

    private void initView() {
        mainFragment = new MainFragment();

        getFragmentManager().beginTransaction().replace(R.id.fragment2, mainFragment).commit();
        MainDisplayNextView.initView(this, getFragmentManager(), mainFragment);
    }


    private void initSlidingMenu() {
        mMenu = (SlidingMenu) findViewById(R.id.id_menu);
        Menu1 = (ImageView) findViewById(R.id.menu_1);
        Menu2 = (ImageView) findViewById(R.id.menu_2);
        Menu3 = (ImageView) findViewById(R.id.menu_3);
        Menu4 = (ImageView) findViewById(R.id.menu_4);
        Menu1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, HistoricalTask.class);
                intent.putExtra("Tm_id", "");
                startActivity(intent);
                mMenu.closeMenu();
            }
        });

        Menu2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, Team.class));
                mMenu.closeMenu();
            }
        });
        Menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Publish.class));
                mMenu.closeMenu();
            }
        });
        Menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Set.class));
                mMenu.closeMenu();
            }
        });
    }
}
