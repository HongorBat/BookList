package com.example.booklist.Model;

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mImageUrl;

    public Book(String title, String author, String url, String imageUrl){
        this.mTitle = title;
        this.mAuthor = author;
        this.mUrl = url;
        this.mImageUrl = imageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
