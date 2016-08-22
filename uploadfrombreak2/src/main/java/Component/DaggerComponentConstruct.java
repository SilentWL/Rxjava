package Component;

import DBHelper.BeansDaoModule;
import DBHelper.DBHelperModule;
import ui.UploadFromBreakApplication;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class DaggerComponentConstruct {
    private static DaggerComponent mDaggerComponent;

    public static DaggerComponent getInstance(){
        if (mDaggerComponent == null) {
            synchronized (DaggerComponentConstruct.class) {
                if (mDaggerComponent == null) {
                    mDaggerComponent = DaggerDaggerComponent.builder().dBHelperModule(new DBHelperModule(UploadFromBreakApplication.getApplication())).beansDaoModule(new BeansDaoModule()).build();
                }
            }
        }
        return mDaggerComponent;
    }
}
