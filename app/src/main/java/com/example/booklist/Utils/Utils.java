package com.example.booklist.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.booklist.Model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link Utils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Utils(){}

    /**
     * Query the GoogleBooks dataset and return list of {@link Book} objects
     */
    public static List<Book> fetchBookData(String stringUrl){
        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        return extractFeaturesFromJson(jsonResponse);
    }

    /**
     * Returns list of {@link Book} objects that has been built up from
     * parsing JSON response
     */
    private static List<Book> extractFeaturesFromJson(String bookJson){
        if (TextUtils.isEmpty(bookJson))
            return null;

        //Create an empty ArrayList that we can start adding new Book objects
        List<Book> books = new ArrayList<>();

        //Try to parse bookJson. If there is a problem with bookJson catch JSONException
        //so app wont crash
        try{
            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++){
                //Get current book at position i
                JSONObject currentBook = bookArray.getJSONObject(i);
                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all information
                // for that book.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String author = "";
                if (volumeInfo.has("authors")){
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                } else {
                    author = "No Author Specified";
                }
                String title = volumeInfo.getString("title");
                String url = volumeInfo.getString("infoLink");

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnail = "";
                if (imageLinks != null && imageLinks.has("thumbnail")) {
                    thumbnail = imageLinks.getString("thumbnail");
                }

                Book book = new Book(title, author, url, thumbnail);
                books.add(book);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the book Json result", e);
        }

        return books;
    }

    /**
     * Returns new URL object from given string
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem Building URL", e);
        }

        return url;
    }


    /**
     * Make an HTTP request to the given URL and return String as response
     */
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";

        if(url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000); // milliseconds
            urlConnection.setConnectTimeout(15000); // milliseconds
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If request was successful with 200 code
            //then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving book JSON results", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                //Closing inputStream could throw IOException
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into String which contains the
     * whole JSON response from the server
     */
    private static String readFromInputStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}
