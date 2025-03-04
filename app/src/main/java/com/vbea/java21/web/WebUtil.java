package com.vbea.java21.web;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;

import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.ui.HtmlViewer;
import com.vbes.util.VbeUtil;
import com.vbes.util.lis.DialogResult;

import org.wlf.filedownloader.DownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
import org.wlf.filedownloader.util.UrlUtil;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Vbe on 2021/3/11.
 */
public class WebUtil {
    public static boolean isUrl(String url) {
        Pattern pat = Pattern.compile("(http|https|file|ftp)+[://]+(\\S+\\.)+[\\w-]+(/[\\w-_./?%&=]*)?.*");
        Matcher mat = pat.matcher(url);
        return mat.matches();
    }

    public static String getDownloadFileName(String disposition, String url) {
        if (!VbeUtil.isNullOrEmpty(disposition)) {
            int index = disposition.indexOf("filename=");
            if (index >= 0) {
                String name = disposition.substring(index + 9);
                name = name.replaceAll("\"", "");
                return name;
            }
        }
        Pattern pattern = Pattern.compile("\"(.*)\"");
        Matcher matcher = pattern.matcher(disposition);
        while (matcher.find()) {
            return Util.trim(matcher.group(), '"');
        }
        String uri = url.substring(url.lastIndexOf("/") + 1);
        //修复浏览器下载文件时文件名匹配失败的BUG
        //String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        //Pattern pat = Pattern.compile("[^\\/]+[\\.](" + suffixes + ")");
        Pattern pat = Pattern.compile("[^\\/]+[\\.]+[^\\?\\:\\/\\*\\|]+");
        matcher = pat.matcher(uri);
        while (matcher.find()) {
            return matcher.group();
        }
        return disposition;
    }

    public static String validDownloadFileName(String filename) {
        int idx = filename.indexOf("?");
        if (idx > 0) {
            return filename.substring(0, idx);
        }
        return filename;
    }


    public static void saveBitmap(HtmlViewer activity, String imageUri, String folder) {
        Request request = new Request.Builder()
                .get().url(imageUri)
                .build();
        Call call = activity.client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.saveImageResult(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    BufferedSink bufferedSink = null;
                    try {
                        String mimeType = response.header("Content-Type", "image/jpeg");
                        String extensionName = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                        File directory = new File(folder);
                        if (!directory.exists())
                            directory.mkdirs();
                        File file = new File(folder, "VBE" + System.currentTimeMillis() + "." + extensionName);
                        bufferedSink = Okio.buffer(Okio.sink(file));
                        bufferedSink.writeAll(response.body().source());
                        Util.updateGallery(activity, file);
                        activity.saveImageResult(true);
                    } catch (Exception e) {
                        activity.saveImageResult(false);
                    } finally {
                        if (bufferedSink != null) {
                            bufferedSink.close();
                        }
                    }
                } else {
                    activity.saveImageResult(false);
                }
            }
        });
    }

    public static class WebDownload implements DownloadListener {
        HtmlViewer activity;

        public WebDownload(HtmlViewer a) {
            activity = a;
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String disposition, String mimeType, long contentLength) {
            Log.i("disposition", disposition);
            Log.i("disposition-url", url);
            String name = UrlUtil.decode(getDownloadFileName(disposition, url).trim(), "UTF-8");
            String filename = validDownloadFileName(name);
            VbeUtil.showConfirmCancelDialog(activity, "下载文件", "文件名：" + filename + "\n大小：" + Util.getFormatSize(contentLength) + "\n确定要下载该文件？", new DialogResult() {
                @Override
                public void onConfirm() {
                    DownloadConfiguration.Builder builder = new DownloadConfiguration.Builder();
                    builder.addHeader("Accept", "*/*");
                    builder.addHeader("Content-Disposition", disposition);
                    builder.addHeader("Content-Length", ""+contentLength);
                    builder.addHeader("Content-Type", mimeType); //"application/octet-stream");
                    builder.addHeader("Referer", activity.currentUrl);
                    builder.addHeader("User-Agent", userAgent);

                    FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
                        @Override
                        public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                            FileDownloader.createAndStart(url, saveDir, Util.isNullOrEmpty(filename) ? fileName : filename);
                            VbeUtil.toastShortMessage(activity, "已创建下载任务");
                        }

                        @Override
                        public void onDetectUrlFileExist(String url) {
                            VbeUtil.toastShortMessage(activity,"任务已存在");
                        }

                        @Override
                        public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                            VbeUtil.toastShortMessage(activity, "新建下载失败");
                            ExceptionHandler.log("新建下载失败", failReason.getLocalizedMessage() + "\n(" + url + ")\n" + "Type:" + failReason.getType());
                        }
                    }, builder.build());
                    /*try
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        Util.toastShortMessage(getApplicationContext(), "未安装下载管理器");
                    }*/
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    public static int getStatusHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /*private Bitmap getShareBitmap() {
        View v = webView;
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return Bitmap.createScaledBitmap(v.getDrawingCache(), 120, 120, true);
    }

    /*private void saveBitmap(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Bitmap bitmap = Util.getNetBitmap(url);
                    if (bitmap != null) {
                        Util.saveBitmap(HtmlViewer.this, webConfig.SH_savePath, "VBE" + System.currentTimeMillis() + ".png", bitmap);
                        mHandler.sendEmptyMessage(3);
                    } else {
                        mHandler.sendEmptyMessage(4);
                    }
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(4);
                    ExceptionHandler.log("saveBitmap()", e);
                }
            }
        }).start();
    }*/
}
