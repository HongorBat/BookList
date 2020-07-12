package com.example.booklist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.booklist.Model.Book;
import com.example.booklist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> bookList) {
        super(context, 0, bookList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView mTitleTextView = convertView.findViewById(R.id.title);
        mTitleTextView.setText(currentBook.getTitle());

        TextView mAuthorTextView = convertView.findViewById(R.id.author);
        mAuthorTextView.setText(currentBook.getAuthor());

        ImageView mImageView = convertView.findViewById(R.id.image);
        Picasso.get().load(currentBook.getImageUrl()).error(R.drawable.ic_launcher_foreground).into(mImageView);

        return convertView;
    }
}
