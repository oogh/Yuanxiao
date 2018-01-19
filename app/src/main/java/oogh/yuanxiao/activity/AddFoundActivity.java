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
import oogh.yuanxiao.bean.Found;

public class AddFoundActivity extends AppCompatActivity {

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
        mTitle = (EditText) findViewById(R.id.et_input_found_title);
        mPhone = (EditText) findViewById(R.id.et_input_found_phone);
        mDescribe = (EditText) findViewById(R.id.et_input_found_describe);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_add_found);
        mAddButton = (Button) findViewById(R.id.btn_input_found_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String title = mTitle.getText().toString();
                String phone = mPhone.getText().toString();
                String describe = mDescribe.getText().toString();
                Found found = new Found();
                found.setDescribe(describe);
                found.setPhone(phone);
                found.setTitle(title);
                found.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(AddFoundActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            AddFoundActivity.this.setResult(0, intent);
                            finish();
                        } else {
                            Toast.makeText(AddFoundActivity.this, "添加异常！", Toast.LENGTH_SHORT).show();
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
