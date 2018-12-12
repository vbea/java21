package com.vbea.java21.classes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.vbea.java21.data.Users;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Vbe on 2018/12/11.
 */

public class UserImageLoad {

    private OnLoadListener listener;

    public UserImageLoad(String url, OnLoadListener lis) {
        listener = lis;
        saveUserIconByName(url);
    }

    private void saveUserIconByName(String name)
    {
        BmobQuery<Users> sql = new BmobQuery<>();
        sql.addWhereEqualTo("name", name);
        sql.findObjects(new FindListener<Users>()
        {
            @Override
            public void done(List<Users> list, BmobException e)
            {
                if (e == null && list.size() > 0) {
                    Users user = list.get(0);
                    if (user.icon == null)
                        return;
                    File file = new File(Common.getCachePath(), user.name + ".png");
                    user.icon.download(file, new DownloadFileListener()
                    {
                        @Override
                        public void done(String p1, BmobException p2)
                        {
                            if (listener != null) {
                                listener.onComplete();
                            }
                        }

                        @Override
                        public void onProgress(Integer p1, long p2)
                        {

                        }
                    });
                }
            }
        });
    }

    /*private Bitmap getNetBitmap(String imageUri)
    {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try
        {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (Exception e)
        {
            ExceptionHandler.log("AsyncImageLoad.getNetBitmap", e);
            bitmap = null;
        }
        return bitmap;
    }*/

    public interface OnLoadListener {
        void onComplete();
    }
}
