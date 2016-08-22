package wanglei.administrator.dagger;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
@Singleton
@Component(modules = {AppModule.class, ToastModule.class})
public interface DisplayComponent {
    void inject(MainActivity mainActivity);
    Context providesContext();
}
