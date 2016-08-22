package Component;

import javax.inject.Singleton;

import DBHelper.BeansDaoModule;
import DBHelper.DBHelper;
import DBHelper.DBHelperModule;
import DBHelper.DownloadInfoDao;
import DBHelper.FileInfoDao;
import DownloadManager.DownloadManager;
import HttpFileDownload.DownloadThreadRxjava;
import HttpFileDownload.FileDownloadModule;
import DownloadManager.DownloadManagerModule;
import dagger.Component;
import ui.MainActivity;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
@Singleton
@Component(modules = {DBHelperModule.class, BeansDaoModule.class, FileDownloadModule.class, DownloadManagerModule.class})
public interface DaggerComponent {
    void inject(MainActivity  mainActivity);
    void inject(DownloadInfoDao downloadInfoDao);
    void inject(FileInfoDao fileInfoDao);
    void inject(DBHelper dbHelper);
    void inject(DownloadThreadRxjava downloadThreadRxjava);
    void inject(DownloadManager downloadManager);
}
