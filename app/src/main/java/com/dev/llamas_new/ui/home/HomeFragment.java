package com.dev.llamas_new.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.llamas_new.MainActivity;
import com.dev.llamas_new.R;
import com.dev.llamas_new.databinding.FragmentHomeBinding;
import com.dev.llamas_new.firebase.Firebase;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<NewsItem> listNewsItem;
    ArrayList<Category> categoryList;
    ArrayList<String> carouselList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        carouselList = new ArrayList<>();
        listNewsItem = new ArrayList<>();
        categoryList = new ArrayList<>();

        final RecyclerView carouselRecyclerView =binding.carouselRecyclerView;
        final ListView listView = root.findViewById(R.id.news);
        final RecyclerView recyclerView =  root.findViewById(R.id.category_list);

        Firebase firebase = new Firebase(listView,listNewsItem, root.getContext());

        CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager();
        carouselRecyclerView.setLayoutManager(carouselLayoutManager);

        carouselList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRL3s2YxzGwlKiBhyfg_N6HCS2BeurPm9ySzpXisRjvE0N8kZGcb4UvJSTlSTJvjCFyfZ0&usqp=CAU");
        carouselList.add("https://media.istockphoto.com/id/505266386/photo/vibrant-sunrise-at-buttermere-with-reflections-and-snow-on-mountains.jpg?s=612x612&w=0&k=20&c=MDDSSjCvRrGHRSQ_x-lXcR2_stsGT65Mz6Th5-BSfow=");
        carouselList.add("https://media.istockphoto.com/id/516415148/photo/dawn-at-buttermere-with-dramatic-clouds-and-lone-tree.jpg?s=612x612&w=0&k=20&c=Tq1ztNX0LJSrGWo--iykXKHoU6pSQALBBXz1ZvwGYQk=");
        carouselList.add("https://media.istockphoto.com/id/516415148/photo/dawn-at-buttermere-with-dramatic-clouds-and-lone-tree.jpg?s=612x612&w=0&k=20&c=Tq1ztNX0LJSrGWo--iykXKHoU6pSQALBBXz1ZvwGYQk=");
        CarouselAdapter carouselAdapter = new CarouselAdapter(carouselList,root.getContext());
        carouselRecyclerView.setAdapter(carouselAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        firebase.getCategory(recyclerView,categoryList);
        firebase.getNews();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
           Intent intent = new Intent(getContext(),DetailActivity.class);
           intent.putExtra("ITEM", listNewsItem.get(i));
           startActivity(intent);
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}