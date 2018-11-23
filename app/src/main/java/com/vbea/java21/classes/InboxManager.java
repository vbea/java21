package com.vbea.java21.classes;

import java.util.List;
import com.vbea.java21.data.Messages;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.exception.BmobException;

public class InboxManager
{
	private List<Messages> mList;
	private static final String FLAG = "InboxManager";
	private long lastTime = 0;
	private int unreadCount = 0;
	
	public void getMyInbox(long time, final InboxCallback callback)
	{
		if (time - lastTime > 30000)
		{
			lastTime = time;
			BmobQuery<Messages> sql = new BmobQuery<Messages>();
			sql.addWhereEqualTo("user", Common.getUsername());
			sql.addWhereEqualTo("read", false);
			sql.count(Messages.class, new CountListener()
			{
				@Override
				public void done(Integer count, BmobException e)
				{
					if (e != null)
					{
						ExceptionHandler.log(FLAG, e.toString());
						if (callback != null)
							callback.onFailure();
					}
					else
					{
						unreadCount = count;
						if (callback != null)
							callback.onSuccess();
					}
				}
			});
		}
	}
	
	public void clearCount()
	{
		unreadCount = 0;
	}
	
	public int getCount()
	{
		return unreadCount;
	}
	
	public List<Messages> getList()
	{
		return mList;
	}
	
	public void refreshMessage()
	{
		lastTime = 0;
	}
	
	public void getMessage(final InboxCallback callback)
	{
		BmobQuery<Messages> sql = new BmobQuery<Messages>();
		sql.addWhereEqualTo("user", Common.getUsername());
		sql.order("read,-createdAt");
		sql.findObjects(new FindListener<Messages>()
		{
			@Override
			public void done(List<Messages> list, BmobException e)
			{
				if (e != null)
				{
					ExceptionHandler.log(FLAG, e.toString());
					if (callback != null)
						callback.onFailure();
				}
				else
				{
					mList = list;
					if (callback != null)
						callback.onSuccess();
				}
			}
		});
	}
	
	public void addMessage(String ref, String user, String title, String msg, String url, int type)
	{
		Messages message = new Messages();
		message.refId = ref;
		message.user = user;
		message.title = title;
		message.message = msg;
		message.url = url;
		message.type = type;
		message.read = false;
		message.save(new SaveListener<String>()
		{
			@Override
			public void done(String p1, BmobException e)
			{
				if (e != null)
					ExceptionHandler.log(FLAG, e.toString());
			}
		});
	}
	
	public void updateMessage(Messages msg, final InboxCallback callback)
	{
		msg.update(msg.getObjectId(), new UpdateListener()
		{
			@Override
			public void done(BmobException e)
			{
				if (e != null)
				{
					ExceptionHandler.log(FLAG, e.toString());
					if (callback != null)
						callback.onFailure();
				}
				else
				{
					refreshMessage();
					if (callback != null)
						callback.onSuccess();
				}
			}
		});
	}
	
	public void deleteMessage(Messages msg, int p)
	{
		mList.remove(p);
		msg.delete(msg.getObjectId(), new UpdateListener()
		{
			@Override
			public void done(BmobException e)
			{
				if (e != null)
					ExceptionHandler.log(FLAG, e.toString());
				else
					refreshMessage();
			}
		});
	}
	
	public void logout()
	{
		mList = null;
		refreshMessage();
	}
	
	public interface InboxCallback
	{
		void onSuccess();
		void onFailure();
	}
}
