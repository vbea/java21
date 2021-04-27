package com.vbea.java21.adapter;

import java.io.File;
import java.util.List;
import java.util.Collections;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vbea.java21.R;
import com.vbea.java21.data.FileItem;
import com.vbes.util.VbeUtil;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class FileAdapter extends BaseListAdapter<FileItem> { //RecyclerView.Adapter<FileAdapter.MyViewHolder>

	private String imageFile = ".jpg.jpeg.png.webp.bmp.gif.svg.mp4.3gp.rmvb.rm.mov.mpg.mpeg.avi.mkv";

	private OnItemClickListener onItemClickListener;

	public FileAdapter() {
		super(R.layout.fileitem);
	}

	@Override
	protected void onRender(BaseViewHolder holder, FileItem file, int p) {
		holder.setText(R.id.file_name, file.getName());
		if (file.isUplevel())
			holder.setText(R.id.file_detail, file.getDetail());
		else
			holder.setText(R.id.file_detail, file.getDetail() + " " + file.getSize());
		ImageView thumb = holder.getView(R.id.file_thumb);
		if (file.isFolder()) {
			thumb.setVisibility(View.GONE);
			holder.setGone(R.id.file_icon, true);
			holder.setImageResource(R.id.file_icon, R.drawable.ic_folder);
		} else {
			holder.setImageResource(R.id.file_icon, R.drawable.ic_anyfile);
			thumb.setVisibility(View.GONE);
			holder.setGone(R.id.file_icon, true);
			if (!VbeUtil.isNullOrEmpty(file.getExtension()) && imageFile.contains(file.getExtension())) {
				Glide.with(holder.itemView).asBitmap().load(new File(file.getPath())).listener(new RequestListener<Bitmap>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
						thumb.setVisibility(View.VISIBLE);
						holder.setGone(R.id.file_icon, false);
						return false;
					}
				}).into(thumb);
			}
		}
		//holder.setImageResource(R.id.file_icon, file.isFolder() ? R.drawable.ic_folder : R.drawable.ic_anyfile);
		holder.setOnClickListener(R.id.itemLayout, new View.OnClickListener() {
			public void onClick(View v) {
				if(onItemClickListener != null && file.isEnable()) {
					onItemClickListener.onItemClick(file);
				}
			}
		});
		holder.setOnLongClickListener(R.id.itemLayout, new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View p1) {
				if (file.isFolder() && !file.isUplevel()) {
					onItemClickListener.onDelete(file);
					return true;
				}
				return false;
			}
		});

	}
	
	public void setList(List<FileItem> list) {
		Collections.sort(list);
		setNewData(list);
	}
	
	public void addList(List<FileItem> list) {
		Collections.sort(list);
		addData(list);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

	public interface OnItemClickListener {
        void onItemClick(FileItem fileItem);
		void onDelete(FileItem fileItem);
    }
}
