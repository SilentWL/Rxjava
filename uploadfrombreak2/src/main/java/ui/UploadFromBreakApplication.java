package ui;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class UploadFromBreakApplication extends Application{
    private static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getApplication(){
        return application;
    }
}
