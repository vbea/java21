package com.vbea.java21.update;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.NotificationManagerCompat;

import com.vbea.java21.ActivityManager;
import com.vbea.java21.MainActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyAlertDialog;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.AppVersion;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.util.The;

/**
 * Created by Vbe on 2018/12/6.
 */
public class MyUpdateAgent {
    private Activity activity;
    private Context context;
    private boolean isExist = false;
    private String filename = "";
    private NotificationManager notificationManager;
    private int percents = -1;

    private MyUpdateAgent(Activity a) {
        activity = a;
        context = a.getApplicationContext();
        //notificationManager = NotificationManagerCompat.from(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Util.isAndroidO()) {
            NotificationChannel channel = new NotificationChannel("21", "Java21", NotificationManager.IMPORTANCE_LOW);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
        queryData();
    }

    //入口
    public static void update(Activity activity) {
        new MyUpdateAgent(activity);
    }

    private void queryData() {
        BmobQuery query = new BmobQuery();
        query.addWhereEqualTo("platform", "Android");
        query.addWhereEqualTo("channel", "app");
        query.addWhereGreaterThan("version_i", The.Code());
        query.order("-version_i");
        query.findObjects(new FindListener<AppVersion>() {
            public final void done(List<AppVersion> objects, BmobException e) {
                if (e == null) {
                    if (objects != null && objects.size() > 0) {
                        AppVersion appVersion = objects.get(0);
                        if (appVersion != null) {
                            showUpdateDialog(appVersion);
                        }
                    }
                } else {
                    ExceptionHandler.log("updateFailed", e);
                }
            }
        });
    }

    private void showUpdateDialog(final AppVersion appVersion) {
        UpdateResponse response = new UpdateResponse(appVersion);
        if (response.target_size <= 0L) {
            Util.toastShortMessage(context,"target_size为空或格式不对，请填写apk文件大小(long类型)。");
            return;
        }

        if (Util.isNullOrEmpty(response.path)) {
            Util.toastShortMessage(context, "path/android_url需填写其中之一。");
            return;
        }

        filename = response.path_md5 + ".apk";
        File file = new File(Common.getUpdatePath(), filename);
        if (file.exists() && file.length() == response.target_size) {
            isExist = true;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.BMNewVersion));
        builder.append(response.version);
        builder.append("\n");
        builder.append(context.getString(R.string.BMTargetSize));
        builder.append(Util.getFormatSize(response.target_size));
        builder.append("\n\n");
        builder.append(context.getString(R.string.BMUpdateContent));
        builder.append("\n");
        builder.append(response.updateLog);
        if (isExist) {
            builder.append("\n\n");
            builder.append(context.getString(R.string.bmob_common_silent_download_finish));
        }
        builder.append("\n\n");
        MyAlertDialog dialog = new MyAlertDialog(activity);
        dialog.setTitle(R.string.BMUpdateTitle);
        dialog.setMessage(builder.toString());
        dialog.setNegativeButton(R.string.BMNotNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (response.isforce) {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    activity.startActivity(home);
                    activity.finish();
                    ActivityManager.getInstance().FinishAllActivities();
                }
            }
        });
        dialog.setPositiveButton(R.string.BMUpdateNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isExist) {
                    installApk(file);
                } else {
                    downloadApk(appVersion.getPath(), file);
                }
            }
        });
        if (response.isforce) {
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    private void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String mime = Util.getMimeType(filename);
        if (Util.isAndroidN())
        {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        intent.setDataAndType(Uri.fromFile(file), mime);
        activity.startActivity(intent);
    }

    private void downloadApk(BmobFile bmobFile, File file) {
        if (bmobFile != null) {
            bmobFile.download(file, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    ExceptionHandler.log("done", s);
                    notificationManager.cancelAll();
                    installApk(file);
                }

                @Override
                public void onProgress(Integer integer, long l) {
                    int ds = integer / 10;
                    if (percents != ds) {
                        createNotification(context.getString(R.string.bmob_common_action_info_exist) + integer + "%");
                        percents = ds;
                    }
                }
            });
        }
    }

    private void createNotification(String percent)
    {
        Notification.Builder builder = new Notification.Builder(context);
        if (Util.isAndroidO()) {
            builder.setChannelId("21");
        }
        builder.setSmallIcon(R.mipmap.wel_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(context.getString(R.string.bmob_common_start_patch_notification));
        builder.setContentText(percent);
        builder.setOngoing(false);
        //builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
        notificationManager.notify(0, builder.build());
    }
}
