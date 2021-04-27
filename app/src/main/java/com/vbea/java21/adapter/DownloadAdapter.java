package com.vbea.java21.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vbea.java21.R;
import com.vbea.java21.classes.Util;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.base.Status;

/**
 * Created by Vbe on 2019/2/15.
 */
public class DownloadAdapter extends BaseListAdapter<DownloadFileInfo> { //RecyclerView.Adapter<DownloadAdapter.MyViewHolder>{

    private List<Integer> downloading;
    private OnItemClickListener onItemClickListener;

    public DownloadAdapter() {
        super(R.layout.downitem);
    }

    @Override
    protected void onRender(BaseViewHolder holder, DownloadFileInfo info, int i) {
        holder.setText(R.id.file_name, info.getFileName());

        ImageView icon = holder.getView(R.id.file_icon);
        TextView detail = holder.getView(R.id.file_detail);
        LinearLayout layout = holder.getView(R.id.itemLayout);
        String size = Util.getFormatSize(info.getFileSizeLong());

        icon.setImageResource(R.drawable.ic_file_download);
        switch (info.getStatus()) {
            case Status.DOWNLOAD_STATUS_COMPLETED:
                icon.setImageResource(R.drawable.ic_anyfile);
                detail.setText(size + " - 已完成");
                break;
            case Status.DOWNLOAD_STATUS_DOWNLOADING:
                detail.setText(Util.getFormatSize(info.getDownloadedSizeLong()) + "/" + size + " - 正在下载");
                break;
            case Status.DOWNLOAD_STATUS_PAUSED:
                detail.setText(Util.getFormatSize(info.getDownloadedSizeLong()) + "/" + size + " - 已暂停");
                break;
            case Status.DOWNLOAD_STATUS_WAITING:
                detail.setText(size + " - 等待中");
                break;
            case Status.DOWNLOAD_STATUS_ERROR:
                detail.setText("下载出错");
                break;
            case Status.DOWNLOAD_STATUS_FILE_NOT_EXIST:
                detail.setText("文件已删除");
                break;
            case Status.DOWNLOAD_STATUS_RETRYING:
                detail.setText("正在重新请求下载");
                break;
            case Status.DOWNLOAD_STATUS_PREPARING:
                detail.setText("准备中");
                break;
            case Status.DOWNLOAD_STATUS_UNKNOWN:
                detail.setText("NA");
                break;
        }
        layout.setOnClickListener(new View.OnClickListener() {
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
        layout.setOnLongClickListener(new View.OnLongClickListener() {
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
            Collections.reverse(list);
            setNewData(list);
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onOpenFile(String file);
        void onItemClick(String url, int status);
        void onItemLongClick(String url, int status);
    }
}
