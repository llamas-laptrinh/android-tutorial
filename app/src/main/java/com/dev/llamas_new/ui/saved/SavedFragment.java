package com.dev.llamas_new.ui.saved;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.dev.llamas_new.ui.signInMethod.SignInMethodActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    ArrayList<NewsItem>  listNewsItem;
    FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedViewModel savedViewModel =
                new ViewModelProvider(this).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;

        auth =  FirebaseAuth.getInstance();
        ListView listView = root.findViewById(R.id.saved_news);
        Button signOut = binding.btnSignOut;
        TextView userId = binding.userId;
        listView.setEmptyView(textView);

        listNewsItem = new ArrayList<>();

        NewsListAdapter adapter = new NewsListAdapter(getContext(),listNewsItem);
        Firebase firebase = new Firebase(listView,listNewsItem, root.getContext(),adapter);

        firebase.getSaved();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            NewsItem item = listNewsItem.get(i);
            item.setView_count(item.getView_count()+1);
            intent.putExtra("ITEM", item);
            startActivity(intent);
        });

        signOut.setOnClickListener(view -> {
            auth.signOut();
          startActivity( new Intent(getContext(), SignInMethodActivity.class));
        });
        userId.setText(auth.getUid());
        savedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        savedViewModel.getList().observe(getViewLifecycleOwner(),listNewsItem::addAll);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume"+ FirebaseAuth.getInstance().getUid());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}