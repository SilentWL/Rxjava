package Constants;

import android.os.Environment;

/**
 * Created by Administrator on 2016/8/8 0008.
 */
public class Constants {
    public static final String DOWNLOAD_FILES_DIR = Environment.getExternalStorageDirectory() + "/WL";

    public static final int DOWNLOAD_FILE_PIECES_COUNT = 3;

    public static final String DOWNLOAD_SERVICE_ACTION = "com.downloadmanager.downloadservice_action";
    public static final String DOWNLOAD_SERVICE_CMD = "com.downloadmanager.downloadservice.cmd";
    public static final String DOWNLOAD_SERVICE_START_DOWNLOAD = "com.downloadmanager.downloadservice.startdownload";
    public static final String DOWNLOAD_SERVICE_STOP_DOWNLOAD = "com.downloadmanager.downloadservice.stopdownload";
    public static final String DOWNLOAD_SERVICE_DOWNLOAD_FILEINFO_KEY = "com.downloadmanager.downloadservice.downloadinfokey";

}
