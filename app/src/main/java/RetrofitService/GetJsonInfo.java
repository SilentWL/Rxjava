package RetrofitService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public interface GetJsonInfo {

    @GET("{dir}")
    Call<GetIpInfoResponse> getIpInfo(@Path("dir") String dir);
    @GET("{dir}/{location}")
    Call<GetIpInfoResponse> getIpInfo(@Path("dir") String dir, @Path("location") String location);
    @FormUrlEncoded
    @POST("Web/GsonServlet")
    Call<GetIpInfoResponse> postIpInfo(@Field("name") String name, @Field("age") int age);
}
