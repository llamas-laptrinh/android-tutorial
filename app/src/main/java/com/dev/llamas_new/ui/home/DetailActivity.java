package com.dev.llamas_new.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.llamas_new.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView textView;
    TextView tv_item_title, tv_item_like_count, tv_item_view_count,tv_author_name;
    ImageView iv_item_banner,iv_author_avatar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();


        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setTitle("Happy reading");
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv_item_banner = findViewById(R.id.item_banner);
        iv_author_avatar = findViewById(R.id.item_author_avatar);
        tv_item_title = findViewById(R.id.item_title);
        tv_item_view_count = findViewById(R.id.item_view_count);
        tv_item_like_count = findViewById(R.id.item_like_count);
        tv_author_name = findViewById(R.id.item_author_name);
        textView = findViewById(R.id.item_detail);

        NewsItem item = (NewsItem)getIntent().getSerializableExtra("ITEM");
        assert item != null;
        Picasso.with(getApplicationContext()).load(item.getNews_image()).into(iv_item_banner);
        Picasso.with(getApplicationContext()).load(item.getNews_author_avatar()).into(iv_author_avatar);
        tv_item_title.setText(item.getNews_title());
        tv_item_like_count.setText(formatValue(item.getLike_count()));
        tv_item_view_count.setText(formatValue(item.getView_count()));
        tv_author_name.setText(item.getNews_author_name()+" - "+item.getNews_created_at());
        textView.setText(item.getDetail());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String formatValue(float value) {
        String arr[] = {"", "K", "M", "B", "T", "P", "E"};
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return String.format("%s %s", decimalFormat.format(value), arr[index]);
    }
}
