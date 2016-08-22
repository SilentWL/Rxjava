package DBHelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.List;

import javax.inject.Inject;

import Beans.FileInfo;
import Component.DaggerComponentConstruct;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class FileInfoDao {
    @Inject
    Dao<FileInfo, Integer> mFileInfoDao;

    public FileInfoDao() {
        DaggerComponentConstruct.getInstance().inject(this);
    }

    public synchronized int addFileInfo(FileInfo fileInfo) {
        try {
            mFileInfoDao.createIfNotExists(fileInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo.getId();
    }
    public synchronized FileInfo getFileInfoById(int id) {
        FileInfo fileInfo = null;
        try {
            fileInfo = mFileInfoDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    public synchronized List<FileInfo> getFileInfos(FileInfo srcFileInfo) {
        List<FileInfo> fileInfos = null;

        try {
            QueryBuilder<FileInfo, Integer> queryBuilder = mFileInfoDao.queryBuilder();

            Where<FileInfo, Integer> where = queryBuilder.where();
            where.eq("url", srcFileInfo.getUrl());
            where.and();
            where.eq("fileName", srcFileInfo.getFileName());
            where.and();
            where.eq("fileLength", srcFileInfo.getFileLength());
            fileInfos = where.query();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileInfos;
    }

    public synchronized List<FileInfo> getFileInfosByUrl(String url) {
        List<FileInfo> fileInfos = null;

        try {
            QueryBuilder<FileInfo, Integer> queryBuilder = mFileInfoDao.queryBuilder();

            Where<FileInfo, Integer> where = queryBuilder.where();
            where.eq("url", url);
            fileInfos = where.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfos;

    }
    public synchronized List<FileInfo> getFileInfosByUrlAndFileName(String url, String fileName) {
        List<FileInfo> fileInfos = null;

        try {
            QueryBuilder<FileInfo, Integer> queryBuilder = mFileInfoDao.queryBuilder();

            Where<FileInfo, Integer> where = queryBuilder.where();
            where.eq("url", url);
            where.and();
            where.eq("fileName", fileName);
            fileInfos = where.query();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileInfos;
    }
    public synchronized List<FileInfo> getFileInfos() {
        List<FileInfo> fileInfos = null;

        try {
            fileInfos = mFileInfoDao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfos;
    }

    public synchronized void removeFileInfoById(int id) {
        try {
            mFileInfoDao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized int removeFileInfo(FileInfo fileInfo) {
        try {
            mFileInfoDao.delete(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo.getId();
    }

    public synchronized List<FileInfo> removeFileInfos() {
        List<FileInfo> fileInfos = getFileInfos();
        try {
            mFileInfoDao.delete(fileInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfos;
    }

    public synchronized List<FileInfo> removeFileInfosByUrl(String url) {
        List<FileInfo> fileInfos = getFileInfosByUrl(url);

        try {
            mFileInfoDao.delete(fileInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfos;
    }

    public synchronized void updateFileInfoById(FileInfo fileInfo, int id){
        try {
            mFileInfoDao.updateId(fileInfo, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
