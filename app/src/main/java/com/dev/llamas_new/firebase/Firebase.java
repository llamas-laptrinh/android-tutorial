package com.dev.llamas_new.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.llamas_new.ui.home.Category;
import com.dev.llamas_new.ui.home.CategoryAdapter;
import com.dev.llamas_new.ui.home.NewsItem;
import com.dev.llamas_new.ui.home.NewsListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public  static  String SAVED ="Saved";
    public static String CATEGORIES ="categories";
    private DatabaseReference mDatabase;
    ListView listView;
    ArrayList<NewsItem> listNewsItem;
    NewsListAdapter newsListAdapter;
    Context context;
    DatabaseReference savedRef;


    public  Firebase(){
      initFirebase();
    }
    public Firebase(ListView listView, ArrayList<NewsItem> listNewsItem, Context context) {
        initFirebase();
        this.listView = listView;
        this.listNewsItem = listNewsItem;
        this.context = context;
    }

    void initFirebase(){
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.savedRef =this.mDatabase.child(SAVED).child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
    }

    public void getSaved() {
        savedRef.get().addOnCompleteListener(savedSnapshot -> {
            ArrayList<NewsItem> listNewsItem = new ArrayList<>();
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
                    NewsItem item = postSnapshot.getValue(NewsItem.class);
                    assert item != null;
                    item.setNew_id(postSnapshot.getKey());
                    savedRef.child(item.getNew_id()).get().addOnCompleteListener(task -> {
                        item.setIs_saved(task.getResult().exists());
                        listNewsItem.add(item);
                        if (listNewsItem.size() == snapshot.getChildrenCount()) {
                            listView.setAdapter(new NewsListAdapter(context, listNewsItem));
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
