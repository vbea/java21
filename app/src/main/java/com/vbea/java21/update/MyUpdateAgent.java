package com.vbea.java21.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;

import com.vbea.java21.ui.ActivityManager;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbes.util.VbeUtil;
import com.vbes.util.view.MyAlertDialog;

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
    private UpdateResponse response;

    private MyUpdateAgent(Activity a) {
        activity = a;
        context = a.getApplicationContext();
        //notificationManager = NotificationManagerCompat.from(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Util.isAndroidO()) {
            NotificationChannel channel = new NotificationChannel("21", "UpdateAgent", NotificationManager.IMPORTANCE_LOW);
            channel.enableVibration(false);
            channel.enableLights(false);
            notificationManager.createNotificationChannel(channel);
        }
        queryData();
    }

    //入口
    public static void update(Activity activity) {
        new MyUpdateAgent(activity);
    }

    private void queryData() {
        /*BmobQuery<AppVersion> query = new BmobQuery<>();
        query.addWhereEqualTo("platform", "Android");
        query.addWhereEqualTo("channel", "app");
        query.addWhereGreaterThan("version_i", getVersionCode());
        query.order("-version_i");
        query.findObjects(new FindListener<AppVersion>() {
            public final void done(List<AppVersion> objects, BmobException e) {
                if (e == null) {
                    if (objects != null && objects.size() > 0) {
                        AppVersion appVersion = objects.get(0);
                        if (appVersion != null) {
                            response = new UpdateResponse(appVersion);
                            showUpdateDialog();
                        }
                    }
                } else {
                    ExceptionHandler.log("updateFailed", e);
                }
            }
        });*/
    }

    private void showUpdateDialog() {
        if (response == null) return;
        if (response.target_size <= 0L) {
            Util.toastShortMessage(context,"target_size为空或格式不对，请填写apk文件大小(long类型)。");
            return;
        }

        if (response.path == null) {
            Util.toastShortMessage(context, "更新文件不存在");
            return;
        }

        filename = response.path_md5 + ".apk";
        File dist = new File(Common.getUpdatePath(context));
        if (!dist.exists()) dist.mkdirs();
        final File file = new File(dist, filename);
        if (file.exists() && file.length() == response.target_size) {
            isExist = true;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.BMNewVersion));
        builder.append(response.version);
        builder.append("\n");
        builder.append(context.getString(R.string.BMTargetSize));
        builder.append(response.format_size);
        builder.append("\n\n");
        builder.append(context.getString(R.string.BMUpdateContent));
        builder.append("\n");
        builder.append(response.updateLog);
        if (isExist) {
            builder.append("\n\n");
            builder.append(context.getString(R.string.bmob_common_silent_download_finish));
        }
        builder.append("\n");
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
                    response.appVersion.addInstall();
                    response.appVersion.update(null);
                    VbeUtil.installAPK(context, file, Common.FileProvider);
                } else {
                    downloadApk(response.path, file);
                }
            }
        });
        if (response.isforce) {
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    private void installApk(File file) {
        response.appVersion.addInstall();
        //response.appVersion.update(null);
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

    private void downloadApk(final String url, final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getNetFile(url, file, new OnUpdateAgent() {
                    @Override
                    public void done() {
                        notificationManager.cancelAll();
                        response.appVersion.addDownload();
                        response.appVersion.update(null);
                        if (file.exists()) {
                            response.appVersion.addInstall();
                            response.appVersion.update(null);
                            VbeUtil.installAPK(context, file, Common.FileProvider);
                        }
                    }

                    @Override
                    public void onProgress(long l) {
                        int per = (int)(l * 100 / response.target_size);
                        int per1 = per / 5;
                        if (percents != per1) {
                            createNotification(context.getString(R.string.bmob_common_action_info_exist) + per + "%");
                            percents = per1;
                        }
                    }
                });
            }
        }).start();
    }

    /*private void downloadApk(BmobFile bmobFile, final File file) {
        if (bmobFile != null) {
            bmobFile.download(file, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    //ExceptionHandler.log("done", s);
                    notificationManager.cancelAll();
                    installApk(file);
                }

                @Override
                public void onProgress(Integer integer, long l) {
                    int ds = integer / 5;
                    if (percents != ds) {
                        createNotification(context.getString(R.string.bmob_common_action_info_exist) + integer + "%");
                        percents = ds;
                    }
                }
            });
        }
    }*/

    private void createNotification(String percent) {
        Notification.Builder builder = new Notification.Builder(context);
        if (Util.isAndroidO()) {
            builder.setChannelId("21");
        }
        builder.setSmallIcon(R.drawable.wel_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(context.getString(R.string.bmob_common_download_notification_prefix));
        builder.setContentText(percent);
        builder.setOngoing(false);
        //builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
        notificationManager.notify(0, builder.build());
    }

    private int getVersionCode() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            ExceptionHandler.log("update:getVersionCode", e);
        }
        return 0;
    }

    private void getNetFile(String uri, File file, OnUpdateAgent agent) {
        // 下载网络上的文件
        try {
            URL myFileUrl = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int length = 0;
            long total = 0L;
            while((length = is.read(b)) > 0) {
                fos.write(b,0, length);
                total += length;
                agent.onProgress(total);
            }
            is.close();
            fos.close();
        } catch (OutOfMemoryError | IOException e) {
            e.printStackTrace();
        } finally {
            agent.done();
        }
    }

    interface OnUpdateAgent {
        void done();
        void onProgress(long l);
    }
}
