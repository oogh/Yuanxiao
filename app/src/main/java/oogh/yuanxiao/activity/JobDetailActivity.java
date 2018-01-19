package oogh.yuanxiao.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Job;
import oogh.yuanxiao.bean.JobDetail;

public class JobDetailActivity extends AppCompatActivity {
    private static final String TAG = JobDetailActivity.class.getSimpleName();
    private static final String BASE_URL = "http://www.shixiseng.com";
    private ProgressBar mProgressBar;
    private TextView mTitle, mMoney, mPosition, mWeek, mGood, mDetail, mDeadLine;
    private Document mDocument;
    private Job mJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        mJob = (Job) getIntent().getExtras().get("job");
        initView();
        new LoadDetail().execute(BASE_URL + mJob.getDetailUrl());
    }

    private void initView() {
        initToolbar();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_job_detail);
        mTitle = (TextView) findViewById(R.id.tv_job_detail_title);
        mMoney = (TextView) findViewById(R.id.tv_job_detail_money_cutom);
        mPosition = (TextView) findViewById(R.id.tv_job_detail_position);
        mWeek = (TextView) findViewById(R.id.tv_job_detail_week_cutom);
        mGood = (TextView) findViewById(R.id.tv_job_detail_good);
        mDetail = (TextView) findViewById(R.id.tv_job_detail);
        mDeadLine = (TextView) findViewById(R.id.tv_job_detail_deadline);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_job_detail);
        toolbar.setTitle("职位详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void doSend(View view) {
        Toast.makeText(JobDetailActivity.this, "点我了..", Toast.LENGTH_SHORT).show();
    }

    private class LoadDetail extends AsyncTask<String, Void, JobDetail> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected JobDetail doInBackground(String... params) {

            JobDetail detail = null;
            String title = mJob.getTitle();
            String[] extras = mJob.getExtra().split("周");
            String money = extras[1];
            String week = extras[0] + "周";


            // date / good / describe / required / deadline


            try {
                Log.i(TAG, "url = " + params[0]);
                mDocument = Jsoup.connect(params[0]).get();
                String good = mDocument.select(".job_good").text();
                Element describeEl = mDocument.select(".job_detail").get(0);
                String describe = describeEl.text();
//                StringBuilder sb_describe = new StringBuilder();
//                StringBuilder sb_required = new StringBuilder();
//                boolean flag1 = false;
//                boolean flag2 = false;
//                for (int i = 0; i < paras.size(); i++) {
//                    Element el = paras.get(i);
//                    if ("职位描述：".equals(el.text())) {
//                        flag1 = true;
//                        continue;
//                    }
//
//                    if ("任职要求：".equals(el.text())) {
//                        flag1 = false;
//                        flag2 = true;
//                        continue;
//
//                    }
//                    if (flag1) {
//                        sb_describe.append(el.text().concat("\n"));
//                    }
//
//                    if (flag2) {
//                        sb_required.append(el.text().concat("\n"));
//                    }
//                }
//                for (Element el : paras) {
//                    sb.append(el.text().concat("\n"));
////                }
//                String describe = sb_describe.toString();
//                String required = sb_required.toString();


                //title, money, position, week, describe, required, deadline
                detail = new JobDetail(
                        title,
                        money,
                        "上海",
                        week,
                        describe,
                        "deadline",
                        good);
//
            } catch (IOException e) {
                e.printStackTrace();
            }

            return detail;
        }

        @Override
        protected void onPostExecute(JobDetail detail) {
            super.onPostExecute(detail);
            mProgressBar.setVisibility(View.GONE);
            mTitle.setText(detail.getTitle());
            mMoney.setText(detail.getMoney());
            mPosition.setText(detail.getPosition());
            mWeek.setText(detail.getWeek());
            mGood.setText(detail.getGood());
            mDetail.setText(detail.getDescribe());
            mDeadLine.setText(detail.getDeadline());
        }
    }
}
