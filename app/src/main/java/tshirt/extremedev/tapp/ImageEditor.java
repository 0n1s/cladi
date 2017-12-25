package tshirt.extremedev.tapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageEditor extends Activity
{

    WebView webView;
    String currenturl;
    SwipeRefreshLayout swipeRefreshLayout;
public static String front,back;
    class JavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String string)
        {

            if (!string.isEmpty())
            {
                loader(string);
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_image_editor);
        webView=(WebView)findViewById(R.id.webview) ;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setSavePassword(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "AndroidInterface");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                try
                {
                    view.loadUrl("javascript:" + loadingbooster());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                    view.loadUrl(url);
                return true;

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);


            }
        });


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading editor");
        progressDialog.setMessage("Please wait...");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 50)
                {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }


            }
        });

        webView.loadUrl(URLs.main_web);


    }

    private String loadingbooster() throws IOException
    {
        StringBuilder buf = new StringBuilder();
        InputStream inject = getAssets().open("loader.js");// file from assets
        BufferedReader in = new BufferedReader(new InputStreamReader(inject, "UTF-8"));
        String str;
        while ((str = in.readLine()) != null) {
            buf.append(str);
        }
        in.close();

        return buf.toString();
    }





    public void loader(final String string) {

        String[] animalsArray = string.split("two");
        front=animalsArray[0];
        back=animalsArray[1];



       //  startActivity(new Intent(ImageEditor.this, ));

        Intent intent= new Intent(ImageEditor.this,Details.class);
        intent.putExtra("item_price","800");
        intent.putExtra("type","custom");
        intent.putExtra("item_name","Custom Tshirt");
        intent.putExtra("item_description","Owner designed Tshirt");
        startActivity(intent);
       // this.finish();


    }


}
