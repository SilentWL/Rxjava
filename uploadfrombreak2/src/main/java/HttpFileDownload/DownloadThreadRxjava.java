package HttpFileDownload;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import Beans.DownloadInfo;
import Beans.FileInfo;
import Component.DaggerComponentConstruct;
import Constants.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DownloadThreadRxjava {
    @Inject
    ExecutorService mThreadPool;
    @Inject
    List<DownloadInfo> mDownloadInfos;


    private FileInfo mFileInfo;
    private boolean mIsStopThread = true;
    private UpdateFileInfosAndDownloadInfosInterface mUpdateFileInfosAndDownloadInfosInterface = null;

    private static final String TAG = "DownloadThread";

    public DownloadThreadRxjava(FileInfo fileInfo, UpdateFileInfosAndDownloadInfosInterface updateFileInfosAndDownloadInfosInterface) {
        mFileInfo = fileInfo;
        mIsStopThread = true;
        mUpdateFileInfosAndDownloadInfosInterface = updateFileInfosAndDownloadInfosInterface;
        DaggerComponentConstruct.getInstance().inject(this);
    }

    public void stopDownload(){
        if (mFileInfo != null) {
            this.mIsStopThread = true;
        }
    }
    public void startDownload() {

        if (mFileInfo != null) {
            this.mIsStopThread = false;

            Collection<DownloadInfo> downloadInfos = mFileInfo.getDownloadInfos();

            if (downloadInfos.size() > 0){
                for (DownloadInfo downloadInfo : downloadInfos){
                    if (downloadInfo.getEndCursor() - downloadInfo.getStartCursor() + 1 > downloadInfo.getCurrentCursor()){
                        startDownloadTask(downloadInfo);
                    }
                }
            }else {
                loadFileInfoAndStartDownloadTask();
            }
        }

    }
    public boolean getDownloadStatus(){
        return !this.mIsStopThread;
    }

    private void loadFileInfoAndStartDownloadTask() {
        new Retrofit.Builder()
                //Get file length.
                .baseUrl(mFileInfo.getUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(DownloadApi.class)
                .downloadForRx(mFileInfo.getFileName())
                .map(new Func1<ResponseBody, Long>() {
                    @Override
                    public Long call(ResponseBody responseBody) {
                        return responseBody.contentLength();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                //Extract file to pieces.
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Long, Observable<DownloadInfo>>() {
                    @Override
                    public Observable<DownloadInfo> call(Long fileSize) {
                        int sizePerPiece = (int) (fileSize / Constants.DOWNLOAD_FILE_PIECES_COUNT + 1);
                        for (int i = 0; i < Constants.DOWNLOAD_FILE_PIECES_COUNT; i++) {
                            DownloadInfo downloadInfo = new DownloadInfo(mFileInfo.getUrl(), sizePerPiece * i, sizePerPiece * (i + 1) - 1, 0);
                            downloadInfo.setFileInfo(mFileInfo);
                            mDownloadInfos.add(downloadInfo);
                        }
                        mDownloadInfos.get(mDownloadInfos.size() - 1).setEndCursor((int) (fileSize - 1));

                        mFileInfo.setFileLength((int)(fileSize + 0));
                        mFileInfo.setDownloadInfos(mDownloadInfos);
                        mUpdateFileInfosAndDownloadInfosInterface.UpdateFileInfoInDao(mFileInfo, false);
                        mUpdateFileInfosAndDownloadInfosInterface.UpdateDownloadInfosInDao(mDownloadInfos, true);

                        return Observable.from(mDownloadInfos);
                    }
                })
                //Open download task.
                .doOnNext(new Action1<DownloadInfo>(){
                    @Override
                    public void call(DownloadInfo downloadInfo){
                        startDownloadTask(downloadInfo);
                    }
                })
                .subscribe();
    }

    private void startDownloadTask(DownloadInfo downloadInfo) {
        mThreadPool.execute(new DownloadTask(downloadInfo));
    }

    private class DownloadTask extends Thread {
        private DownloadInfo mDownloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.mDownloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            new Retrofit.Builder()
                    .baseUrl(mDownloadInfo.getUrl())
                    .client(getClientForProgress(mDownloadInfo))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create(DownloadApi.class)
                    .downloadForRx(mDownloadInfo.getDownloadFileName())
                    .map(new Func1<ResponseBody, InputStream>() {
                        @Override
                        public InputStream call(ResponseBody responseBody) {
                            return responseBody.byteStream();
                        }
                    })
                    .doOnNext(new Action1<InputStream>() {
                        @Override
                        public void call(InputStream inputStream) {
                            writeFileFromInputStream(inputStream, mDownloadInfo);
                        }
                    })
                    .subscribe();
        }

        private void writeFileFromInputStream(InputStream inputStream, DownloadInfo dowloadInfo) {
            RandomAccessFile randomAccessFile = null;
            try {
                File parentDir = new File(Constants.DOWNLOAD_FILES_DIR);

                if (!parentDir.exists()) {
                    parentDir.mkdir();
                }

                File downLoadFile = new File(parentDir, dowloadInfo.getDownloadFileName());

                randomAccessFile = new RandomAccessFile(downLoadFile, "rwd");
                randomAccessFile.seek(dowloadInfo.getStartCursor() + dowloadInfo.getCurrentCursor());

                byte[] buffer = new byte[1024 * 1024];
                int readLen = -1;
                int total = 0;
                while (!mIsStopThread && (readLen = inputStream.read(buffer)) != -1) {
                    total += readLen;
                    dowloadInfo.setCurrentCursor(total);
                    randomAccessFile.write(buffer, 0, readLen);
                    Log.w(TAG, "onResponse: percent = " + ((long)(dowloadInfo.getCurrentCursor()) * 100 / (dowloadInfo.getEndCursor() - dowloadInfo.getStartCursor() + 1)) + " Thread Info = " + Thread.currentThread().getId());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                int totalLen = 0;

                for (DownloadInfo downloadInfo : mDownloadInfos) {
                    totalLen += downloadInfo.getStartCursor();
                }

                if (totalLen == mFileInfo.getFileLength()) {
                    mFileInfo.setDownLoadFinished(true);
                }
                mUpdateFileInfosAndDownloadInfosInterface.UpdateDownloadInfoInDao(dowloadInfo,false);
                mUpdateFileInfosAndDownloadInfosInterface.UpdateFileInfoInDao(mFileInfo, false);
                try {
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.w(TAG, "success");
        }

        @NonNull
        private OkHttpClient getClientForProgress(final DownloadInfo downloadInfo) {
            return new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("Connection", "keep-alive")
                                    .addHeader("range", "bytes=" + (downloadInfo.getStartCursor() + downloadInfo.getCurrentCursor()) + "-" + downloadInfo.getEndCursor())
                                    .build();
                            Response orginalResponse = chain.proceed(request);

                            return orginalResponse.newBuilder()
                                    .body(new ProgressResponseBody(orginalResponse.body(), new ProgressListener() {
                                        @Override
                                        public void onProgress(long progress, long total, boolean done) {
                                            //Log.w(TAG, "onProgress: " + (progress * 100 / total) + " Thread Info = " + Thread.currentThread().getId());
                                        }
                                    }))
                                    .build();
                        }
                    })
                    .build();
        }
    }
}
