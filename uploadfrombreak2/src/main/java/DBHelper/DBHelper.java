package DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import Component.DaggerComponentConstruct;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper{
    @Inject Map<String, Dao> mDaos;
    @Inject List<Class<? extends  Object>> mClasses;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);

        DaggerComponentConstruct.getInstance().inject(this);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            for (Class clazz : mClasses) {
                TableUtils.dropTable(connectionSource, clazz, true);

            }
            for (Class clazz : mClasses) {
                TableUtils.createTable(connectionSource, clazz);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            for (Class clazz : mClasses) {
                TableUtils.dropTable(connectionSource, clazz, true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Dao getDao(Class clazz){
        Dao dao = null;

        String className = clazz.getSimpleName();

        try {
            if (mDaos.containsKey(className)){
                dao = mDaos.get(className);
            }
            if (dao == null){
                dao = super.getDao(clazz);
                mDaos.put(className, dao);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();

        for (String key : mDaos.keySet()){
            Dao dao = mDaos.get(key);
            dao = null;
        }
    }
}
