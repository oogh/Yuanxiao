package oogh.yuanxiao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;
import oogh.yuanxiao.activity.LoginActivity;

public class StartupActivity extends AppCompatActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            toLoginActivity();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bmob初始化
        Bmob.initialize(this, "f8f0ccd79d3af99323150fdb2aa2f1bd", "bmob");
        // Fresco初始化
        Fresco.initialize(this);
        // 百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_start_up);
        handler.sendEmptyMessageDelayed(0, 1500);





    }

    public void toLoginActivity() {
        Intent intent = new Intent(StartupActivity.this, LoginActivity.class);
        // TODO: 为了开发的方便，启动后直接跳转至MainActivity主页面
//        Intent intent = new Intent(StartupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
