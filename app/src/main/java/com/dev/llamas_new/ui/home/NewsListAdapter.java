package com.dev.llamas_new.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.dev.llamas_new.R;
import com.dev.llamas_new.firebase.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsListAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<NewsItem> listNewsItem;

    public NewsListAdapter(Context context, ArrayList<NewsItem> listNewsItem) {
        this.context = context;
        this.listNewsItem = listNewsItem;
    }


    @Override
    public int getCount() {
        return this.listNewsItem.size();
    }

    @Override
    public Object getItem(int i) {
        return this.listNewsItem.get(i);
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
            viewProduct = layoutInflater.inflate( R.layout.news_item,null);
        }else{
            viewProduct= view;
        }

        NewsItem item = (NewsItem) getItem(i);
        ImageButton savedBtn = viewProduct.findViewById(R.id.btn_book_mark);
        ImageView newsImage = (viewProduct.findViewById(R.id.news_image));
        ImageView newsAuthorAvatar = (viewProduct.findViewById(R.id.news_author_avatar));
        TextView newsTitle = (viewProduct.findViewById(R.id.news_title));
        TextView newsAuthorName = (viewProduct.findViewById(R.id.news_author_name));
        TextView newsCreatedAt = (viewProduct.findViewById(R.id.news_created_at));

        Picasso.with(context).load(item.getNews_image()).into(newsImage);
        Picasso.with(context).load(item.getNews_author_avatar()).into(newsAuthorAvatar);

        savedBtn.setOnClickListener(view1 -> {
            boolean value = true;
            int color = R.color.primary;
            if (item.getIs_saved()){
                value = false;
                color = R.color.gray_200;
            }
            savedBtn.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
            new Firebase().addToSaved(item.getNew_id(),value);
            notifyDataSetChanged();
           });
        if (item.getIs_saved()){
            savedBtn.setImageResource(R.drawable.baseline_bookmark_24);
            savedBtn.setColorFilter(ContextCompat.getColor(context, R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        newsTitle.setText(item.getNews_title());
        newsAuthorName.setText(item.getNews_author_name());
        newsCreatedAt.setText(item.getNews_created_at());

        return viewProduct;
    }
}
