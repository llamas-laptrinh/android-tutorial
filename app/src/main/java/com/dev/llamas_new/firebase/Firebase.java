package com.dev.llamas_new.firebase;

import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.llamas_new.ui.home.Category;
import com.dev.llamas_new.ui.home.CategoryAdapter;
import com.dev.llamas_new.ui.home.NewsItem;
import com.dev.llamas_new.ui.home.NewsListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase {
    public  static  String NEWS ="news";
    public static String CATEGORIES ="categories";
    private final DatabaseReference mDatabase;
    ListView listView;
    ArrayList<NewsItem> listNewsItem;
    NewsListAdapter newsListAdapter;
    Context context;

    public  Firebase(){
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public Firebase(ListView listView, ArrayList<NewsItem> listNewsItem, Context context) {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listView = listView;
        this.listNewsItem = listNewsItem;
        this.context = context;
    }


    public void addToSaved(String id, boolean value){
        this.mDatabase.child(NEWS).child(id).child("is_saved").setValue(value);
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
                mDatabase.child(NEWS).orderByChild("category_id").equalTo(model.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listNewsItem.clear();

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
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                });
                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void getNews (){

        if (listView == null) {
            return;
        }
        this.mDatabase.child(NEWS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    // TODO: handle the post
                    NewsItem item = postSnapshot.getValue(NewsItem.class);

                    assert item != null;
                    item.setNew_id(postSnapshot.getKey());
                    listNewsItem.add(item);
                }
                newsListAdapter = new NewsListAdapter(context, listNewsItem);
                listView.setAdapter(newsListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
