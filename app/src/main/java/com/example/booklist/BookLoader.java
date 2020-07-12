package com.example.booklist;

import android.content.Context;
import android.text.TextUtils;
import android.content.AsyncTaskLoader;
import com.example.booklist.Model.Book;
import com.example.booklist.Utils.Utils;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        if (TextUtils.isEmpty(mUrl))
            return null;

        return Utils.fetchBookData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
