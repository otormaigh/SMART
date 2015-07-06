package ie.teamchile.smartapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import ie.teamchile.smartapp.R;

public class AboutActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        progressBar = (ProgressBar) findViewById(R.id.pb_webview);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        webView = (WebView) findViewById(R.id.wv_about);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                Log.d("bugs", "progress = " + newProgress);
                if(newProgress == 100)
                    progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl("http://54.72.7.91/#/about");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
