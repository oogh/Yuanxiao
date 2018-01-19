package oogh.yuanxiao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Notice;

public class NoticeDetailActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://www.sspu.edu.cn";

    private WebView mWebView;
    private Notice mNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        mNotice = (Notice) getIntent().getExtras().get("notice");
        initView();
    }

    private void initView() {
        initToolbar();
        mWebView = (WebView) findViewById(R.id.wv_notice_detail);
        mWebView.loadUrl(BASE_URL + mNotice.getHref());
        // 设置可以支持缩放 
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notice_detail);
        toolbar.setTitle("详细信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
