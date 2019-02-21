package com.vbea.java21.audio;

import android.app.NotificationChannel;
import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.widget.RemoteViews;
import android.media.AudioManager;

import com.vbea.java21.More;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.R;
import com.vbea.java21.classes.Util;

public class AudioService extends Service
{
	private AudioManager audioManager;
	private NotificationManager notiManager;
	//private AudioPlayListener listener;
	private boolean isPlaying = false, isPlay = false, isReceived = false, isPaused = false, isNoysi = false, isHeadset = false;
	public boolean loop = false, order = true;
	public int what = -1, max = 0,current = 0;//, result = 0;
	public Music music;
	private MusicThread mThread;
	private String[] strmusic;
	public String mid = "";
	private final String PLAY_CLOSE = "action.close", PLAY_NEXT = "action.next", PLAY_PAUSE = "action.pause", ALARM_ALERT = "com.android.deskclock.ALARM_ALERT";
	private OnAudioChangedListener listener;
	
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
		notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (Util.isAndroidO()) {
			NotificationChannel channel = new NotificationChannel("22", "AudioPlayer", NotificationManager.IMPORTANCE_LOW);
			channel.enableVibration(false);
			notiManager.createNotificationChannel(channel);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onRebind(Intent intent)
	{
		super.onRebind(intent);
	}

	public void registerReceiver()
	{
		if (isReceived)
			unregisterReceiver(receiver);
		IntentFilter filter = new IntentFilter();
		filter.addAction(PLAY_PAUSE);
		filter.addAction(PLAY_CLOSE);
		filter.addAction(PLAY_NEXT);
		filter.addAction(ALARM_ALERT);
		filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(receiver, filter);
		isReceived = true;
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver)
	{
		super.unregisterReceiver(receiver);
		isReceived = false;
	}
	
	public void addAudioChangedListener(OnAudioChangedListener lis)
	{
		listener = lis;
	}

	@Override
	public void onDestroy()
	{
		Stop();
		super.onDestroy();
	}
	
	//发送通知
	public void createNotification()
	{
		if (music == null)
			return;
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.music_noti);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.mipmap.wel_icon);
		builder.setOngoing(true);
		if (Util.isAndroidN()) {
			builder.setCustomContentView(view);
		} else {
			builder.setContent(view);
		}
		if (Util.isAndroidO()) {
			builder.setChannelId("22");
		}
		builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, More.class), PendingIntent.FLAG_CANCEL_CURRENT));
		view.setTextViewText(R.id.noti_text, getMusicName());
		view.setTextViewText(R.id.noti_sub, isPaused ? "已暂停" : "正在播放");
		view.setOnClickPendingIntent(R.id.noti_play, PendingIntent.getBroadcast(this, 1, new Intent(PLAY_PAUSE), PendingIntent.FLAG_UPDATE_CURRENT));
		view.setOnClickPendingIntent(R.id.noti_close, PendingIntent.getBroadcast(this, 1, new Intent(PLAY_CLOSE), PendingIntent.FLAG_UPDATE_CURRENT));
		view.setOnClickPendingIntent(R.id.noti_next, PendingIntent.getBroadcast(this, 2, new Intent(PLAY_NEXT), PendingIntent.FLAG_UPDATE_CURRENT));
		view.setImageViewResource(R.id.noti_play, isPaused ? R.mipmap.not_play : R.mipmap.not_pause);
		notiManager.notify(1, builder.build());
		registerReceiver();
	}
	
	/*public void setOnAudioPlayListener(AudioPlayListener lis)
	{
		listener = lis;
	}*/
	
	//播放音乐
	public void play(int mu)
	{
		isNoysi = isPaused = false;
		if (Common.SOUND == null)
			return;
		if (mThread != null)
		{
			if (what != mu || !isPlaying)
			{
				what = mu;
				music = Common.SOUND.getMusic(what);
				mThread.init();
			}
			else
				Stop();
		}
		else
		{
			what = mu;
			music = Common.SOUND.getMusic(what);
			if (music != null)
			{
				mThread = new MusicThread(false);
				mThread.setPriority(Thread.MAX_PRIORITY);
				mThread.start();
				audioManager.requestAudioFocus(afListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			}
		}
	}
	
	public void Stop()
	{
		isPlay = isPlaying = false;
		current = max = 0;
		what = -1;
		mThread = null;
		notiManager.cancelAll();
		if (isReceived)
			unregisterReceiver(receiver);
		audioManager.abandonAudioFocus(afListener);
		if (listener != null)
			listener.onAudioChange();
	}
	
	public void onPause()
	{
		//isPlay = isPlaying = false;
		mThread = null;
	}
	
	public void Pause()
	{
		if (mThread != null)
		{
			mThread.setIsResume(true);
			isPlay = false;
			isPaused = true;
			//Common.SOUND.stop();
			createNotification();
			if (listener != null)
				listener.onAudioChange();
		}
	}
	
	public void Replay()
	{
		if (Common.SOUND == null)
			return;
		if (music != null)
		{
			isPaused = false;
			isPlay = true;
			isNoysi = false;
			mThread = new MusicThread(true);
			mThread.start();
			audioManager.requestAudioFocus(afListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			createNotification();
			if (listener != null)
				listener.onAudioChange();
		}
	}
	
	public boolean isPlay()
	{
		return isPlaying;
	}
	
	public boolean isPause()
	{
		return isPaused;
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
		if (music != null)
			return music.getName();
		return "";
	}
	
	public boolean playNext()
	{
		what+=1;
		music = Common.SOUND.getMusic(what);
		return music != null;
	}
	
	public void nextMusic()
	{
		if (mThread != null)
		{
			what+=1;
			if (what >= Common.SOUND.getMusicCount())
				what = 0;
			music = Common.SOUND.getMusic(what);
			mThread.init();
		}
		else
		{
			if (isPause())
				isPaused = false;
			what+=1;
			if (what >= Common.SOUND.getMusicCount())
				what = 0;
			play(what);
		}
		if (listener != null)
			listener.onAudioChange();
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			switch (intent.getAction())
			{
				case PLAY_NEXT:
					nextMusic();
					break;
				case PLAY_CLOSE:
					Stop();
					break;
				case PLAY_PAUSE:
					if (isPaused)
						Replay();
					else {
						Pause();
						audioManager.abandonAudioFocus(afListener);
					}
					break;
				case ALARM_ALERT:
					Stop();
					break;
				case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
					Pause();
					isNoysi = isHeadset = true;
					break;
				case Intent.ACTION_HEADSET_PLUG:
					if (isHeadset)
						isHeadset = false;
					else if (intent.getIntExtra("state", 0) == 1 && isNoysi)
						Replay();
					break;
			}
		}
	};
	
	private AudioManager.OnAudioFocusChangeListener afListener = new AudioManager.OnAudioFocusChangeListener()
	{
		@Override
		public void onAudioFocusChange(int code)
		{
			//ExceptionHandler.log("onAudioFocusChange", "code:" + code);
			switch (code)
			{
				//case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
					//audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
					//pause
					Pause();
					break;
				/*case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
					Replay();
					break;*/
				case AudioManager.AUDIOFOCUS_GAIN:
					Replay();
					//resume
					break;
				case AudioManager.AUDIOFOCUS_LOSS:
					Pause();
					break;
			}
		}
	};
	
	public class AudioBinder extends Binder
	{
		public AudioService getService()
		{
			return AudioService.this;
		}
	}
	
	class MusicThread extends Thread //implements Runnable
	{
		long longs = 0;
		long shortx = 0;
		boolean isResume;
		
		public MusicThread(boolean resume)
		{
			isResume = resume;
		}

		public void setIsResume(boolean resume)
		{
			this.isResume = resume;
		}
		
		public void init()
		{
			if (music != null)
			{
				strmusic = music.getKeys();
				longs = music.max;
				shortx = music.min;
				zero();
				isPlay = max > 0;
			}
			else
				stoped();
		}
		
		public void replay()
		{
			longs = music.max;
			shortx = music.min;
		}
		
		@Override
		public void run()
		{
			try
			{
				if (!isResume)
					init();
				else
					replay();
				isPlaying = isPlay;
				sleep(500);
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
								sleep(500);
								if (playNext())
									init();
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
			if (!isResume)
			{
				what = -1;
				zero();
				Stop();
			}
			else
				onPause();
		}
		
		private void zero()
		{
			if (strmusic != null)
			{
				max = strmusic.length;
				createNotification();
			}
			else
				max = 0;
			current = 0;
			mid = "";
		}
		
		public void runpl(final String...music)
		{
			try
			{
				Common.SOUND.play(music);
			}
			catch (Exception e)
			{
				isPlay = false;
			}
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
}
