package oogh.yuanxiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Lost;

public class AddLostActivity extends AppCompatActivity {
    public static final String TAG = AddLostActivity.class.getSimpleName();
    private EditText mTitle, mPhone, mDescribe;
    private Button mAddButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost);
        initView();
    }

    private void initView() {
        initToolbar();
        mTitle = (EditText) findViewById(R.id.et_input_lost_title);
        mPhone = (EditText) findViewById(R.id.et_input_lost_phone);
        mDescribe = (EditText) findViewById(R.id.et_input_lost_describe);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_add_lost);
        mAddButton = (Button) findViewById(R.id.btn_input_lost_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String title = mTitle.getText().toString();
                String phone = mPhone.getText().toString();
                String describe = mDescribe.getText().toString();
                Lost lost = new Lost();
                lost.setDescribe(describe);
                lost.setPhone(phone);
                lost.setTitle(title);
                lost.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(AddLostActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            AddLostActivity.this.setResult(0, intent);
                            finish();
                        } else {
                            Toast.makeText(AddLostActivity.this, "添加异常！", Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_lost);
        toolbar.setTitle("添加寻物信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
