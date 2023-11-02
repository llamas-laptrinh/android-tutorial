package com.dev.llamas_new.ui.saved;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dev.llamas_new.R;
import com.dev.llamas_new.databinding.FragmentSavedBinding;
import com.dev.llamas_new.firebase.Firebase;
import com.dev.llamas_new.ui.home.DetailActivity;
import com.dev.llamas_new.ui.home.NewsItem;
import com.dev.llamas_new.ui.home.NewsListAdapter;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedViewModel savedViewModel =
                new ViewModelProvider(this).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;

        ListView listView = root.findViewById(R.id.saved_news);
        listView.setEmptyView(textView);

        savedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        ArrayList<NewsItem>  listNewsItem = new ArrayList<>();
        NewsListAdapter adapter = new NewsListAdapter(getContext(),listNewsItem);
        new Firebase(listView,listNewsItem, root.getContext(),adapter).getSaved();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            NewsItem item = listNewsItem.get(i);
            item.setView_count(item.getView_count()+1);
            listNewsItem.clear();
            intent.putExtra("ITEM", item);
            startActivity(intent);

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}