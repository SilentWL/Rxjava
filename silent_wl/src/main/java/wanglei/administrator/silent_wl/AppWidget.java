package wanglei.administrator.silent_wl;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class AppWidget extends AppWidgetProvider{
    private final String BROADCAST_STRING = "silent.wl.appWidgetUpdate";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(BROADCAST_STRING))
        {
            //只能通过远程对象来设置appwidget中的控件状态
            RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.app_widget);

            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            //相当于获得所有本程序创建的appwidget
            ComponentName componentName = new ComponentName(context,AppWidget.class);

            //更新appwidget
            //appWidgetManager.updateAppWidget(componentName, remoteViews);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //创建一个Intent对象
        Intent intent = new Intent();
        //intent.setAction(BROADCAST_STRING);
        intent.setClass(context, MainActivity.class);


        //设置pendingIntent的作用
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.app_widget);


        //绑定事件
        remoteViews.setOnClickPendingIntent(R.id.AppWidget, pendingIntent);

        //更新Appwidget
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
