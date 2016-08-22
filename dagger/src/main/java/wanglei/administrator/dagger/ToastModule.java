package wanglei.administrator.dagger;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
@Module
public class ToastModule {
    private String mDisplayMessage;

    public ToastModule(String DisplayMessage){
        mDisplayMessage = DisplayMessage;
    }

    @Provides    @Named("activity")
    @Singleton
    Toast providesActivityToast(Context context){
        return Toast.makeText(context, mDisplayMessage, Toast.LENGTH_LONG);
    }

    @Provides     @Named("application")
    @Singleton
    Toast providesApplicationToast(Application application){
        return Toast.makeText(application.getApplicationContext(), mDisplayMessage, Toast.LENGTH_LONG);
    }
}
