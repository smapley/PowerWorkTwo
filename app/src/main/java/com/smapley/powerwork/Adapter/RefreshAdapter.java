package com.smapley.powerwork.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.MyData;
import com.smapley.powerwork.Widge.CoolSwitch;
import com.smapley.powerwork.animation.MainDisplayNextView;
import com.smapley.powerwork.animation.Rotate3dAnimation;

import java.util.List;
import java.util.Map;

/**
 * Created by Smapley on 2015/4/11.
 */
public class RefreshAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    public List<Map> listitem;
    private MainDisplayNextView displayNextView;
    private int[] pic = {R.drawable.main_table1,
            R.drawable.main_table2,
            R.drawable.main_table7,
            R.drawable.main_table4,
            R.drawable.main_table5,
            R.drawable.main_table6,
            R.drawable.main_table3};
    private int[] color = {R.color.main_item1,
            R.color.main_item2,
            R.color.main_item7,
            R.color.main_item4,
            R.color.main_item5,
            R.color.main_item6,
            R.color.text1};

    public RefreshAdapter(Context context,
                          List listitem) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.listitem = listitem;
        displayNextView = new MainDisplayNextView();

    }

    @Override
    public int getCount() {
        return listitem.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.layout_main_item, parent, false);
        holder.coolSwitch = (CoolSwitch) convertView.findViewById(R.id.cool_switch_foo);
        holder.image = (ImageView) convertView.findViewById(R.id.main_item_iamge);
        holder.free = (TextView) convertView.findViewById(R.id.main_item_free);
        holder.title = (TextView) convertView.findViewById(R.id.main_item_title);
        holder.endtime = (TextView) convertView.findViewById(R.id.main_item_endtime);
        convertView.setTag(holder);

        int freetime = Integer.parseInt(listitem.get(position).get("FreeTime").toString());
        String endtime = listitem.get(position).get("EndTime").toString();
        String end = endtime.substring(11, 12);
        int time = Math.round((freetime - Integer.parseInt(end) + 12) / 24);
        final int type = Integer.parseInt(listitem.get(position).get("TaskType").toString());

        holder.free.setText(time + "");
        holder.title.setText(listitem.get(position).get("TaskName").toString());
        holder.endtime.setText(endtime.substring(5, 16));

        holder.image.setImageResource(pic[type]);
        holder.coolSwitch.setColor(context.getResources().getColor(color[type]));
        holder.free.setTextColor(context.getResources().getColor(color[type]));
        holder.coolSwitch.addAnimationListener(new CoolSwitch.AnimationListener() {

            @Override
            public void onCheckedAnimationStar() {

            }

            @Override
            public void onCheckedAnimationFinished() {
                holder.image.setImageResource(pic[6]);
                holder.coolSwitch.setColor(context.getResources().getColor(color[6]));
                holder.free.setTextColor(context.getResources().getColor(color[6]));
            }

            @Override
            public void onUncheckedAnimationFinished() {
                holder.image.setImageResource(pic[type]);
                holder.coolSwitch.setColor(context.getResources().getColor(color[type]));
                holder.free.setTextColor(context.getResources().getColor(color[type]));
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyRotation(view, 1, 0, 90);//点击后先旋转90度
                MyData.TaskId = listitem.get(position).get("TaskId").toString();
            }
        });
        return convertView;
    }


    class ViewHolder {
        ImageView image;
        CoolSwitch coolSwitch;
        TextView free;
        TextView endtime;
        TextView title;
    }

    private void applyRotation(View view, int position, float start, float end) {
        Log.i("_________________", "--------------------->>开始动画");
        // 计算中心点
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setInterpolator(new AccelerateInterpolator());
        //设置监听
        rotation.setAnimationListener(displayNextView);
        view.startAnimation(rotation);
    }

}
