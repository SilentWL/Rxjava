package DBHelper;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import Beans.DownloadInfo;
import Beans.FileInfo;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
@Module
public class DBHelperModule {
    private Context mContext;
    private DBHelper mDBHelper;
    private Map<String, Dao> mDaos;
    private List<Class<? extends Object>> mlistClasses;

    public DBHelperModule(Context context){
        mContext = context;
    }

    @Provides
    @Singleton
    public Context providesContext(){
        return mContext;
    }

    @Provides
    @Singleton
    public DBHelper providesDBHelper(Context context){
        if (mDBHelper == null) {
            synchronized (DBHelperModule.class) {
                if (mDBHelper == null) {
                    mDBHelper = new DBHelper(context);
                }
            }
        }
        return mDBHelper;
    }

    @Provides
    @Singleton
    public Map<String, Dao> providesDaos(){
        if (mDaos == null){
            synchronized (DBHelperModule.class){
                if (mDaos == null){
                    mDaos = new HashMap<String, Dao>();
                }
            }
        }
        return mDaos;
    }

    @Provides
    @Singleton
    public List<Class<? extends  Object>> providesClasses(){
        if (mlistClasses == null) {
            synchronized (DBHelperModule.class) {
                if (mlistClasses == null) {
                    mlistClasses = new ArrayList<>();
                    mlistClasses.add(DownloadInfo.class);
                    mlistClasses.add(FileInfo.class);
                }
            }
        }
        return mlistClasses;
    }
}
