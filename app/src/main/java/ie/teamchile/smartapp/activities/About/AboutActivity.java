package ie.teamchile.smartapp.activities.About;

import android.app.NotificationManager;
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

public class AboutActivity extends AppCompatActivity implements AboutView {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableScreenshot();
        setContentView(R.layout.activity_about);

        initViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void disableScreenshot() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.pb_webview);
        WebView webView = (WebView) findViewById(R.id.wv_about);

        progressBar.setProgress(0);
        progressBar.setMax(100);

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
    public void setContentForNav(int layout) {
        throw new UnsupportedOperationException(getString(R.string.mvp_unsupported_operation_exception));
    }

    @Override
    public void setActionBarTitle(String title) {
        throw new UnsupportedOperationException(getString(R.string.mvp_unsupported_operation_exception));
    }

    @Override
    public void createNavDrawer() {
        throw new UnsupportedOperationException(getString(R.string.mvp_unsupported_operation_exception));
    }

    @Override
    public void showNotification(String title, String message, Class activity) {
        throw new UnsupportedOperationException(getString(R.string.mvp_unsupported_operation_exception));
    }

    @Override
    public NotificationManager getNotificationManager() {
        throw new UnsupportedOperationException(getString(R.string.mvp_unsupported_operation_exception));
    }
}
