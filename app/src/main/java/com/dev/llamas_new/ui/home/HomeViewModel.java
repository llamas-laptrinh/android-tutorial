package com.dev.llamas_new.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final LiveData<ArrayList<NewsItem>> listNewsItemLiveData;

    public HomeViewModel() {
        listNewsItemLiveData = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("Nothing Here!!");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<ArrayList<NewsItem>> getList() {
        return listNewsItemLiveData;
    }
}