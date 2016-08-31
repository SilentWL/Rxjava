package wanglei.administrator.silent_wl;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class MeasureUtils {
    public static int dp2px(Resources resources, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
