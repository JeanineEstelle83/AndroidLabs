package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView catImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catImageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        CatImages catImagesTask = new CatImages();
        catImagesTask.execute();
    }

    private class CatImages extends AsyncTask<Void, Integer, Void> {
        private Bitmap currentCatPicture;

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isCancelled()) {
                try {
                    URL url = new URL("https://cataas.com/cat?json=true");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    StringBuilder response = new StringBuilder();
                    int data;
                    while ((data = inputStream.read()) != -1) {
                        response.append((char) data);
                    }
                    inputStream.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    String catId = jsonObject.getString("id");
                    String catImageUrl = jsonObject.getString("url");

                    File localFile = new File(getFilesDir(), catId + ".jpg");
                    if (localFile.exists()) {
                        currentCatPicture = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    } else {
                        URL imageUrl = new URL(catImageUrl);
                        HttpURLConnection imageConnection = (HttpURLConnection) imageUrl.openConnection();
                        imageConnection.connect();
                        InputStream imageInput = imageConnection.getInputStream();
                        currentCatPicture = BitmapFactory.decodeStream(imageInput);

                        FileOutputStream outputStream = new FileOutputStream(localFile);
                        currentCatPicture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }

                    publishProgress(0);
                    for (int i = 0; i < 100; i++) {
                        try {
                            publishProgress(i);
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }  catch (IOException e) {
                    // Handle IOException (specific to network operations)
                    Log.e("CatImages", "IO Exception: " + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    // Handle JSONException (specific to JSON parsing)
                    Log.e("CatImages", "JSON Exception: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    // Catch any other generic exceptions
                    Log.e("CatImages", "General Exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (currentCatPicture != null) {
                            catImageView.setImageBitmap(currentCatPicture);
                        }
                        progressBar.setProgress(values[0]);
                    } catch (Exception e) {
                        Log.e("CatImages", "Error in onProgressUpdate: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}