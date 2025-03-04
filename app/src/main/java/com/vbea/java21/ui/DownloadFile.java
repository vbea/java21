package com.vbea.java21.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.adapter.DownloadAdapter;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbes.util.UriUtils;
import com.vbes.util.VbeUtil;
import com.vbes.util.view.MyAlertDialog;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.base.Status;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.simple.OnSimpleFileDownloadStatusListener;
import org.wlf.filedownloader.util.UrlUtil;

import java.io.File;
import java.util.List;

/**
 * Created by Vbe on 2019/2/15.
 */
public class DownloadFile extends BaseActivity {
    private LinearLayout noView;
    private RecyclerView recyclerView;
    private DownloadAdapter mAdapter;
    private boolean isLooper = false;
    @Override
    protected void before() {
        setContentView(R.layout.download);
    }

    @Override
    protected void after() {
        enableBackButton(R.id.toolbar);
        noView = bind(R.id.dm_noView);
        recyclerView = bind(R.id.dm_recyclerView);
        recyclerView.addItemDecoration(new MyDividerDecoration(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DownloadAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(null);
        //ExceptionHandler.log("DownloadFile", "" + FileDownloader.getDownloadFiles().size());
        mAdapter.setOnItemClickListener(new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onOpenFile(String file) {
                Intent intent;
                if (VbeUtil.isAndroidM()) {
                    intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                } else {
                    intent = new Intent();
                }
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String mime = Util.getMimeType(file);
                /*if (Util.isAndroidN())
                {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }*/
                if (Util.isAndroidN()) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(FileProvider.getUriForFile(DownloadFile.this, Common.FileProvider, new File(file)), mime);
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(file)), mime);
                }
                startActivity(intent);
            }

            @Override
            public void onItemClick(String url, int status) {
                if (status == Status.DOWNLOAD_STATUS_DOWNLOADING)
                    FileDownloader.pause(url);
                else if (status == Status.DOWNLOAD_STATUS_PAUSED)
                    FileDownloader.start(url);
                refresh();
            }

            @Override
            public void onItemLongClick(String url, int status) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFile.this);
                builder.setTitle("下载");
                builder.setItems(R.array.array_download_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                if (status == Status.DOWNLOAD_STATUS_DOWNLOADING)
                                    FileDownloader.pause(url);
                                else if (status != Status.DOWNLOAD_STATUS_COMPLETED)
                                    FileDownloader.start(url);
                                refresh();
                                break;
                            case 1:
                                MyAlertDialog dialog1 = new MyAlertDialog(DownloadFile.this);
                                dialog1.setTitle("删除任务");
                                dialog1.setMessage("您确定要删除此任务？");
                                dialog1.setNegativeButton(android.R.string.cancel, null);
                                dialog1.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FileDownloader.delete(url, false, new OnDeleteDownloadFileListener() {
                                            @Override
                                            public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {

                                            }

                                            @Override
                                            public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
                                                toastShortMessage("删除成功");
                                                refresh();
                                            }

                                            @Override
                                            public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo, DeleteDownloadFileFailReason failReason) {
                                                toastShortMessage("删除失败");
                                            }
                                        });
                                    }
                                });
                                dialog1.setNeutralButton("同时删除文件", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FileDownloader.delete(url, true, new OnDeleteDownloadFileListener() {
                                            @Override
                                            public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {

                                            }

                                            @Override
                                            public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
                                                toastShortMessage("删除成功");
                                                refresh();
                                            }

                                            @Override
                                            public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo, DeleteDownloadFileFailReason failReason) {
                                                toastShortMessage("删除失败");
                                            }
                                        });
                                    }
                                });
                                dialog1.show();
                                break;
                            case 2:
                                FileDownloader.reStart(url);
                                refresh();
                                break;
                            case 3:
                                Util.addClipboard(DownloadFile.this, url);
                                toastShortMessage("复制成功");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);
        refresh();
    }

    private void refresh() {
        mAdapter.setList(FileDownloader.getDownloadFiles());
        if (mAdapter.getItemCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            if (!isLooper) {
                isLooper = true;
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        } else {
            noView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            View view = LayoutInflater.from(DownloadFile.this).inflate(R.layout.webhome, null);
            final EditText edt = view.findViewById(R.id.edt_webhome);
            MyAlertDialog dialog = new MyAlertDialog(DownloadFile.this);
            dialog.setTitle("新建下载");
            dialog.setView(view);
            dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface d, int s)
                {
                    if (Util.isNullOrEmpty(edt.getText().toString().trim())) {
                        toastShortMessage("请输入下载链接");
                        return;
                    }
                    toastShortMessage("请稍候...");
                    FileDownloader.detect(edt.getText().toString(), new OnDetectBigUrlFileListener() {
                        @Override
                        public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                            FileDownloader.createAndStart(url, saveDir, UrlUtil.decode(fileName, "UTF-8"));
                            toastLongMessage("已创建下载任务");
                            refresh();
                        }

                        @Override
                        public void onDetectUrlFileExist(String url) {
                            toastShortMessage("任务已存在");
                        }

                        @Override
                        public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                            toastShortMessage("新建下载失败");
                            ExceptionHandler.log("DownloadFailed", failReason.getLocalizedMessage() + "\n" + url);
                        }
                    });
                }
            });
            dialog.setNegativeButton(android.R.string.cancel, null);
            dialog.show();
        } else if (item.getItemId() == R.id.item_startAll) {
            FileDownloader.continueAll(true);
            refresh();
        } else if (item.getItemId() == R.id.item_stopAll) {
            FileDownloader.pauseAll();
            refresh();
        } else if (item.getItemId() == R.id.item_clear) {
            List<DownloadFileInfo> list = FileDownloader.getDownloadFiles();
            for (DownloadFileInfo info : list) {
                if (info.getStatus() == Status.DOWNLOAD_STATUS_COMPLETED) {
                    FileDownloader.delete(info.getUrl(), false,null);
                }
            }
            toastShortMessage("清理完成");
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
    }

    private OnFileDownloadStatusListener mOnFileDownloadStatusListener = new OnSimpleFileDownloadStatusListener() {
        @Override
        public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) {
            // 正在重试下载（如果你配置了重试次数，当一旦下载失败时会尝试重试下载），retryTimes是当前第几次重试
            refresh();
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
            //refresh();
        }

        @Override
        public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
            // 下载已被暂停
        }

        @Override
        public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
            // 下载完成（整个文件已经全部下载完成）
            refresh();
        }

        @Override
        public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, OnFileDownloadStatusListener.FileDownloadStatusFailReason failReason) {
            // 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心
            refresh();
            /*String failType = failReason.getType();
            String failUrl = failReason.getUrl();// 或：failUrl = url，url和failReason.getUrl()会是一样的

            if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType)) {
                // 下载failUrl时出现url错误
            } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType)) {
                // 下载failUrl时出现本地存储空间不足
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType)) {
                // 下载failUrl时出现无法访问网络
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType)) {
                // 下载failUrl时出现连接超时
            } else {
                // 更多错误....
            }

            // 查看详细异常信息
            Throwable failCause = failReason.getCause();// 或：failReason.getOriginalCause()

            // 查看异常描述信息
            String failMsg = failReason.getMessage();// 或：failReason.getOriginalCause().getMessage()*/
        }
    };

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //List<Integer> list = mAdapter.getDownloading();
                //for (Integer i : list) {
                    //mAdapter.notifyItemChanged(i);
                //}
                if (mAdapter.getItemCount() > 0) {
                    mAdapter.notifyDataSetChanged();
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                } else
                    isLooper = false;
            }
            super.handleMessage(msg);
        }
    };
}
