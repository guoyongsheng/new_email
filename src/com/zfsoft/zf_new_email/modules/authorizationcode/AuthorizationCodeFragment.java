/**
 * 
 */
package com.zfsoft.zf_new_email.modules.authorizationcode;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;

/**
 * @author wesley
 * @date: 2017-1-4
 * @Description:
 */
public class AuthorizationCodeFragment extends BaseFragment<AuthorizationCodePresenter> implements AuthorizationCodeContract.View {

	private String url;
	private WebView webView;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_authorization_code;
	}

	@Override
	public void initVariables() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			url = bundle.getString("url");
		}
	}

	@Override
	public void initViews(View view) {
		webView = (WebView) view.findViewById(R.id.authorization_code_webview);
		webView.loadUrl(url);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient());
	}

	public static AuthorizationCodeFragment newInstance(String url) {
		AuthorizationCodeFragment fragment = new AuthorizationCodeFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			webView = null;
		}
	}
}
