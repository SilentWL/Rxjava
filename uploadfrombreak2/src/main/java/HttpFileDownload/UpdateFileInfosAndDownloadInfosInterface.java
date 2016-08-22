package HttpFileDownload;

import java.io.File;
import java.util.List;

import Beans.DownloadInfo;
import Beans.FileInfo;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public interface UpdateFileInfosAndDownloadInfosInterface {
    void UpdateFileInfoInDao(FileInfo fileInfo, boolean add);
    void UpdateDownloadInfoInDao(DownloadInfo downloadInfo, boolean add);
    void UpdateFileInfosInDao(List<FileInfo> fileInfos, boolean add);
    void UpdateDownloadInfosInDao(List<DownloadInfo> downloadInfos, boolean add);
}
