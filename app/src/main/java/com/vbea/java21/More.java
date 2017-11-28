package com.vbea.java21;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.IBinder;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.audio.AudioService;

public class More extends AppCompatActivity
{
	private LinearLayout layoutMusic;
	private TextView txtCode, txtName;
	private Switch swtLoop, swtOrder;
	private ProgressBar proMusic;
	private SoundLoad soundLoad = null;
	private PlayThread mThread = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		RelativeLayout btnLoop = (RelativeLayout) findViewById(R.id.btn_setLoop);
		RelativeLayout btnOrder = (RelativeLayout) findViewById(R.id.btn_setOrder);
		TextView btnPiano = (TextView) findViewById(R.id.btn_musicPiano);
		TextView btnTest = (TextView) findViewById(R.id.btn_musicTest);
		TextView btnTrans = (TextView) findViewById(R.id.btn_musicTrans);
		layoutMusic = (LinearLayout) findViewById(R.id.more_musicLayout);
		txtCode = (TextView) findViewById(R.id.more_musicCode);
		txtName = (TextView) findViewById(R.id.more_musicName);
		proMusic = (ProgressBar) findViewById(R.id.pro_micMore);
		swtLoop = (Switch) findViewById(R.id.slid_setLoop);
		swtOrder = (Switch) findViewById(R.id.slid_setOrder);
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		btnLoop.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				swtLoop.toggle();
				Common.audioService.setLoop(swtLoop.isChecked());
				swtOrder.setChecked(Common.audioService.order);
			}
		});
		swtLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				Common.audioService.setLoop(p2);
				swtOrder.setChecked(Common.audioService.order);
			}
		});
		btnOrder.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				swtOrder.toggle();
				Common.audioService.setOrder(swtOrder.isChecked());
				swtLoop.setChecked(Common.audioService.loop);
			}
		});
		swtOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				Common.audioService.setOrder(p2);
				swtLoop.setChecked(Common.audioService.loop);
			}
		});
		btnTrans.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(More.this, MusicTrans.class);
			}
		});
		
		btnPiano.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(More.this, Piano.class);
			}
		});
		
		btnTest.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(More.this, MusicTest.class);
			}
		});
		soundLoad = Common.SOUND;
		if (soundLoad != null)
			generiteMusic();
	}
	
	public void generiteMusic()
	{
		LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < soundLoad.getMusicCout(); i++)
		{
			View t = inf.inflate(R.layout.music, layoutMusic, false);
			TableRow row = (TableRow) t.findViewById(R.id.musicTableRow);
			TextView text = (TextView) t.findViewById(R.id.music_txtMusicName);
			TextView velo = (TextView) t.findViewById(R.id.music_txtMusicVolet);
			text.setText(soundLoad.getMusicName(i));
			velo.setText(getMusicVelot(i));
			row.setOnClickListener(new MusicPlayerClick(i));
			row.setOnLongClickListener(new MusicPlayerLongClick(i));
			layoutMusic.addView(row);
		}
	}
	
	class MusicPlayerClick implements View.OnClickListener
	{
		int music;
		public MusicPlayerClick(int mu)
		{
			music = mu;
		}
		@Override
		public void onClick(View v)
		{
			if (Common.audioService != null)
			{
				/*if (Common.audioService.isPlay())
				{
					stoped();
					return;
				}
				else
				{*/
					if (Common.audioService != null)
					{
						//showMusicName();
						Common.audioService.play(music);
						if (mThread == null)
						{
							mThread = new PlayThread();
							mThread.start();
						}
					}
					else
						supportFinishAfterTransition();
					//Util.toastLongMessage(getApplicationContext(), "max:"+Common.SOUND.getMusicMax(music) + "\nmin:" + Common.SOUND.getMusicMin(music));
				
			}
		}
	}
	
	class MusicPlayerLongClick implements View.OnLongClickListener
	{
		int music;
		public MusicPlayerLongClick(int mu)
		{
			music = mu;
		}
		@Override
		public boolean onLongClick(View v)
		{
			CopyMusic(soundLoad.getMusic(music));
			return true;
		}
	}
	
	private void init()
	{
		if (Common.audioService == null)
		{
			if (Common.SOUND != null)
			{
				if (soundLoad == null)
					soundLoad = Common.SOUND;
				Intent intent = new Intent(getApplicationContext(), AudioService.class);
				intent.setAction(AUDIO_SERVICE);
				startService(intent);
				bindService(intent, sconn, Context.BIND_AUTO_CREATE);
			}
			else
				finishAfterTransition();
		}
		else
		{
			swtLoop.setChecked(Common.audioService.loop);
			swtOrder.setChecked(Common.audioService.order);
			if (Common.audioService.isPlay())
			{
				showMusicName();
				if (mThread == null)
				{
					mThread = new PlayThread();
					mThread.start();
				}
			}
		}
	}
	
	class PlayThread extends Thread //implements Runnable
	{
		@Override
		public void run()
		{
			synchronized(this)
			{
				try
				{
					if (Common.audioService != null)
					{
						while(true)
						{
							if (Common.audioService.isPlay())
							{
								Thread.sleep(10);
								mHandler.sendEmptyMessage(1);
							}
							else
							{
								mHandler.sendEmptyMessage(2);
								Thread.sleep(1000);
							}
						}
					}
				}
				catch (Exception e)
				{
					
				}
			}
		}
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					proMusic.setMax(Common.audioService.max);
					proMusic.setProgress(Common.audioService.current);
					txtCode.setText(Common.audioService.mid);
					if (Common.audioService.current < 5)
						showMusicName();
					break;
				case 2:
					proMusic.setProgress(0);
					txtCode.setText("");
					txtName.setText("");
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void showMusicName()
	{
		txtName.setText(Common.audioService.getMusicName());
	}
	
	private void CopyMusic(String music)
	{
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		cbm.setPrimaryClip(ClipData.newPlainText("music", music));
		Util.toastShortMessage(getApplicationContext(), "已复制代码到剪贴板");
	}
	
	private String getMusicVelot(int i)
	{
		if (Common.mUser != null && Common.mUser.roles.equals("管理员"))
			return soundLoad.getMusicMax(i) + "/" + soundLoad.getMusicMin(i);
		return (100 - (soundLoad.getMusicMax(i) / 5)) + "/" + (90 - (soundLoad.getMusicMin(i) / 5));
	}
	private ServiceConnection sconn = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName p1, IBinder p2)
		{
			Common.audioService = ((AudioService.AudioBinder)p2).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName p1)
		{
			//mService = null;
		}
	};

	@Override
	protected void onResume()
	{
		init();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
