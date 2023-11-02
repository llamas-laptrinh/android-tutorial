package com.dev.llamas_new.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.llamas_new.R;
import com.dev.llamas_new.firebase.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView textView;
    TextView tv_item_title, tv_item_like_count, tv_item_view_count,tv_author_name;
    ImageView iv_item_banner,iv_author_avatar;

    LinearLayout btn_like;
    Firebase firebase;
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

        ImageView imageViewLike = findViewById(R.id.like_icon);
        btn_like = findViewById(R.id.btn_like);



        NewsItem items = (NewsItem)getIntent().getSerializableExtra("ITEM");
        assert items != null;
        firebase = new Firebase();

        DatabaseReference likesRef =  firebase.updateLikeCount();
        likesRef
                .child(items.getNew_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()){
                        imageViewLike.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                });

        firebase.updateViewCount(items.getNew_id(),items.getView_count());

        btn_like.setOnClickListener(view -> {

         String id = items.getNew_id();
         likesRef.child(id)
                    .get()
                    .addOnCompleteListener(task -> {
                        int like = items.getLike_count();
                        int color = R.color.primary;
                        if (task.getResult().exists()){
                            like -=1;
                            items.setLike_count(like);
                            likesRef.removeValue();
                            color = R.color.gray_200;
                        }
                        else {
                            like+=1;
                            likesRef.child(id).setValue(id);
                        }
                        DatabaseReference ref = firebase.getmDatabase().child(Firebase.NEWS).child(id).child(Firebase.LIKE_COUNT);
                        ref.setValue(like);
                        imageViewLike.setColorFilter(ContextCompat.getColor(getApplicationContext(),color), android.graphics.PorterDuff.Mode.SRC_IN);
                    });
        });
        firebase.getDetail(items.getNew_id()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               NewsItem item = snapshot.getValue(NewsItem.class);
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        String[] arr = {"", "K", "M", "B", "T", "P", "E"};
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return String.format("%s %s", decimalFormat.format(value), arr[index]);
    }
}
