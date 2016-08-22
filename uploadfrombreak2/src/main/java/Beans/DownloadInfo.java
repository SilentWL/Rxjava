package Beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
@DatabaseTable(tableName = "tb_DownloadInfo")
public class DownloadInfo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String url;
    @DatabaseField
    private int startCursor;
    @DatabaseField
    private int endCursor;
    @DatabaseField
    private int currentCursor;
    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
    private FileInfo fileInfo;

    public DownloadInfo() {
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public DownloadInfo(String url, int startCursor, int endCursor, int currentCursor) {
        this.url = url;
        this.startCursor = startCursor;
        this.endCursor = endCursor;
        this.currentCursor = currentCursor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentCursor() {
        return currentCursor;
    }

    public void setCurrentCursor(int currentCursor) {
        this.currentCursor = currentCursor;
    }

    public int getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(int endCursor) {
        this.endCursor = endCursor;
    }

    public int getStartCursor() {
        return startCursor;
    }

    public void setStartCursor(int startCursor) {
        this.startCursor = startCursor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadFileName(){
        return fileInfo == null ? null : fileInfo.getFileName();
    }
}
