package com.vbea.java21.list;

import java.util.List;
import java.util.Collections;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	private List<WifiItem> mList;
	private WifiInfo cureent;
	private String mSSID = "";
	private WifiManager wmg;
	public WifiAdapter(Context context)
	{
		wmg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		resumes();
	}
	
	public void setList(List<WifiItem> list)
	{
		mList = list;
		Collections.sort(mList);
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.wifi_item, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int p)
	{
		final WifiItem item = mList.get(p);
		final StringBuilder info = new StringBuilder();
		info.append("安全性：" + item.Safety);
		if (!item.Safety.toLowerCase().equals("none") && item.Password != null)
			info.append("\n密码：" + item.Password);
		if (mSSID.indexOf(item.SSID) >= 0)
		{
			holder.txt_ssid.setText(item.SSID + "(已连接)");
			info.append("\nIP地址：" + Formatter.formatIpAddress(cureent.getIpAddress()));
		}
		else
			holder.txt_ssid.setText(item.SSID);
		holder.txt_info.setText(info);
		holder.layout.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onItemClick(item.SSID, info.toString());
				return true;
			}
		});
	}

	@Override
	public int getItemCount()
	{
		if (mList != null)
			return mList.size();
		return 0;
	}
	
	public void resumes()
	{
		cureent = wmg.getConnectionInfo();
		if (cureent != null)
			mSSID = cureent.getSSID();
	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

	class MyViewHolder extends ViewHolder
	{
		TextView txt_ssid, txt_info;
		LinearLayout layout;
		public MyViewHolder(View v)
		{
			super(v);
			txt_ssid = (TextView) v.findViewById(R.id.wifi_ssid);
			txt_info = (TextView) v.findViewById(R.id.wifi_infos);
			layout = (LinearLayout) v.findViewById(R.id.itemLayout);
		}
	}
	
	public interface OnItemClickListener
	{
        void onItemClick(String ssid, String psd);
    }
}
