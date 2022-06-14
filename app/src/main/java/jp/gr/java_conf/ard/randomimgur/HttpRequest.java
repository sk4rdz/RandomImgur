package jp.gr.java_conf.ard.randomimgur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arduitas on 2017/06/20.
 */

public class HttpRequest extends AsyncTask<Uri.Builder, Void, Bitmap> {
    private MainActivity activity;

    public HttpRequest(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(Uri.Builder... builder) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(builder[0].toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            if ((bitmap.getWidth() == 161 && bitmap.getHeight() == 81)
                    || bitmap.getWidth() <= 50 || bitmap.getHeight() <= 50)
                bitmap = null;
        } catch (MalformedURLException exception) {
            Log.e("ERROR", "MalformedURLException", exception);
        } catch (IOException exception) {
            Log.e("ERROR", "IOException", exception);
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException exception){
                Log.e("ERROR", "IOException", exception);
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        if (result == null)
            activity.loadImage(null);
        else {
            activity.loadFinished(result);
        }
    }

}
