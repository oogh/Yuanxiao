package oogh.yuanxiao.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Account;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private SimpleDraweeView mAvatar;
    private EditText mUsername, mEmail, mSchool, mPhone, mPassword, mPasswordConfirm;
    private Button mRegisterButton;

    private Uri mImagePath;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        setUpToolbar();
        mAvatar = (SimpleDraweeView) findViewById(R.id.iv_register_avatar);
        mUsername = (EditText) findViewById(R.id.et_register_username);
        mEmail = (EditText) findViewById(R.id.et_register_email);
        mSchool = (EditText) findViewById(R.id.et_register_school);
        mPhone = (EditText) findViewById(R.id.et_register_phone);
        mPassword = (EditText) findViewById(R.id.et_register_password);
        mPasswordConfirm = (EditText) findViewById(R.id.et_register_password_confirm);
        mProgressBar = (ProgressBar) findViewById(R.id.register_progress);

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mRegisterButton = (Button) findViewById(R.id.btn_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                // TODO: 提交注册信息
                BmobFile avatar = new BmobFile("avatar", null, mImagePath.toString());
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String school = mSchool.getText().toString();
                String phone = mPhone.getText().toString();
                String password = mPassword.getText().toString();
                String passwordConfirm = mPasswordConfirm.getText().toString();
                if (password.equals(passwordConfirm)) {
                    Account account = new Account(avatar, username, email, school, phone, password);
                    account.signUp(new SaveListener<Account>() {
                        @Override
                        public void done(Account account, BmobException e) {
                            mProgressBar.setVisibility(View.GONE);
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "done: e.Message = " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "确认密码输入有误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 从本地选择图片作为头像
     */
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    /**
     * 返回选择图片的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mImagePath = data.getData();
            // 设置头像
            mAvatar.setImageURI(mImagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
    }
}
