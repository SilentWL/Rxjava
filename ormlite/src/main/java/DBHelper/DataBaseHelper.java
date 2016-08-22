package DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import bean.User;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper{
    private static final String TABLE_NAME = "sqlite-test.db";
    private static DataBaseHelper mDataBaseHelper = null;
    private Dao<User, Integer> mUserDao = null;
    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataBaseHelper getHelper(Context context){
        if (mDataBaseHelper == null){
            synchronized (DataBaseHelper.class){
                if (mDataBaseHelper == null){
                    mDataBaseHelper = new DataBaseHelper(context);
                }
            }
        }
        return mDataBaseHelper;
    }

    public Dao<User, Integer> getUserDao() throws java.sql.SQLException {
        if (mUserDao == null){
            mUserDao = getDao(User.class);
        }
        return mUserDao;
    }

    @Override
    public void close() {
        super.close();
        mUserDao = null;
    }
}
