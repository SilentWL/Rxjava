package wanglei.administrator.silent_wl;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class KeepScreenOn {

    private static PowerManager.WakeLock mWakeLock;

    /**
     * 保持屏幕唤醒状态（即背景灯不熄灭）
     * @param on 是否唤醒
     */
    public static void keepScreenOn(Context context, boolean on) {
        if (on) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "==KeepScreenOn==");
            mWakeLock.acquire();
        }else {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
