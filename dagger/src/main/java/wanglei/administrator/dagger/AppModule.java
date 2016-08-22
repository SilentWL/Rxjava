package wanglei.administrator.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
@Module
public class AppModule {
    private Context mContext;
    private Application mAppContext;

    public AppModule(Context context, Application appContext){
        mContext = context;
        mAppContext = appContext;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return mAppContext;
    }

    @Provides
    @Singleton
    Context providesActivity(){
        return mContext;
    }
}
