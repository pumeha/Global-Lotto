package com.preciousumeha.globallotto;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CustomerCareActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout refreshLayout;
    WebView webView;
    ProgressBar bar;
    public static String link ="https://www.global-lotto.com/contact";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_care);
        Toolbar toolbar = findViewById(R.id.cc_toolbar);
        refreshLayout = findViewById(R.id.cc_refresh);
        webView = findViewById(R.id.cc_web);
        bar = findViewById(R.id.cc_progressBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        refreshLayout.setOnRefreshListener(CustomerCareActivity.this);
        webView.loadUrl(link);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                bar.setProgress(newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                refreshLayout.setRefreshing(true);
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                refreshLayout.setRefreshing(false);
                bar.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                String page_error = "<html><body><h1>Webpage not available</h1>"+
                        "<h3>The webpage could not be loaded because:</h3>"+
                        "<h4 style=\"color: red;\"><strong>net:ERR_INTERNET_DISCONNECTED</strong></h4>"+
                        "<h5>Swipe Down To Refresh when <strong style=\"color: green;\"><b>Connected</b></strong></h5></body> </html> ";
                webView.loadData(page_error,"text/html",null);
            }


        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode ==  KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        bar.setVisibility(View.VISIBLE);
        webView.loadUrl(link);
    }
}