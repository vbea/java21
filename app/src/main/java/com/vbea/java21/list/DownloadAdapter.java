package com.vbea.java21.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vbea.java21.R;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.base.Status;

/**
 * Created by Vbe on 2019/2/15.
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyViewHolder>{
    private List<Integer> downloading;
    private OnItemClickListener onItemClickListener;
    private List<DownloadFileInfo> mList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
    {
        LayoutInflater inflate = LayoutInflater.from(p1.getContext());
        View v = inflate.inflate(R.layout.downitem, p1, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int p)
    {
        final DownloadFileInfo info = mList.get(p);
        holder.title.setText(info.getFileName());
        String size = Util.getFormatSize(info.getFileSizeLong());
        holder.icon.setImageResource(R.mipmap.ic_file_download);
        switch (info.getStatus()) {
            case Status.DOWNLOAD_STATUS_COMPLETED:
                holder.icon.setImageResource(R.mipmap.ic_anyfile);
                holder.detail.setText(size + " - 已完成");
                break;
            case Status.DOWNLOAD_STATUS_DOWNLOADING:
                holder.detail.setText(Util.getFormatSize(info.getDownloadedSizeLong()) + "/" + size + " - 正在下载");
                break;
            case Status.DOWNLOAD_STATUS_PAUSED:
                holder.detail.setText(Util.getFormatSize(info.getDownloadedSizeLong()) + "/" + size + " - 已暂停");
                break;
            case Status.DOWNLOAD_STATUS_WAITING:
                holder.detail.setText(size + " - 等待中");
                break;
            case Status.DOWNLOAD_STATUS_ERROR:
                holder.detail.setText("下载出错");
                break;
            case Status.DOWNLOAD_STATUS_FILE_NOT_EXIST:
                holder.detail.setText("文件已删除");
                break;
            case Status.DOWNLOAD_STATUS_RETRYING:
                holder.detail.setText("正在重新请求下载");
                break;
            case Status.DOWNLOAD_STATUS_PREPARING:
                holder.detail.setText("准备中");
                break;
            case Status.DOWNLOAD_STATUS_UNKNOWN:
                holder.detail.setText("NA");
                break;
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    if (info.getStatus() == Status.DOWNLOAD_STATUS_COMPLETED) {
                        onItemClickListener.onOpenFile(info.getFilePath());
                    } else {
                        onItemClickListener.onItemClick(info.getUrl(), info.getStatus());
                    }
                }
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemLongClick(info.getUrl(), info.getStatus());
                return true;
            }
        });
    }

    public void setList(List<DownloadFileInfo> list) {
        if (list != null) {
            mList = list;
            Collections.reverse(mList);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public List<Integer> getDownloading() {
        if (downloading == null)
            downloading = new ArrayList<>();
        else
            downloading.clear();
        for (int i = 0; i < getItemCount(); i++) {
            if (mList.get(i).getStatus() == Status.DOWNLOAD_STATUS_DOWNLOADING)
                downloading.add(i);
        }
        return downloading;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView detail;
        ImageView icon;
        LinearLayout layout;
        public MyViewHolder(View v)
        {
            super(v);
            title = v.findViewById(R.id.file_name);
            detail = v.findViewById(R.id.file_detail);
            icon = v.findViewById(R.id.file_icon);
            layout = v.findViewById(R.id.itemLayout);
        }
    }

    public interface OnItemClickListener
    {
        void onOpenFile(String file);
        void onItemClick(String url, int status);
        void onItemLongClick(String url, int status);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
