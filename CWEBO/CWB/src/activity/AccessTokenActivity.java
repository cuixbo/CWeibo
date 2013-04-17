package activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cuixbo.cweibo.R;

public class AccessTokenActivity extends Activity {
	WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_access_token);

		CApp.ACCESS_TOKEN = CApp.shp.getString("access_token", "");
		CApp.UID = CApp.shp.getString("uid", "");

		if ("".equals(CApp.ACCESS_TOKEN)) {
			webView = (WebView) findViewById(R.id.webview);
			webView.setVerticalScrollBarEnabled(false);
			webView.setHorizontalScrollBarEnabled(false);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new WeiboWebViewClient());
			webView.loadUrl("https://open.weibo.cn/oauth2/authorize?client_id=2840624634&response_type=token&redirect_uri=http://www.baidu.com&display=mobile&forcelogin=true");
		} else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(getApplicationContext(), WeiboHomeActivity_Pull.class);
					startActivity(intent);
					AccessTokenActivity.this.finish();

				}
			}, 1000);
		}
	}

	private class WeiboWebViewClient extends WebViewClient {
		// http://www.baidu.com/#access_token=2.00QxLHwB3OyOGD4beabed2d90uyjpd&remind_in=154767420&expires_in=154767420&uid=1774874574
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// System.out.println("shouldOverrideUrlLoading");
			CApp.ACCESS_TOKEN = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
			CApp.UID=url.substring(url.lastIndexOf("=") + 1);
			CApp.editor.putString("access_token", CApp.ACCESS_TOKEN);
			CApp.editor.putString("uid", CApp.UID);
			CApp.editor.commit();
			Intent intent = new Intent(getApplicationContext(), WeiboHomeActivity_Pull.class);
			startActivity(intent);
			AccessTokenActivity.this.finish();
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			// System.out.println("onReceivedError");
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// System.out.println("onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// System.out.println("onPageFinished");
			super.onPageFinished(view, url);
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			// System.out.println("onReceivedSslError");
			handler.proceed();
		}

	}
}
