package wanglei.administrator.dagger;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class MyApp extends Application{
    private DisplayComponent mDisplayCOmponent;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public DisplayComponent getmDisplayCOmponent(){
        return mDisplayCOmponent;
    }
}
