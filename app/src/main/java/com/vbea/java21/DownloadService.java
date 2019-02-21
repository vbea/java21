package com.vbea.java21;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.base.Status;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.simple.OnSimpleFileDownloadStatusListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownloadService extends Service {
    private NotificationManager notiManager;
    private int TYPE_ON = 1, TYPE_PAUSE = 2, TYPE_OTHER = 3;
    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Util.isAndroidO()) {
            NotificationChannel channel = new NotificationChannel("23", "DownloadManager", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(false);
            notiManager.createNotificationChannel(channel);
        }
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);
    }

    public void createNotification(boolean going, int type, String title, String text)
    {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.wel_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setOngoing(going);
        if (Util.isAndroidO()) {
            builder.setChannelId("23");
        }
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, DownloadBridge.class), PendingIntent.FLAG_CANCEL_CURRENT));
        notiManager.notify(type, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notiManager.cancelAll();
    }

    private String getSpeed(float speed) {
        double decimal = speed;
        String unit = "KB/s";
        if (speed > 1024) {
            decimal = speed / 1024;
            unit = "MB/s";
        }
        BigDecimal result = new BigDecimal(decimal);
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + unit;
    }

    private String getRemain(long rem) {
        long hour = 0, minute = 0, second = 0;
        if (rem > 3600) {
            hour = rem / 3600;
            long s = rem - hour * 3600;
            if (s > 60)
                minute = s / 60;
        } else if (rem > 60) {
            minute = rem / 60;
            second = rem % 60;
        } else {
            second = rem;
        }
        String result = "";
        if (hour > 0) {
            result = hour + "小时";
            if (minute > 0)
                result += minute + "分钟";
        } else if (minute > 0) {
            result += minute + "分";
            if (second > 0)
                result += second + "秒";
        } else {
            result = second + "秒";
        }
        return result;
    }

    private String getDownlading() {
        int count = 0;
        for (DownloadFileInfo info : FileDownloader.getDownloadFiles()) {
            if (info.getStatus() == Status.DOWNLOAD_STATUS_DOWNLOADING)
                count+=1;
        }
        return "正在下载" + count + "个文件";
    }

    private OnFileDownloadStatusListener mOnFileDownloadStatusListener = new OnSimpleFileDownloadStatusListener() {
        @Override
        public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) {
            // 正在重试下载（如果你配置了重试次数，当一旦下载失败时会尝试重试下载），retryTimes是当前第几次重试
        }

        @Override
        public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
            // 等待下载（等待其它任务执行完成，或者FileDownloader在忙别的操作）
        }

        @Override
        public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
            // 准备中（即，正在连接资源）
        }

        @Override
        public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
            // 已准备好（即，已经连接到了资源）
        }

        @Override
        public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) {
            // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
            notiManager.cancel(TYPE_PAUSE);
            createNotification(true, TYPE_ON, getDownlading(),  getSpeed(downloadSpeed) + "，预计需要" + getRemain(remainingTime));
        }

        @Override
        public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
            // 下载已被暂停
            notiManager.cancel(TYPE_ON);
            createNotification(false, TYPE_PAUSE, "下载已停止", downloadFileInfo.getFileName());
        }

        @Override
        public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
            // 下载完成（整个文件已经全部下载完成）
            notiManager.cancelAll();
            createNotification(false, TYPE_OTHER, "下载完成", downloadFileInfo.getFileName());
        }

        @Override
        public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, OnFileDownloadStatusListener.FileDownloadStatusFailReason failReason) {
            // 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心
            String failType = failReason.getType();
            String failMsg = downloadFileInfo.getFileName();
            if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType)) {
                failMsg = "url错误";
            } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType)) {
                failMsg = "存储空间不足";
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType)) {
                failMsg = "无法访问网络";
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType)) {
                failMsg = "连接超时";
            }
            createNotification(false, TYPE_OTHER, "下载失败", failMsg);
            ExceptionHandler.log("Service-下载失败", failReason.getLocalizedMessage() + "\n(" + url + ")\n" + "Type:" + failType);
        }
    };
}
