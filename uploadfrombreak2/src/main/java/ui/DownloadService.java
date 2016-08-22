package ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import Beans.FileInfo;
import Constants.Constants;
import DownloadManager.DownloadManager;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class DownloadService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(Constants.DOWNLOAD_SERVICE_DOWNLOAD_FILEINFO_KEY);

        if (intent.getStringExtra(Constants.DOWNLOAD_SERVICE_CMD).equals(Constants.DOWNLOAD_SERVICE_START_DOWNLOAD)){
            DownloadManager.getInstance().startDownloadFile(fileInfo);
        }else if (intent.getStringExtra(Constants.DOWNLOAD_SERVICE_CMD).equals(Constants.DOWNLOAD_SERVICE_STOP_DOWNLOAD)){
            DownloadManager.getInstance().stopDownloadFile(fileInfo);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
