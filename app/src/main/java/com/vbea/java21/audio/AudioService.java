package com.vbea.java21.audio;

import android.app.Service;
import android.os.Binder;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;

import android.content.Intent;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

public class AudioService extends Service
{
	private AudioPlayListener listener;
	private boolean isPlaying = false, isPlay = false;
	public boolean loop = false;
	public boolean order = false;
	public int what = -1, max = 0,current = 0;//, result = 0;
	//public SoundLoad.Music music;
	private musicThread mThread;
	private String[] strmusic;
	public String mid = "";
	
	private final IBinder binder = new AudioBinder();
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return binder;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO: Implement this method
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		isPlay = false;
		super.onDestroy();
	}
	
	public void setOnAudioPlayListener(AudioPlayListener lis)
	{
		listener = lis;
	}
	
	public void play(int mu)
	{
		if (Common.SOUND == null)
			return;
		if (mThread != null)
		{
			if (what != mu || !isPlaying)
			{
				what = mu;
				mThread.init();
			}
			else
				Stop();
		}
		else
		{
			what = mu;
			//strmusic = Common.SOUND.getMusic(mu);
			//if (strmusic != null)
			mThread = new musicThread();
			mThread.start();
		}
	}
	
	public void Stop()
	{
		isPlay = isPlaying = false;
		current = max = 0;
		what = -1;
		mThread = null;
	}
	
	public boolean isPlay()
	{
		return isPlaying;
	}
	
	public void setLoop(boolean _loop)
	{
		loop = _loop;
		if (order && loop)
			order = false;
	}
	
	public void setOrder(boolean _order)
	{
		order = _order;
		if (loop && order)
			loop = false;
	}
	
	public String getMusicName()
	{
		return Common.SOUND.getMusicName(what);
	}
	
	public class AudioBinder extends Binder
	{
		public AudioService getService()
		{
			return AudioService.this;
		}
	}
	
	class musicThread extends Thread //implements Runnable
	{
		long longs = 0;
		long shortx = 0;
		public musicThread()
		{
			init();
		}
		
		public void init()
		{
			if (Common.SOUND != null)
			{
				if (Common.SOUND.isValid(what))
				{
					strmusic = Common.SOUND.getMusicArray(what);
					if (strmusic != null)
					{
						longs = Common.SOUND.getMusicMax(what);
						shortx = Common.SOUND.getMusicMin(what);
						zero();
						isPlay = max > 0;
						return;
					}
				}
			}
			stoped();
		}
		
		@Override
		public void run()
		{
			try
			{
				isPlaying = true;
				Thread.sleep(500);
				while (isPlay)
				{
					if (isPlay)
					{
						synchronized (this)
						{
							mid = strmusic[current].trim();
							if (mid.indexOf("_") > 0)
							{
								String[] s = mid.split("_");
								for (String _mid : s)
								{
									if (!isPlaying)
										break;
									mid = _mid.trim();
									if (play(false))
										Thread.sleep(shortx/2);
									else
										Thread.sleep(shortx/4);
								}
								sleep(shortx/2);
								current++;
							}
							else
							{
								if (play(true))
									Thread.sleep(longs);
								else
									Thread.sleep(shortx);
							}
						}
					}
					if (current == max)
					{
						mid = "";
						if (isPlay)
						{
							if (loop)
							{
								current = 0;
								sleep(1000);
							}
							else if (order)
							{
								if (Common.SOUND.isValid(what+1))
								{
									what+=1;
									sleep(500);
									init();
									sleep(500);
								}
								else
									isPlay = false;
							}
							else
								isPlay = false;
						}
					}
				}
				isPlaying = false;
			}
			catch (Exception e)
			{
				stoped();
				ExceptionHandler.log("AudioServise.Thread(" + current + ")", e.toString());
			}
			finally
			{
				stoped();
			}
		}

		public void stoped()
		{
			isPlay = isPlaying = false;
			what = -1;
			zero();
			Stop();
		}
		
		private void zero()
		{
			max = strmusic.length;
			current = 0;
			mid = "";
		}
		
		public boolean play(boolean nor)
		{
			if (mid.indexOf("-") > 0)
				runpl(mid.split("-"));
			else
				runpl(mid);
			if (nor)
				current++;
			if (mid.equals("-"))
			{
				mid = "";
				return true;
			}
			else if (mid.equals("_"))
			{
				mid = "";
				return false;
			}
			else
				return Character.isUpperCase(mid.charAt(0));
		}
	}
	
	public void runpl(final String...music)
	{
		/*new Handler().post(new Runnable()
		{
			public void run()
			{*/
				try
				{
					Common.SOUND.play(music);
				}
				catch (Exception e)
				{
					isPlay = false;
				}
			/*}
		});*/
	}
}
