package com.example.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.booklist.Adapter.BookAdapter;
import com.example.booklist.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>{

    private EditText mSearchEditText;
    private String mSearchKeyword;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter adapter;
    private String requestURL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

    /**
     * Fix ImageView and Everytime click of a button change the results also image caching
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = findViewById(R.id.search_keyword);
        Button mSearchButton = findViewById(R.id.search_button);

        ListView listView = findViewById(R.id.listView);
        adapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(adapter);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mSearchEditText.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter Keyword", Toast.LENGTH_SHORT).show();
                } else {
                    mSearchKeyword = mSearchEditText.getText().toString();
                    requestURL = requestURL.replace("android", mSearchKeyword);
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = adapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, requestURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        //Clear adapter
        adapter.clear();

        if (data != null || !data.isEmpty()){
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }
}
