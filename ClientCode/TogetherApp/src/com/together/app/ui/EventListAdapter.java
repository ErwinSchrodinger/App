package com.together.app.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.together.app.R;
import com.together.app.data.DataEngine;
import com.together.app.data.Event;

public class EventListAdapter extends BaseAdapter {

    private Event[] mEvents;
    private Context mContext;

    public EventListAdapter(Context context) {
        mContext = context;
        mEvents = DataEngine.getInstance().getEventList();
    }

    private class ListItemContainer {
        UrlImageView img;
        TextView title;
        TextView author;
        TextView address;
        TextView time;
    }

    @Override
    public int getCount() {
        return mEvents.length;
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
    public void notifyDataSetChanged() {
        mEvents = DataEngine.getInstance().getEventList();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemContainer container = null;
        Resources res = mContext.getResources();
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.view_event_list_cell, null);
            container = new ListItemContainer();
            container.img = (UrlImageView) convertView
                    .findViewById(R.id.event_img);
            int imgSize = res.getDimensionPixelSize(R.dimen.event_img_size);
            container.img.setImageWidth(imgSize);
            container.img.setImageHeight(imgSize);
            container.img.setDefaultBitmap(BitmapFactory.decodeResource(res,
                    R.drawable.ic_launcher));
            container.title = (TextView) convertView
                    .findViewById(R.id.event_title);
            container.author = (TextView) convertView
                    .findViewById(R.id.event_author);
            container.address = (TextView) convertView
                    .findViewById(R.id.event_address);
            container.time = (TextView) convertView
                    .findViewById(R.id.event_time);
            convertView.setTag(container);
        } else {
            container = (ListItemContainer) convertView.getTag();
        }

        Event event = mEvents[position];
        container.img.setImageFromUrl(event.getImg());
        container.title.setText(event.getTitle());
        container.author.setText(res.getString(R.string.tip_event_author,
                event.getAuthor()));
        container.address.setText(res.getString(R.string.tip_event_address,
                event.getAddress()));
        container.time.setText(res.getString(R.string.tip_event_time,
                event.getEventTime()));

        return convertView;
    }
}
