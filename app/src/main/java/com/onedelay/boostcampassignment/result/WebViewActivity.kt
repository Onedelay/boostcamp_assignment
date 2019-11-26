package com.onedelay.boostcampassignment.result

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.utils.Constants
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setSupportActionBar(toolbar)

        button_home.setOnClickListener {
            onBackPressed()
        }

        setupWebView(intent.getStringExtra(Constants.URL))
    }

    private fun setupWebView(url: String) {
        webView.apply {
            webViewClient = WebViewClient()

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    web_view_load_progress_bar.progress = newProgress
                    web_view_load_progress_bar.visibility = if(newProgress < 100) View.VISIBLE else View.GONE
                }
            }

            /** https://developer.android.com/training/articles/security-tips#WebView **/
            settings.javaScriptEnabled = true

            loadUrl(url)

            setWebContentsDebuggingEnabled(true)
        }
    }

}
