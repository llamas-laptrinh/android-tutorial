package com.dev.llamas_new.ui.notifications;

public class NotificationItems {
    private String noti_id;
    private String title;
    private String content;
    private String type;

    public NotificationItems(String id, String title, String content, String type) {
        this.noti_id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public NotificationItems() {
    }

    public String getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(String noti_id) {
        this.noti_id = noti_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
