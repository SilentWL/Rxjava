package wanglei.administrator.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import RetrofitService.GetIpInfoResponse;
import RetrofitService.GetJsonInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class MainActivity extends Activity{
    private TextView textView;
    String[] strings = new String[]{"Hello", "World", "wl", "ym"};
    List<String> stringList = new ArrayList<String>();
    private static final String ENDPOINT = "http://192.168.199.18:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.Hello);
        stringList.add("Hello");
        stringList.add("World");
        stringList.add("wl");
        stringList.add("ym");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT).addConverterFactory(GsonConverterFactory.create()).build();
        final GetJsonInfo getJsonInfo = retrofit.create(GetJsonInfo.class);
        //Call<GetIpInfoResponse> call = getJsonInfo.getIpInfo("Web", "GsonServlet");
        Call<GetIpInfoResponse> call = getJsonInfo.postIpInfo( "wl", 18);

        call.enqueue(new Callback<GetIpInfoResponse>() {
            @Override
            public void onResponse(Call<GetIpInfoResponse> call, Response<GetIpInfoResponse> response) {
                GetIpInfoResponse getInInfoResponse = response.body();
                textView.setText(response.raw().toString());
            }

            @Override
            public void onFailure(Call<GetIpInfoResponse> call, Throwable t) {
                textView.setText(call.toString());

            }
        });


//        Observable.just("1", "2", "2", "3", "4", "5")
//        .map(new Func1<String, Integer>(){
//            @Override
//            public Integer call(String s) {
//                return Integer.parseInt(s);
//            }
//        })
//        .filter(new Func1<Integer, Boolean>() {
//            @Override
//            public Boolean call(Integer integer) {
//                return integer > 1;
//            }
//        })
//        .distinct()
//        .take(3)
//        .reduce(new Func2<Integer, Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer, Integer integer2) {
//                return integer + integer2;
//            }
//        })
//        .subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                System.out.println("integer = " + integer);
//            }
//        });



//        Observable<String> myObservable = Observable.from(stringList);
//
//        myObservable.map(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                return s + "!";
//            }
//        })
//
//        .subscribe(new Action1<String>() {
//               @Override
//               public void call(String s) {
//
//               }
//        });



//        Observable<List<String>> myObservalbe = Observable.create(new Observable.OnSubscribe<List<String>>() {
//            @Override
//            public void call(Subscriber<? super List<String>> subscriber) {
//                subscriber.onNext(stringList);
//            }
//        });
//        myObservalbe.flatMap(new Func1<List<String>, Observable<String>>(){
//            @Override
//            public Observable<String> call(List<String> strings) {
//                return Observable.from(strings);
//            }
//        }).take(2).filter(new Func1<String, Boolean>() {
//            @Override
//            public Boolean call(String s) {
//                return s != "Hello";
//            }
//        }).doOnNext(new Action1<String>() {
//            @Override
//            public void call(String s) {
//
//            }
//        }).subscribe(new Action1<String>() {
//           @Override
//           public void call(String s) {
//               textView.setText(s);
//           }
//       });


//        Observable<List<String>> myObservalbe = Observable.create(new Observable.OnSubscribe<List<String>>() {
//            @Override
//            public void calString> call(List<String> strings) {
//                return Observable.from(strings);
//            }l(Subscriber<? super List<String>> subscriber) {
//                subscriber.onNext(stringList);
//            }
//        });
//        myObservalbe.flatMap(new Func1<List<String>, Observable<String>>(){
//            @Override
//            public Observable<
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                textView.setText(s);
//            }
//        });



//        Observable<List<String>> myObservalbe = Observable.create(new Observable.OnSubscribe<List<String>>() {
//            @Override
//            public void call(Subscriber<? super List<String>> subscriber) {
//                subscriber.onNext(stringList);
//            }
//        });
//        myObservalbe.subscribe(new Action1<List<String>>(){
//            @Override
//            public void call(List<String> strings) {
//                Observable.from(strings).subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        textView.setText(s);
//                    }
//                });
//            }
//        });

//        Observable<String> myObservalbe = Observable.from(strings);
//        myObservalbe.subscribe(new Action1<String>(){
//            @Override
//            public void call(String s) {
//                textView.setText(s);
//            }
//        });


//        Observable<List<String>> myObservalbe = Observable.create(new Observable.OnSubscribe<List<String>>() {
//            @Override
//            public void call(Subscriber<? super List<String>> subscriber) {
//                subscriber.onNext(stringList);
//            }
//        });
//        myObservalbe.subscribe(new Action1<List<String>>(){
//            @Override
//            public void call(List<String> strings) {
//                for (String s : strings) {
//                    textView.setText(s);
//                }
//            }
//        });


//        Observable myObservable = Observable.just("Hello");
//        myObservable.map(new Func1<String, String>(){
//            @Override
//            public String call(String s) {
//                return s + "World";
//            }
//        }).subscribe(new Action1<String>(){
//            @Override
//            public void call(String s) {
//                textView.setText(s);
//            }
//        });




//        Observable myObservable = Observable.just("Hello");
//        myObservable.subscribe(new Action1<String>(){
//            @Override
//            public void call(String s) {
//                textView.setText(s);
//            }
//        });




//        Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onCompleted();
//            }
//        });
//
//        Subscriber<String> mySubscriber = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                textView.setText(s);
//            }
//        };
//        myObservable.subscribe(mySubscriber);
    }
}
