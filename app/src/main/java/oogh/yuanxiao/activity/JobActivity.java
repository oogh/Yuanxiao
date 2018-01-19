package oogh.yuanxiao.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import oogh.yuanxiao.adapter.JobRecyclerViewAdapter;
import oogh.yuanxiao.bean.Job;

public class JobActivity extends AppCompatActivity {
    private static final String TAG = JobActivity.class.getSimpleName();

    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private JobRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        initView();
    }

    private void initView() {
        initToolbar();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sfl_job);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.pb_load_job);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_job);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new JobRecyclerViewAdapter();
        mAdapter.setOnItemClickListener(new JobRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Job j) {
                Intent intent = new Intent(JobActivity.this, JobDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("job", j);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    /**
     * 加载实习工作列表
     */
    private void loadData() {
        new LoadJob().execute("http://www.shixiseng.com/");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_job);
        toolbar.setTitle("兼职/工作");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class LoadJob extends AsyncTask<String, Void, List<Job>> {
        private Document mDocument;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Job> doInBackground(String... params) {
            List<Job> jobs = new ArrayList<>();
            try {
                mDocument = Jsoup.connect(params[0]).get();
                Elements contents = mDocument.select(".intern-content-href");
                for (Element content : contents) {
                    // 解析Html
                    Elements aElements = content.select("a");
                    Elements spanElements = content.select("span");
                    String title = aElements.get(1).text();
                    String detailUrl = aElements.get(1).attr("href");
                    String company = aElements.get(2).text();
                    String describe = spanElements.get(0).text();
                    String extra = spanElements.get(1).text()
                            + spanElements.get(2).text();
                    String logoUri = content.select("img").attr("src");
                    String company_describe = company + describe;
                    // 构造Job对象
                    Job job = new Job(title, logoUri, company_describe, extra);
                    job.setDetailUrl(detailUrl);
                    jobs.add(job);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jobs;
        }

        @Override
        protected void onPostExecute(List<Job> jobs) {
            super.onPostExecute(jobs);
            mAdapter.setDataset(jobs);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
