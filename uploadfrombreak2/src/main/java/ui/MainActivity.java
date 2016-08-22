package ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import Beans.FileInfo;
import Component.DaggerComponentConstruct;
import Constants.Constants;
import DBHelper.DownloadInfoDao;
import DBHelper.FileInfoDao;
import HttpFileDownload.DownloadThreadRxjava;
import wanglei.administrator.uploadfrombreak2.R;


public class MainActivity extends AppCompatActivity {
    @Inject
    FileInfoDao fileInfoDao;
    @Inject
    DownloadInfoDao downloadInfoDao;

    private static final String TAG = "MainActivity";

    private static final String URL1 = "http://192.168.199.18:8080/Web/";
    DownloadThreadRxjava downloadThreadRxjava;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerComponentConstruct.getInstance().inject(this);

        final FileInfo fileInfo = new FileInfo("abc.mp", 3933721, false, URL1);
        //DownloadInfo downloadInfo = new DownloadInfo(URL1, 0, 200, 0);
        //downloadInfo.setFileInfo(fileInfo);

        //Collection<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
        //downloadInfos.add(downloadInfo);
        //fileInfo.setDownloadInfos(downloadInfos);
        fileInfoDao.removeFileInfos();
        downloadInfoDao.removeDownloadInfos();
        fileInfoDao.addFileInfo(fileInfo);
        //downloadInfoDao.addDownloadInfo(downloadInfo);

        Log.w(TAG, " Thread Info = " + Looper.myLooper() + " Thread Info = " + Thread.currentThread().getId());
        //downloadThreadRxjava = new DownloadThreadRxjava(fileInfo);

        ((Button)findViewById(R.id.stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //intent.setClassName("wanglei.administrator.uploadfrombreak2", "ui.DownloadManager.DownloadService");
                intent.setClass(MainActivity.this, DownloadService.class);
                //intent.setAction(Constants.DOWNLOAD_SERVICE_ACTION);

                boolean i = true;
                if (i){
                    intent.putExtra(Constants.DOWNLOAD_SERVICE_CMD, Constants.DOWNLOAD_SERVICE_START_DOWNLOAD);

                    intent.putExtra(Constants.DOWNLOAD_SERVICE_DOWNLOAD_FILEINFO_KEY, fileInfo);
                }else {
                    intent.putExtra(Constants.DOWNLOAD_SERVICE_CMD, Constants.DOWNLOAD_SERVICE_STOP_DOWNLOAD);

                    intent.putExtra(Constants.DOWNLOAD_SERVICE_DOWNLOAD_FILEINFO_KEY, fileInfo);

                }
                i = !i;
                MainActivity.this.startService(intent);

            }
        });

        ((Button)findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //intent.setClassName("wanglei.administrator.uploadfrombreak2", "ui.DownloadManager.DownloadService");
                intent.setClass(MainActivity.this, DownloadService.class);
                //intent.setAction(Constants.DOWNLOAD_SERVICE_ACTION);

                boolean i = false;
                if (i){
                    intent.putExtra(Constants.DOWNLOAD_SERVICE_CMD, Constants.DOWNLOAD_SERVICE_START_DOWNLOAD);

                    intent.putExtra(Constants.DOWNLOAD_SERVICE_DOWNLOAD_FILEINFO_KEY, fileInfo);
                }else {
                    intent.putExtra(Constants.DOWNLOAD_SERVICE_CMD, Constants.DOWNLOAD_SERVICE_STOP_DOWNLOAD);

                    intent.putExtra(Constants.DOWNLOAD_SERVICE_DOWNLOAD_FILEINFO_KEY, fileInfo);

                }
                i = !i;
                MainActivity.this.startService(intent);

            }
        });
//        Log.w("MainActivity", "start---->" + "FileInfo: " + fileInfoDao.getFileInfos());
//        Log.w("MainActivity", "start---->" + "FileInfo: " + downloadInfoDao.getDownloadInfos());
//        fileInfoDao.removeFileInfos();
//        downloadInfoDao.removeDownloadInfos();
//        Log.w("MainActivity", "FileInfo: " + fileInfoDao.getFileInfoById(2));
//        Log.w("MainActivity", "FileInfo: " + downloadInfoDao.getDownloadInfoById(1));
//
//
//        ExecutorService executorService = Executors.newCachedThreadPool();
//
//        for (int i = 0; i < 100; i++){
//            final int j = i;
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    fileInfoDao.addFileInfo(new FileInfo("abc", 100, true, "http://baidu.com"));
//                    downloadInfoDao.addDownloadInfo(new DownloadInfo("http://baidu.com", 0, 100, 0));
//                    Log.w("MainActivity", "i = " + j + "FileInfo: " + fileInfoDao.getFileInfos());
//                    Log.w("MainActivity", "i = " + j + "FileInfo: " + downloadInfoDao.getDownloadInfos());
//                }
//            });
//        }
    }


}
