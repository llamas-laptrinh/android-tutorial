package com.dev.llamas_new.ui.saved;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dev.llamas_new.ui.home.NewsItem;
import com.dev.llamas_new.ui.home.NewsListAdapter;

import java.util.ArrayList;

public class SavedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final LiveData<ArrayList<NewsItem>> listNewsItemLiveData;

    public SavedViewModel() {
        listNewsItemLiveData = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("Nothing Here!!!");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<ArrayList<NewsItem>> getList() {
        return listNewsItemLiveData;
    }
}