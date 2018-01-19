package oogh.yuanxiao.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Lost;

public class DetailLostActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = DetailLostActivity.class.getSimpleName();
    private TextView mTitle, mTime, mPhone, mDescribe;
    private FloatingActionButton mDeleteButton, mCallButton;
    private Bundle mBundle;
    private String mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lost);
        mBundle = getIntent().getExtras();
        initView();
    }

    private void initView() {
        initToolbar();
        mTitle = (TextView) findViewById(R.id.tv_lost_detail_title);
        mTime = (TextView) findViewById(R.id.tv_lost_detail_time);
        mPhone = (TextView) findViewById(R.id.tv_lost_detail_phone);
        mDescribe = (TextView) findViewById(R.id.tv_lost_detail_describe);
        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_lost_detail_delete);
        mCallButton = (FloatingActionButton) findViewById(R.id.fab_lost_detail_call);
        String title = mBundle.getString("title");
        String time = mBundle.getString("time");
        mPhoneNumber = mBundle.getString("phone");
        String describe = mBundle.getString("describe");
        mTitle.setText(title);
        mTime.setText("日期： " + time);
        mPhone.setText("手机： " + mPhoneNumber);
        mDescribe.setText(describe);
        mDeleteButton.setOnClickListener(this);
        mCallButton.setOnClickListener(this);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_lost);
        toolbar.setTitle("详细信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_lost_detail_delete:
                Snackbar.make(v, "确定删除？", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Lost lost = new Lost();
                        lost.setObjectId(mBundle.getString("objectid"));
                        lost.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(DetailLostActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    setResult(1);
                                    finish();
                                } else {
                                    Toast.makeText(DetailLostActivity.this, "删除失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).show();
                break;
            case R.id.fab_lost_detail_call:
                Snackbar.make(v, "确定打电话给TA", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + mPhoneNumber));
                        startActivity(intent);
                    }
                }).show();
                break;
            default:
                break;
        }
    }
}
