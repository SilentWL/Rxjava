package Beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
@DatabaseTable(tableName = "tb_FileInfo")
public class FileInfo implements Serializable{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private int fileLength;
    @DatabaseField
    private String url;
    @DatabaseField
    private boolean downLoadFinished;
    @ForeignCollectionField
    private Collection<DownloadInfo> downloadInfos;

    public FileInfo() {
    }

    public Collection<DownloadInfo> getDownloadInfos() {
        return downloadInfos;
    }

    public void setDownloadInfos(Collection<DownloadInfo> downloadInfos) {
        this.downloadInfos = downloadInfos;
    }

    public FileInfo(String fileName, int fileLength, boolean downLoadFinished, String url) {
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.downLoadFinished = downLoadFinished;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDownLoadFinished() {
        return downLoadFinished;
    }

    public void setDownLoadFinished(boolean downLoadFinished) {
        this.downLoadFinished = downLoadFinished;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileInfo = (FileInfo) o;

        if (fileName != null ? !fileName.equals(fileInfo.fileName) : fileInfo.fileName != null)
            return false;
        return url != null ? url.equals(fileInfo.url) : fileInfo.url == null;

    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
