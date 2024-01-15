package com.example.androidlabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private ImageView catImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catImageView = findViewById(R.id.imageViews);
        progressBar = findViewById(R.id.progressBar);

        // Start loading cat images
        new CatImages().execute();
    }

    private class CatImages extends AsyncTask<Void, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject(downloadJson("https://cataas.com/cat?json=true"));
                String catId = jsonObject.getString("id");
                String imageUrl = "https://cataas.com" + jsonObject.getString("url");

                File imageFile = new File(getFilesDir(), catId + ".jpg");
                Bitmap catBitmap;

                if (imageFile.exists()) {
                    catBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                } else {
                    catBitmap = downloadImage(imageUrl);
                    if (catBitmap != null) {
                        saveImageToFile(catBitmap, catId);
                    }
                }

                return catBitmap;
            } catch (Exception e) {
                // Handle errors and log them
                Log.e("CatImages", "Error loading cat image: " + e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                catImageView.setImageBitmap(bitmap);
            }else {
                // Handle the case where no valid bitmap was loaded
                Log.e("CatImages", "No valid bitmap was loaded.");
            }
        }

        private String downloadJson(String urlString) throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            StringBuilder result = new StringBuilder();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.append(new String(buffer, 0, length));
                publishProgress(0); // Indicate progress
            }

            inputStream.close();
            connection.disconnect();
            return result.toString();
        }

        private Bitmap downloadImage(String urlString) throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
            connection.disconnect();
            return bitmap;
        }

        private void saveImageToFile(Bitmap bitmap, String catId) throws Exception {
            File imageFile = new File(getFilesDir(), catId + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        }
    }
}
