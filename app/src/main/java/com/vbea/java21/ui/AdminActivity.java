package com.vbea.java21.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.data.Users;
import com.vbes.util.VbeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Vbe on 2018/12/12.
 */
public class AdminActivity extends BaseActivity {
    private ProgressDialog dialog;
    private int deleteCount = 0;

    @Override
    protected void before() {
        setContentView(R.layout.ac_handle);
    }

    @Override
    protected void after() {
        enableBackButton();
        bind(R.id.admin_clearUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUser();
            }
        });
    }

    private void clearUser() {
        deleteCount = 0;
        String createdAt = "2017-12-31 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = null;
        try {
            createdAtDate = sdf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        dialog = ProgressDialog.show(this, "运行中", "请稍候...");
        BmobQuery<Users> query = new BmobQuery<>();
        query.addWhereLessThan("updatedAt", bmobCreatedAtDate);
        query.setLimit(500);
        query.order("-updatedAt");
        query.findObjects(new FindListener<Users>() {
            @Override
            public void done(List<Users> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        toastLongMessage("找到" + list.size() + "条记录");
                        new DeleteThread(list).start();
                    }
                } else {
                    toastLongMessage(e.toString());
                }
            }
        });
    }

    private void deleteUser(Users u) {
        new Users().delete(u.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    deleteCount += 1;
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                VbeUtil.showResultDialog(AdminActivity.this,"成功删除" + deleteCount + "条记录", "清理用户");
                dialog.dismiss();
            }
            super.handleMessage(msg);
        }
    };

    class DeleteThread extends Thread implements Runnable {
        List<Users> users;
        DeleteThread(List<Users> u) {
            users = u;
        }
        public void run() {
            try {
                for (Users user : users) {
                    deleteUser(user);
                }
            } catch (Exception e) {
                ExceptionHandler.log("Admin:deleteUser", e);
            } finally {
                mHandler.sendEmptyMessage(1 );
            }
        }
    }
}
