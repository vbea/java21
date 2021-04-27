package com.vbea.java21;

import android.app.Application;
import android.content.Context;
//import android.support.multidex.MultiDex;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbes.util.EasyPreferences;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        // TODO: Implement this method
        super.onCreate();
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);
        builder.configFileDownloadDir(Common.getDownloadPath(this)); // 配置下载文件保存的文件夹
        builder.configDownloadTaskSize(3); // 配置同时下载任务数量，如果不配置默认为2
        builder.configRetryDownloadTimes(3); // 配置下载失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configConnectTimeout(30000); // 配置连接网络超时时间，如果不配置默认为15秒
        FileDownloader.init(builder.build());
        ExceptionHandler.getInstance().init(this);
        EasyPreferences.init(this, "java21");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(base);
        ExceptionHandler.getInstance().init(base);
        EasyPreferences.init(base, "java21");
    }
}
