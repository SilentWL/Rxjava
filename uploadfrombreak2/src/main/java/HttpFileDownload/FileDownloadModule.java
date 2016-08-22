package HttpFileDownload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import Beans.DownloadInfo;
import Beans.FileInfo;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/8/8 0008.
 */
@Module
public class FileDownloadModule {
    private static ExecutorService mExecutorService;

    @Provides
    @Singleton
    public ExecutorService providesExecutorService(){
        if (mExecutorService == null){
            synchronized (FileDownloadModule.class){
                if (mExecutorService == null){
                    mExecutorService = Executors.newCachedThreadPool();
                }
            }
        }
        return mExecutorService;
    }

    @Provides
    public List<DownloadInfo> ProvidesDownloadInfos(){
        return new ArrayList<DownloadInfo>();
    }

    @Provides
    public List<FileInfo> ProvidesFileInfos(){
        return new ArrayList<FileInfo>();
    }

}
