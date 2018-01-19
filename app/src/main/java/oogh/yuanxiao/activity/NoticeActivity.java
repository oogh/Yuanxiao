package oogh.yuanxiao.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.adapter.NoticeRecyclerViewAdapter;
import oogh.yuanxiao.bean.Notice;

public class NoticeActivity extends AppCompatActivity {
    private static final String TAG = NoticeActivity.class.getSimpleName();
    private static final String BASE_URL = "http://www.sspu.edu.cn/info/iList.jsp?cat_id=10665";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private NoticeRecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;
    private Document mDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initView();

    }

    /**
     * 加载通知公告数据
     */
    private void loadData() {
        new LoadNotice().execute(BASE_URL);
    }

    private void initView() {
        initToolbar();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_notice);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_refresh_notice);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_notice);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoticeRecyclerViewAdapter();
        mAdapter.setOnItemClickListener(new NoticeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Notice notice) {
                Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("notice", notice);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        loadData();


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notice);
        toolbar.setTitle("通知公告");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class LoadNotice extends AsyncTask<String, Void, List<Notice>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Notice> doInBackground(String... params) {

            List<Notice> notices = new ArrayList<>();
//            Log.i(TAG, "请求的网址是: " + params[0]);

            try {
                mDocument = Jsoup.connect(params[0]).get();
                Elements links = mDocument.select("#wp_news_w2").select("a");
                for (Element link : links) {
                    String href = link.attr("href");
                    String[] tag_title = link.select("span").get(1).text().split("：");
                    String tag = "";
                    String title = "";
                    if (tag_title.length == 2) {
                        tag = tag_title[0];
                        title = tag_title[1];
                    } else {
                        continue;
                    }

                    String date = link.select("span").get(0).text();
                    notices.add(new Notice("iamgeUri", tag, title, date, href));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return notices;
        }

        @Override
        protected void onPostExecute(List<Notice> notices) {
            super.onPostExecute(notices);
            Log.i(TAG, "获取记录数: " + notices.size());
            mProgressBar.setVisibility(View.GONE);
            mAdapter.setDataset(notices);

        }
    }
}
