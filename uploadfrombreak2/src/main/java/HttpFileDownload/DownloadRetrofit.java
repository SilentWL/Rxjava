package HttpFileDownload;

import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import Beans.DownloadInfo;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DownloadRetrofit {
    private DownloadInfo mDownloadInfo;

    private static final String TAG = "DownloadThread";

    public DownloadRetrofit(DownloadInfo downloadInfo) {
        mDownloadInfo = downloadInfo;
    }

    public void run() {
        new Retrofit.Builder()
                .baseUrl(mDownloadInfo.getUrl())
                .client(getClientForProgress())
                .build()
                .create(DownloadApi.class)
                .download(mDownloadInfo.getDownloadFileName())
                .enqueue(getCallback());
    }

    @NonNull
    private OkHttpClient getClientForProgress() {
        return new OkHttpClient
                .Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Connection", "keep-alive")
                                //.addHeader("Range", "bytes=" + + "-" + );
                        .build();
                        Response orginalResponse = chain.proceed(request);

                        return orginalResponse.newBuilder()
                                .body(new ProgressResponseBody(orginalResponse.body(), new ProgressListener() {
                                    @Override
                                    public void onProgress(long progress, long total, boolean done) {
                                        Log.w(TAG, "onProgress: " + progress);
                                    }
                                }))
                                .build();
                    }
                })
                .build();
    }

    @NonNull
    private Callback<ResponseBody> getCallback() {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(Environment.getExternalStorageDirectory(), mDownloadInfo.getFileInfo().getFileName());
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                        Log.w(TAG, "onResponse: len = " + len + " Thread Info = " + Looper.myLooper());
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.w(TAG, "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w(TAG, "failure");
            }
        };
    }
}
