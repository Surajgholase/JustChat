package com.nip.justchat;

public class NewsModel {
    private String title;
    private String description;
    private String imageUrl;
    private String url;

    public NewsModel(String title, String description, String imageUrl, String url) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getUrl() { return url; }
}
