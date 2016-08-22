package DBHelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.List;

import javax.inject.Inject;

import Beans.DownloadInfo;
import Component.DaggerComponentConstruct;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class DownloadInfoDao {
    @Inject
    Dao<DownloadInfo, Integer> mDownloadInfoDao;


    public DownloadInfoDao(){
        DaggerComponentConstruct.getInstance().inject(this);
    }

    public synchronized int addDownloadInfo(DownloadInfo downloadInfo){
        try {
            mDownloadInfoDao.createIfNotExists(downloadInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadInfo.getId();
    }
    public synchronized void addDownloadInfos(List<DownloadInfo> downloadInfos){
        try {
            mDownloadInfoDao.create(downloadInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized List<DownloadInfo> getDownloadInfos(DownloadInfo srcDownloadInfo){
        List<DownloadInfo> downloadInfos = null;

        try{
            QueryBuilder<DownloadInfo, Integer> queryBuilder = mDownloadInfoDao.queryBuilder();

            Where<DownloadInfo, Integer> where = queryBuilder.where();
            where.eq("url", srcDownloadInfo.getUrl());
            where.and();
            where.eq("startCursor", srcDownloadInfo.getStartCursor());
            where.and();
            where.eq("endCursor", srcDownloadInfo.getEndCursor());
            downloadInfos = where.query();
        }catch (Exception e){
            e.printStackTrace();
        }

        return downloadInfos;
    }
    public synchronized DownloadInfo getDownloadInfoById(int id){
        DownloadInfo downloadInfo = null;

        try {
            downloadInfo = mDownloadInfoDao.queryForId(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return downloadInfo;
    }
    private List<DownloadInfo> getDownloadInfosByUrl(String url) {
        List<DownloadInfo> downloadInfos = null;

        try {
            QueryBuilder<DownloadInfo, Integer> queryBuilder = mDownloadInfoDao.queryBuilder();

            Where<DownloadInfo, Integer> where = queryBuilder.where();
            where.eq("url", url);
            downloadInfos = where.query();
        }catch (Exception e){
            e.printStackTrace();
        }
        return downloadInfos;
    }
    public synchronized List<DownloadInfo> getDownloadInfos(){
        List<DownloadInfo> downloadInfos = null;

        try{
            downloadInfos = mDownloadInfoDao.queryForAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return downloadInfos;
    }

    public synchronized void removeDownloadInfoById(int id){
        try {
            mDownloadInfoDao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<DownloadInfo> removeDownloadInfos(){
        List<DownloadInfo> downloadInfos = getDownloadInfos();

        try {
            mDownloadInfoDao.delete(downloadInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadInfos;
    }

    public synchronized int removeDownloadInfo(DownloadInfo downloadInfo){
        try {
            mDownloadInfoDao.delete(downloadInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadInfo.getId();
    }

    public synchronized List<DownloadInfo> removeDownloadInfosByUrl(String url){
        List<DownloadInfo> downloadInfos = getDownloadInfosByUrl(url);

        try {
            mDownloadInfoDao.delete(downloadInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadInfos;
    }

    public synchronized void updateDownloadInfoById(DownloadInfo downloadInfo, int id){
        try {
            mDownloadInfoDao.updateId(downloadInfo, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateDownloadInfosById(List<DownloadInfo> downloadInfos){
        try {
            for (DownloadInfo downloadInfo : downloadInfos) {
                mDownloadInfoDao.updateId(downloadInfo, downloadInfo.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
