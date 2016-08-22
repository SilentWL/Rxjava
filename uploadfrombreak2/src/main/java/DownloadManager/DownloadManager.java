package DownloadManager;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import Beans.DownloadInfo;
import Beans.FileInfo;
import Component.DaggerComponentConstruct;
import DBHelper.DownloadInfoDao;
import DBHelper.FileInfoDao;
import HttpFileDownload.DownloadThreadRxjava;
import HttpFileDownload.UpdateFileInfosAndDownloadInfosInterface;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class DownloadManager implements UpdateFileInfosAndDownloadInfosInterface{
    @Inject
    DownloadInfoDao mDownloadInfoDao;
    @Inject
    FileInfoDao mFileInfoDao;
    @Inject
    Map<FileInfo, DownloadThreadRxjava> mDownloadTasks;

    private static DownloadManager mDownloadManager = null;

    private DownloadManager() {
        DaggerComponentConstruct.getInstance().inject(this);
    }

    public static DownloadManager getInstance(){
        if (mDownloadManager == null){
            synchronized (DownloadManager.class){
                if (mDownloadManager == null){
                    mDownloadManager = new DownloadManager();
                }
            }
        }
        return mDownloadManager;
    }

    public void startDownloadFile(FileInfo fileInfo){
        if (fileInfo != null){
            List<FileInfo> fileInfos = mFileInfoDao.getFileInfosByUrlAndFileName(fileInfo.getUrl(), fileInfo.getFileName());
            boolean startDownloadTask = false;
            FileInfo fileInfoInDao = null;

            if (fileInfos != null && fileInfos.size() > 0){
                fileInfoInDao = fileInfos.get(0);
                if (!fileInfoInDao.isDownLoadFinished()){
                    startDownloadTask = true;
                }
            }else {
                fileInfoInDao = fileInfo;
                UpdateFileInfoInDao(fileInfo, true);
                startDownloadTask = true;
            }

            if (startDownloadTask){
                if (!mDownloadTasks.containsKey(fileInfoInDao)){
                    DownloadThreadRxjava downloadThreadRxjava = new DownloadThreadRxjava(fileInfoInDao, this);
                    mDownloadTasks.put(fileInfoInDao, downloadThreadRxjava);
                    downloadThreadRxjava.startDownload();
                }
            }
        }
    }

    public void startDownloadFiles(List<FileInfo> fileInfos){
        for (FileInfo fileInfo: fileInfos){
            startDownloadFile(fileInfo);
        }
    }
    public void stopDownloadFile(FileInfo fileInfo){
        if (mDownloadTasks.containsKey(fileInfo)){
            DownloadThreadRxjava downloadThreadRxjava = mDownloadTasks.get(fileInfo);
            downloadThreadRxjava.stopDownload();
            mDownloadTasks.remove(fileInfo);
        }
    }

    public void stopDownloadFiles(List<FileInfo> fileInfos){
        for(FileInfo fileInfo : fileInfos){
            stopDownloadFile(fileInfo);
        }
    }
    @Override
    public void UpdateFileInfoInDao(FileInfo fileInfo, boolean add) {
        if (fileInfo != null) {
            List<FileInfo> fileInfos = mFileInfoDao.getFileInfosByUrlAndFileName(fileInfo.getUrl(), fileInfo.getFileName());
            FileInfo fileInfoInDao = null;
            if (fileInfos != null && fileInfos.size() > 0){
                fileInfoInDao = fileInfos.get(0);
                mFileInfoDao.updateFileInfoById(fileInfo, fileInfoInDao.getId());
            }else {
                if (add){
                    mFileInfoDao.addFileInfo(fileInfo);
                }
            }
        }

    }

    @Override
    public void UpdateDownloadInfoInDao(DownloadInfo downloadInfo, boolean add) {
        if (downloadInfo != null){
            List<DownloadInfo> downloadInfos = mDownloadInfoDao.getDownloadInfos(downloadInfo);
            DownloadInfo downloadInfoInDao = null;

            if (downloadInfos != null && downloadInfos.size() > 0){
                downloadInfoInDao = downloadInfos.get(0);
                mDownloadInfoDao.updateDownloadInfoById(downloadInfo, downloadInfoInDao.getId());
            }else {
                if (add){
                    mDownloadInfoDao.addDownloadInfo(downloadInfo);
                }
            }
        }
    }

    @Override
    public void UpdateFileInfosInDao(List<FileInfo> fileInfos, boolean add) {
        for (FileInfo fileInfo : fileInfos){
            UpdateFileInfoInDao(fileInfo, add);
        }
    }

    @Override
    public void UpdateDownloadInfosInDao(List<DownloadInfo> downloadInfos, boolean add) {
        for(DownloadInfo downloadInfo : downloadInfos){
            UpdateDownloadInfoInDao(downloadInfo, add);
        }
    }
}
