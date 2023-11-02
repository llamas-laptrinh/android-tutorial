package com.dev.llamas_new.ui.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.llamas_new.R;
import com.dev.llamas_new.ui.home.NewsItem;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    final Context context;
   final ArrayList<NotificationItems> itemsArrayList;

    public NotificationAdapter(Context context, ArrayList<NotificationItems> itemsArrayList) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public int getCount() {
        return itemsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewProduct;
        if (view == null) {
            viewProduct = layoutInflater.inflate( R.layout.notification_items,null);
        }else{
            viewProduct= view;
        }

        NotificationItems item = (NotificationItems) getItem(i);
        TextView notification_title = (viewProduct.findViewById(R.id.notification_title));
        TextView notification_content = (viewProduct.findViewById(R.id.notification_content));
        Log.d("TAG", "getNotifications: "+item.getTitle());

        notification_title.setText(item.getTitle());
        notification_content.setText(item.getContent());
        return  viewProduct;
    }
}
