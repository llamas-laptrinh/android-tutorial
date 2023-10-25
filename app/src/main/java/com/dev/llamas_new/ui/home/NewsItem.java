package com.dev.llamas_new.ui.home;

import java.io.Serializable;

public class NewsItem implements Serializable {
    private String new_id;
    private String news_image;
    private String news_author_avatar;
    private String news_title;
    private String news_author_name;
    private  String news_created_at;
    private  Boolean is_saved;
    private  String detail;
    private  int category_id;
    private  int like_count;
    private  int view_count;

    public NewsItem(){}

    public NewsItem(String news_image, String news_author_avatar, String news_title, String newsAuthorName, String news_created_at, Boolean isSaved, String detail, int category_id, int likeCount, int viewCount) {
        this.news_image = news_image;
        this.news_author_avatar = news_author_avatar;
        this.news_title = news_title;
        this.news_author_name = newsAuthorName;
        this.news_created_at = news_created_at;
        this.is_saved = isSaved;
        this.detail = detail;
        this.category_id = category_id;
        this.like_count = likeCount;
        this.view_count = viewCount;
    }

    public String getNew_id() {
        return new_id;
    }

    public void setNew_id(String new_id) {
        this.new_id = new_id;
    }

    public int getLike_count() {
        return like_count;
    }

    public int getView_count() {
        return view_count;
    }

    public int getCategory_id() {
        return category_id;
    }

    public Boolean getIs_saved() {
        return is_saved;
    }

    public void setIs_saved(Boolean is_saved) {
        this.is_saved = is_saved;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getNews_author_name() {
        return news_author_name;
    }

    public void setNews_author_name(String news_author_name) {
        this.news_author_name = news_author_name;
    }
    public String getNews_image() {
        return news_image;
    }

    public void setNews_image(String news_image) {
        this.news_image = news_image;
    }

    public String getNews_author_avatar() {
        return news_author_avatar;
    }

    public void setNews_author_avatar(String news_author_avatar) {
        this.news_author_avatar = news_author_avatar;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_created_at() {
        return news_created_at;
    }

    public void setNews_created_at(String news_created_at) {
        this.news_created_at = news_created_at;
    }
}
