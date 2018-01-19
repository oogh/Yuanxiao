package oogh.yuanxiao.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.bmob.v3.BmobUser;
import oogh.yuanxiao.R;
import oogh.yuanxiao.activity.LoginActivity;
import oogh.yuanxiao.activity.RegisterActivity;
import oogh.yuanxiao.bean.Account;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private Account mCurrentUser;

    public AccountFragment() {
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        mCurrentUser = BmobUser.getCurrentUser(Account.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        TextView name, email, school, phone;
        Button logout, register;
        logout = (Button) root.findViewById(R.id.btn_user_logout);
        register = (Button) root.findViewById(R.id.btn_user_register);
        register.setOnClickListener(this);
        logout.setOnClickListener(this);
        name = (TextView) root.findViewById(R.id.tv_username);
        email = (TextView) root.findViewById(R.id.tv_email);
        school = (TextView) root.findViewById(R.id.tv_user_school);
        phone = (TextView) root.findViewById(R.id.tv_user_phone);
        name.setText(mCurrentUser.getUsername());
        email.setText(mCurrentUser.getEmail());
        school.setText(mCurrentUser.getSchool());
        phone.setText(mCurrentUser.getPhone());
        SimpleDraweeView avatar = (SimpleDraweeView) root.findViewById(R.id.iv_avatar);
        Uri imageUri = Uri.parse(mCurrentUser.getAvatar().getFileUrl());
        avatar.setImageURI(imageUri);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar_account);
        toolbar.setTitle("我");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_user_logout:
                doLogout();
                break;
            case R.id.btn_user_register:
                doRegister();
                break;
            default:
                break;
        }

    }

    /**
     * 还没有账户
     */
    private void doRegister() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    private void doLogout() {
        BmobUser.logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
