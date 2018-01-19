package oogh.yuanxiao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by oogh on 17-10-23.
 */

public class CetDb {

    private static final String TAG = CetDb.class.getSimpleName();
    private DbHelper mDbHelper;

    public CetDb(DbHelper helper) {
        mDbHelper = helper;
    }

    public Cursor select(String sid, int which) {
        String[] projection = new String[1];
        String selection = null;
        String tableName = null;
        switch (which) {
            case 201406:
                projection[0] = StudentContract.Entry1406.COLUMN_EID;
                selection = StudentContract.Entry1406.COLUMN_SID;
                tableName = StudentContract.Entry1406.TABLE_NAME;
                break;
            case 201412:
                projection[0] = StudentContract.Entry1412.COLUMN_EID;
                selection = StudentContract.Entry1412.COLUMN_SID;
                tableName = StudentContract.Entry1412.TABLE_NAME;
                break;
            case 201506:
                projection[0] = StudentContract.Entry1506.COLUMN_EID;
                selection = StudentContract.Entry1506.COLUMN_SID;
                tableName = StudentContract.Entry1506.TABLE_NAME;
                break;
            case 201512:
                projection[0] = StudentContract.Entry1512.COLUMN_EID;
                selection = StudentContract.Entry1512.COLUMN_SID;
                tableName = StudentContract.Entry1512.TABLE_NAME;
                break;
            case 201606:
                projection[0] = StudentContract.Entry1606.COLUMN_EID;
                selection = StudentContract.Entry1606.COLUMN_SID;
                tableName = StudentContract.Entry1606.TABLE_NAME;
                break;
            case 201612:
                projection[0] = StudentContract.Entry1612.COLUMN_EID;
                selection = StudentContract.Entry1612.COLUMN_SID;
                tableName = StudentContract.Entry1612.TABLE_NAME;
                break;
            case 201706:
                projection[0] = StudentContract.Entry1706.COLUMN_EID;
                selection = StudentContract.Entry1706.COLUMN_SID;
                tableName = StudentContract.Entry1706.TABLE_NAME;
                break;
            case 201712:
                projection[0] = StudentContract.Entry1712.COLUMN_EID;
                selection = StudentContract.Entry1712.COLUMN_SID;
                tableName = StudentContract.Entry1712.TABLE_NAME;
                break;
            default:
                break;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] selectionArgs = {sid};


        String sql = "select eid from " + tableName + " where sid=" + sid;
        Log.i(TAG, "查询语句: " + sql);
        return db.rawQuery(sql, null);
    }
}
