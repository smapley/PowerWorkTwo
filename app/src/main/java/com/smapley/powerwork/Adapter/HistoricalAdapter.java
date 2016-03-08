package com.smapley.powerwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.MyData;

import java.util.List;
import java.util.Map;

/**
 * Created by Smapley on 2015/4/11.
 */
public class HistoricalAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    public List<Map> listitem;

    public HistoricalAdapter(Context context,
                             List listitem) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.listitem = listitem;

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
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_his_item, null);
            holder = new ViewHolder();
            holder.year = (TextView) convertView.findViewById(R.id.his_item_year);
            holder.month = (TextView) convertView.findViewById(R.id.his_item_month);
            holder.lin1 = (TextView) convertView.findViewById(R.id.his_item_lin1);
            holder.lin2 = (TextView) convertView.findViewById(R.id.his_item_lin2);
            holder.content = (TextView) convertView.findViewById(R.id.his_item_content);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象

        }

        String endtime = listitem.get(position).get("EndTime").toString();
        String endyear = endtime.substring(0, 4);
        String endmonth = endtime.substring(5, 7);
        String endday = endtime.substring(8, 10);
        holder.content.setText(endmonth + "/" + endday + "  " + listitem.get(position).get("TaskName").toString());

        if (MyData.YEAR.equals("")) {
            holder.year.setText(endyear);
            MyData.YEAR = endyear;
            holder.year.setVisibility(View.VISIBLE);
        } else {
            if (Integer.parseInt(MyData.YEAR) > Integer.parseInt(endyear)) {
                holder.year.setText(endyear);
                holder.year.setVisibility(View.VISIBLE);
                MyData.YEAR = endyear;
                MyData.MONTH = endmonth;
                MyData.DAY = endday;
            }
        }
        if (MyData.MONTH.equals("")) {
            holder.month.setText(endmonth);
            MyData.MONTH = endmonth;
            holder.month.setVisibility(View.VISIBLE);
        } else {
            if (Integer.parseInt(MyData.MONTH) > Integer.parseInt(endmonth)) {
                holder.month.setText(endmonth);
                holder.month.setVisibility(View.VISIBLE);
                MyData.MONTH = endmonth;
                MyData.DAY = endday;
            }
        }
        if (MyData.DAY.equals("")) {
            MyData.DAY = endday;
        } else {
            int day = Integer.parseInt(endday) - Integer.parseInt(MyData.DAY);
            int height = 20 + day * 10;
            holder.lin2.setHeight(height);
        }
        return convertView;
    }


    class ViewHolder {
        TextView year;
        TextView month;
        TextView lin1;
        TextView lin2;
        TextView content;
    }


}
