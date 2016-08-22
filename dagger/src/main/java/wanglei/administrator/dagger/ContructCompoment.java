package wanglei.administrator.dagger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class ContructCompoment {
    private static DisplayComponent mDisplayComponent;

    private ContructCompoment(){

    }
    public static DisplayComponent Contruct(Application application, Context context, String displayString){
        if (mDisplayComponent == null) {
            mDisplayComponent = DaggerDisplayComponent.builder()
                    .appModule(new AppModule(context, application))
                    .toastModule(new ToastModule(displayString))
                    .build();
        }
        return mDisplayComponent;
    }

    public void Inject(MainActivity activity){
        mDisplayComponent.inject(activity);
    }
}
