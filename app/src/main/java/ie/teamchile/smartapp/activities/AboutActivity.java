package ie.teamchile.smartapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Login.LoginActivity;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.NotKeys;

public class AboutActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_about);

        progressBar = (ProgressBar) findViewById(R.id.pb_webview);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        WebView webView = (WebView) findViewById(R.id.wv_about);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100)
                    progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(NotKeys.BASE_URL + Constants.TPW_ABOUT_URL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
