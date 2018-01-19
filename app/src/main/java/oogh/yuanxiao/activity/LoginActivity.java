package oogh.yuanxiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import oogh.yuanxiao.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private EditText mUsername, mPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);


        mProgressBar = (ProgressBar) findViewById(R.id.pb_login);
        mUsername = (EditText) findViewById(R.id.et_login_username);
        mPassword = (EditText) findViewById(R.id.et_login_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                if ("" != username && "" != password) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    BmobUser user = new BmobUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                // TODO: 登录成功
//                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // TODO: 登录失败
                                Log.e(TAG, "登录失败: " + e);
                            }
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
