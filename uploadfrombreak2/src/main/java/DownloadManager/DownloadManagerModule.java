package DownloadManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import Beans.FileInfo;
import HttpFileDownload.DownloadThreadRxjava;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
@Module
public class DownloadManagerModule {
    @Provides
    @Singleton
    public Map<FileInfo, DownloadThreadRxjava> providesDownloadTasksMap(){
        return new HashMap<FileInfo, DownloadThreadRxjava>();
    }

}
