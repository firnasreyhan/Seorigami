package com.project.seorigami.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.project.seorigami.databinding.ActivityPaymentWebViewBinding
import com.project.seorigami.model.response.TransactionDataModel
import com.project.seorigami.util.KeyIntent

class PaymentWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentWebViewBinding
    private lateinit var transactionData: TransactionDataModel
    private var urlPayment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        urlPayment = intent.getStringExtra(KeyIntent.URL_PAYMENT.name)
        transactionData = intent.getParcelableExtra(KeyIntent.TRANSACTION_DATA.name)!!



    }

    private fun openUrlFromWebView() {
        binding.apply {
            webview.settings.loadsImagesAutomatically = true
            webview.settings.javaScriptEnabled =true
            webview.scrollBarSize = View.SCROLLBARS_INSIDE_OVERLAY
            webview.settings.domStorageEnabled = true

            webview.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
//                    Log.e("abc", request?.url.toString())
                    view?.loadUrl(request?.url.toString())
                    return true
//                    val requestUrl = request?.url.toString()
//                    return if (requestUrl.contains("mitradigitalelektronika/")) {
////                        val intent = Intent(Intent.ACTION_VIEW, request!!.url)
////                        startActivity(intent)
//                        view?.loadUrl(request?.url.toString())
//                        true
//                    } else {
//                        val intent = Intent(this@PaymentWebViewActivity, MainActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                        startActivity(intent)
//                        false
//                    }
                }
            }
            webview.loadUrl(urlPayment.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@PaymentWebViewActivity, StatusPaymentActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(KeyIntent.TRANSACTION_DATA.name, transactionData)
        startActivity(intent)
    }
}