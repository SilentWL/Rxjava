package DBHelper;

import javax.inject.Singleton;

import HttpFileDownload.DownloadThreadRxjava;
import dagger.Component;
import ui.MainActivity;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
@Singleton
@Component(modules = {DBHelperModule.class, BeansDaoModule.class})
public interface DBHelperComponent{
    void inject(MainActivity  mainActivity);
    void inject(DownloadInfoDao  downloadInfoDao);
    void inject(FileInfoDao  fileInfoDao);
    void inject(DBHelper  dbHelper);
}
