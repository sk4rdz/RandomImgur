package jp.gr.java_conf.ard.randomimgur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> history;
    private String displayUrl;
    private TextView textView1;
    private TextView textView2;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.urlView) ;
        textView2 = (TextView)findViewById(R.id.textView) ;
        imageView = (ImageView)findViewById(R.id.imageView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar) ;
        progressBar.setVisibility(View.INVISIBLE);
        button =  (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loading();
            }
        });
        history = new ArrayList();
        displayUrl = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backMenu:
                if (history.size() >= 1) {
                    int lastIndex = history.size() - 1;
                    loadImage(history.get(lastIndex));
                    history.remove(lastIndex);
                }
                break;
            case R.id.shareMenu:
                if (displayUrl != null) {
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.extra_subject));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, displayUrl);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.Chooser_text)));
                }
        }
        return true;
    }

    private void loading(){
        textView2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        if (displayUrl != null)
            history.add(displayUrl);
        loadImage(null);
    }

    protected void loadImage(String pass) {
        if (pass == null)
            displayUrl = getString(R.string.url) + randomPass();
        else displayUrl = pass;
        String url = displayUrl + ".jpg";
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        HttpRequest task = new HttpRequest(this);
        task.execute(builder);
    }

    protected void loadFinished(Bitmap result) {
        progressBar.setVisibility(View.INVISIBLE);
        button.setEnabled(true);
        imageView.setImageBitmap(result);
        textView1.setText(displayUrl);
    }

    private String randomPass() {
        char[] c = new char[62];
        char[] pass = new char[5];
        for (int i = 0; i < 10; i++)
            c[i] = ((char)('0' + i));
        for (int i = 0; i < 26; i++)
            c[i + 10] = ((char)('a' + i));
        for (int i = 0; i < 26; i++)
            c[i + 36] = ((char)('A' + i));
        for (int i = 0; i < pass.length; i++)
            pass[i] = c[(int) (Math.random() * 62)];
        return String.valueOf(pass);
    }
}
