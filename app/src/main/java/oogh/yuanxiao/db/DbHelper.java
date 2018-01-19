package oogh.yuanxiao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oogh on 17-10-23.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper sInstance;

    //单例设计模式，确保程序中只有一个Database实例对象，避免Leak错误
    public static synchronized DbHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private static final String DATABASE_NAME = "yuanxiao.db";
    private static final int DATABASE_VERSION = 1;

    // 私有化DbHelper类构造器,只通过getInstance方法获取DbHelper实例
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // 更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
