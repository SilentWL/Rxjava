package DBHelper;

import com.j256.ormlite.dao.Dao;

import javax.inject.Singleton;

import Beans.DownloadInfo;
import Beans.FileInfo;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
@Module
public class BeansDaoModule {
    private FileInfoDao mFileInfoDao;
    private DownloadInfoDao mDownloadInfoDao;

    @Provides
    @Singleton
    public Dao<DownloadInfo, Integer> providesDownloadInfoTableDao(DBHelper dbHelper){
        return dbHelper.getDao(DownloadInfo.class);
    }

    @Provides
    @Singleton
    public Dao<FileInfo, Integer> providesFileInfoTableDao(DBHelper dbHelper){
        return dbHelper.getDao(FileInfo.class);
    }

    @Provides
    @Singleton
    public DownloadInfoDao providesDownloadInfoDao(){
        if (mDownloadInfoDao == null){
            synchronized (BeansDaoModule.class){
                if (mDownloadInfoDao == null){
                    mDownloadInfoDao = new DownloadInfoDao();
                }
            }
        }
        return mDownloadInfoDao;
    }

    @Provides
    @Singleton
    public FileInfoDao providesFileInfoDao(){
        if (mFileInfoDao == null){
            synchronized (BeansDaoModule.class){
                if (mFileInfoDao == null){
                    mFileInfoDao = new FileInfoDao();
                }
            }
        }
        return mFileInfoDao;
    }
}
