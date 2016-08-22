package HttpFileDownload;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public interface DownloadApi {
    @Streaming
    @GET("{dst}")
    Observable<ResponseBody> downloadForRx(@Path("dst") String dst);
    @Streaming
    @GET("{dst}")
    Call<ResponseBody> download(@Path("dst") String dst);
}
