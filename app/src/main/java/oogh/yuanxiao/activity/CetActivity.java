package oogh.yuanxiao.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Cet;
import oogh.yuanxiao.db.CetDb;
import oogh.yuanxiao.db.DbHelper;

public class CetActivity extends AppCompatActivity {
    private static final String TAG = CetActivity.class.getSimpleName();

    private EditText mSId, mName;
    private TextView mEId, mListening, mReading, mWriting, mSum;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cet);
        initView();
    }

    private void initView() {
        initToolbar();
        mSId = (EditText) findViewById(R.id.et_cet_sid);
        mName = (EditText) findViewById(R.id.et_cet_name);
        mEId = (TextView) findViewById(R.id.tv_cet_eid);
        mListening = (TextView) findViewById(R.id.tv_cet_listen);
        mReading = (TextView) findViewById(R.id.tv_cet_read);
        mWriting = (TextView) findViewById(R.id.tv_cet_write);
        mSum = (TextView) findViewById(R.id.tv_cet_sum);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_cet_loading);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cet);
        toolbar.setTitle("四六级查询");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 查询成绩
     *
     * @param view
     */
    public void doSearch(View view) {
        hintKb();
        String sid = mSId.getText().toString();

        String url = null;
        try {
            String name = URLDecoder.decode(mName.getText().toString(), "UTF-8");
            if ("".equals(sid) || "".equals(name)) {
                Toast.makeText(this, "学号或姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String eid = obtainEidBySid(sid);
            new LoadCet(eid, name).execute(url);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据学号获取准考证号
     *
     * @param sid 学号
     */
    private String obtainEidBySid(String sid) {
        String eid = "";
        DbHelper helper = DbHelper.getInstance(this);
        CetDb db = new CetDb(helper);
        Cursor cursor = db.select(sid, Integer.parseInt("201706"));
        if (cursor == null) {
            Toast.makeText(this, "未找到记录", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (cursor.moveToNext()) {
            eid = cursor.getString(cursor.getColumnIndex("eid"));
            mEId.setText("准考证号:    " + eid);
        }
        return eid;
    }

    private class LoadCet extends AsyncTask<String, Void, Cet> {

        private String eid;
        private String name;

        public LoadCet(String eid, String name) {
            this.eid = eid;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cet doInBackground(String... params) {
            Cet cet = null;
            try {
                Connection conn = Jsoup.connect("http://www.chsi.com.cn/cet/query?zkzh=" + eid + "&xm=" + name)
                        .header("Referer", "http://www.chsi.com.cn/cet");
                Document doc = conn.get();
                Elements trs = doc.select(".cetTable").select("tr");
                Log.i(TAG, "trs.size = " + trs.size());
                String sum = trs.get(5).select("td").select("span").text();
                String listen = trs.get(6).select("td").get(1).text();
                String read = trs.get(7).select("td").get(1).text();
                String write = trs.get(8).select("td").get(1).text();
                cet = new Cet(listen, write, read, sum);
            } catch (Exception e) {
                Log.i(TAG, "发生错误啦: " + e.getMessage());
                return null;
//                e.printStackTrace();
            }
            return cet;
        }

        @Override
        protected void onPostExecute(Cet cet) {
            super.onPostExecute(cet);
            mProgressBar.setVisibility(View.GONE);
            if (cet == null) {
                Toast.makeText(CetActivity.this, "查询失败,请检查输入是否正确", Toast.LENGTH_SHORT).show();
                return;
            }
            mListening.setText("听    力: " + cet.getListening());
            mWriting.setText("翻译写作: " + cet.getWriting());
            mReading.setText("阅    读: " + cet.getReading());
            mSum.setText("总    分: " + cet.getSum());
        }
    }

    /**
     * 此方法只是关闭软键盘
     */
    private void hintKb() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
