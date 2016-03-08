package com.smapley.powerwork.Euclid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smapley.powerwork.R;
import com.smapley.powerwork.Utils.bitmap.GetBitmap;

import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> {

    public static final String KEY_PIC = "TmImage";
    public static final String KEY_NAME = "Name";
    public static final String KEY_DESCRIPTION_SHORT = "Intro";
    public static final String KEY_DESCRIPTION_FULL = "Intro_full";

    private final LayoutInflater mInflater;
    public List<Map<String, Object>> mData;
    private GetBitmap getBitmap;
    private Context context;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
        getBitmap = new GetBitmap(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mViewOverlay = convertView.findViewById(R.id.view_avatar_overlay);
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.image_view_avatar);
            viewHolder.mListItemName = (TextView) convertView.findViewById(R.id.text_view_name);
            viewHolder.mListItemDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Picasso.with(getContext()).load((Integer) mData.get(position).get(KEY_AVATAR))
//                .resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
//                .placeholder(R.color.blue)
//                .into(viewHolder.mListItemAvatar);
//

        getBitmap.getBitmap(mData.get(position).get(KEY_PIC).toString(), viewHolder.mListItemAvatar);

        viewHolder.mListItemName.setText(mData.get(position).get(KEY_NAME).toString().toUpperCase());
        viewHolder.mListItemDescription.setText((String) mData.get(position).get(KEY_DESCRIPTION_SHORT));
        viewHolder.mViewOverlay.setBackground(EuclidActivity.sOverlayShape);
//        viewHolder.mViewOverlay.setBackgroundResource(R.drawable.main_backgound);
        return convertView;
    }

    static class ViewHolder {
        View mViewOverlay;
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;
    }
}
