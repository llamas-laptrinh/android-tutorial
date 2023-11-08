package com.dev.llamas_new.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.llamas_new.ui.home.Category;
import com.dev.llamas_new.ui.home.CategoryAdapter;
import com.dev.llamas_new.ui.home.DetailActivity;
import com.dev.llamas_new.ui.home.NewsItem;
import com.dev.llamas_new.ui.home.NewsListAdapter;
import com.dev.llamas_new.ui.notifications.NotificationAdapter;
import com.dev.llamas_new.ui.notifications.NotificationItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;



public class Firebase {
    public  static  String NEWS ="news";
    public  static  String SAVED ="saved";
    public  static  String LIKES ="likes";
    public  static  String NOTIFICATIONS ="notifications";
    public static String CATEGORIES ="categories";
    public  static String VIEW_COUNT ="view_count";
    public  static String LIKE_COUNT ="like_count";
    private DatabaseReference mDatabase;
    public ListView listView;
    public ArrayList<NewsItem> listNewsItem;
    public NewsListAdapter newsListAdapter;
    Context context;
    public DatabaseReference savedRef;


    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public  Firebase(){
      initFirebase();
    }
    public Firebase(ListView listView, ArrayList<NewsItem> listNewsItem, Context context, NewsListAdapter adapter) {
        initFirebase();
        this.listView = listView;
        this.listNewsItem = listNewsItem;
        this.context = context;
        this.newsListAdapter = adapter;
    }

    void initFirebase(){
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        if(FirebaseAuth.getInstance().getUid() != null){
            this.savedRef =this.mDatabase.child(SAVED).child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        }
    }

    public DatabaseReference getDetail(String id){
        return mDatabase.child(NEWS).child(id);
    }
    public void getSaved() {
        savedRef.get().addOnCompleteListener(savedSnapshot -> {
            for (DataSnapshot postSnapshot : savedSnapshot.getResult().getChildren()) {
                String id = postSnapshot.getValue(String.class);
                assert id != null;

                mDatabase.child(NEWS).child(id).get().addOnCompleteListener(newsSnapshot -> {
                    NewsItem item = newsSnapshot.getResult().getValue(NewsItem.class);
                    assert item != null;
                    item.setIs_saved(true);
                    item.setNew_id(newsSnapshot.getResult().getKey());
                    listNewsItem.add(item);

                    // Check if all of the asynchronous calls have completed
                    if (listNewsItem.size() == savedSnapshot.getResult().getChildrenCount()) {
                        // Update the UI
                        listView.setAdapter(new NewsListAdapter(context, listNewsItem));
                    }
                });
            }
        });
    }
    public void updateViewCount(String id, int value){
        DatabaseReference ref = this.mDatabase.child(NEWS).child(id).child(VIEW_COUNT);
        ref.setValue(value);
    }

    public DatabaseReference updateLikeCount(){
        String user_id  = Objects.requireNonNull(FirebaseAuth.getInstance().getUid());
        return this.mDatabase.child(LIKES).child(user_id);
    }


    public void savedItem(String id, boolean isAdd){
      if (isAdd){
          savedRef.child(id).removeValue();
      }else {
          savedRef.child(id).setValue(id);
      }

    }

    public  void getCategory (RecyclerView recyclerView, ArrayList<Category> categoryList){
        this.mDatabase.child(CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Category item = postSnapshot.getValue(Category.class);
                    categoryList.add(item);
                }
                CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
                categoryAdapter.setOnClickListener((position, model) -> {

                listNewsItem.clear();

                mDatabase.child(NEWS)
                        .orderByChild("category_id")
                        .equalTo(model.getId())
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            NewsItem item = postSnapshot.getValue(NewsItem.class);
                            assert item != null;
                            item.setNew_id(postSnapshot.getKey());
                            listNewsItem.add(item);
                        }
                        listView.setAdapter(newsListAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                });
                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  DatabaseReference getNewsRef (){
        return  this.mDatabase.child(NEWS);
    }
    public  void getNews (){
        this.mDatabase.child(NEWS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    NewsItem item = postSnapshot.getValue(NewsItem.class);
                    assert item != null;
                    item.setNew_id(postSnapshot.getKey());
                    savedRef.child(item.getNew_id()).get().addOnCompleteListener(task -> {
                        item.setIs_saved(task.getResult().exists());
                        listNewsItem.add(item);
                        if (listNewsItem.size() == snapshot.getChildrenCount()) {
                            listView.setAdapter(newsListAdapter);
                        }
                    });

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getNotifications(Context context,ListView notificationListView, ArrayList<NotificationItems> notificationItems) {
        this.mDatabase.child(NOTIFICATIONS).get().addOnCompleteListener(snapshot -> {
            for (DataSnapshot postSnapshot: snapshot.getResult().getChildren()) {
                NotificationItems item = postSnapshot.getValue(NotificationItems.class);
                assert item != null;
                item.setNoti_id(postSnapshot.getKey());
                notificationItems.add(item);
            }
            NotificationAdapter adapter = new NotificationAdapter(context,notificationItems);
            notificationListView.setAdapter(adapter);
        });
    }
}
