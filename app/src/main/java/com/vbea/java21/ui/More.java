package com.vbea.java21.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.IBinder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.audio.AudioService;
import com.vbes.util.EasyPreferences;

public class More extends BaseActivity
{
	private TextView txtCode, txtName, txtPianoSize;
	private Switch swtLoop, swtOrder;
	private ProgressBar proMusic;
	private SoundLoad soundLoad = null;
	private PlayThread mThread = null;
	private String MusicName;
	private String[] arraySize;

	@Override
	protected void before() {
		setContentView(R.layout.more);
	}

	@Override
	protected void after() {
		enableBackButton();
		arraySize = getResources().getStringArray(R.array.array_pianosize);
		RelativeLayout btnLoop = bind(R.id.btn_setLoop);
		RelativeLayout btnOrder = bind(R.id.btn_setOrder);
		RelativeLayout btnSize = bind(R.id.btn_setPianoSize);
		TextView btnPiano = bind(R.id.btn_musicPiano);
		TextView btnTest = bind(R.id.btn_musicTest);
		TextView btnTrans = bind(R.id.btn_musicTrans);
		TextView btnList = bind(R.id.btn_musicList);
		txtPianoSize = bind(R.id.txt_pianoSize);
		txtCode = bind(R.id.more_musicCode);
		txtName = bind(R.id.more_musicName);
		proMusic = bind(R.id.pro_micMore);
		swtLoop = bind(R.id.slid_setLoop);
		swtOrder = bind(R.id.slid_setOrder);

		txtPianoSize.setText(arraySize[Common.PIANO_SIZE]);

		btnLoop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				swtLoop.toggle();
				Common.audioService.setLoop(swtLoop.isChecked());
				swtOrder.setChecked(Common.audioService.order);
			}
		});
		swtLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				Common.audioService.setLoop(p2);
				swtOrder.setChecked(Common.audioService.order);
			}
		});
		btnOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				swtOrder.toggle();
				Common.audioService.setOrder(swtOrder.isChecked());
				swtLoop.setChecked(Common.audioService.loop);
			}
		});
		swtOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2) {
				Common.audioService.setOrder(p2);
				swtLoop.setChecked(Common.audioService.loop);
			}
		});
		btnTrans.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Common.startActivityOptions(More.this, MusicTrans.class);
			}
		});
		
		btnPiano.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(More.this, Piano.class));
			}
		});
		
		btnList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Common.startActivityOptions(More.this, MusicList.class);
			}
		});
		
		btnTest.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Common.startActivityOptions(More.this, MusicTest.class);
			}
		});

		btnSize.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(More.this);
				builder.setSingleChoiceItems(arraySize, Common.PIANO_SIZE, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Common.PIANO_SIZE = which;
						txtPianoSize.setText(arraySize[which]);
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
		soundLoad = Common.SOUND;
		//if (soundLoad != null)
			//generiteMusic();
		//createNotification();
	}
	
	class MusicPlayerClick implements View.OnClickListener {
		int music;
		public MusicPlayerClick(int mu)
		{
			music = mu;
		}
		@Override
		public void onClick(View v) {
			if (Common.audioService != null) {
				/*if (Common.audioService.isPlay())
				{
					stoped();
					return;
				}
				else
				{*/
					if (Common.audioService != null) {
						Common.audioService.play(music);
						if (mThread == null) {
							mThread = new PlayThread();
							mThread.start();
						}
					} else
						supportFinishAfterTransition();
					//Util.toastLongMessage(getApplicationContext(), "max:"+Common.SOUND.getMusicMax(music) + "\nmin:" + Common.SOUND.getMusicMin(music));
				
			}
		}
	}
	
	private void init() {
		MusicName = "";
		if (Common.audioService == null) {
			if (Common.SOUND != null) {
				if (soundLoad == null)
					soundLoad = Common.SOUND;
				Intent intent = new Intent(getApplicationContext(), AudioService.class);
				intent.setAction(AUDIO_SERVICE);
				startService(intent);
				bindService(intent, sconn, Context.BIND_AUTO_CREATE);
			} else
				finishAfterTransition();
		} else {
			swtLoop.setChecked(Common.audioService.loop);
			swtOrder.setChecked(Common.audioService.order);
			if (Common.audioService.isPlay()) {
				showMusicName();
				if (mThread == null) {
					mThread = new PlayThread();
					mThread.start();
				}
			}
		}
	}
	
	class PlayThread extends Thread { //implements Runnable
		@Override
		public void run() {
			synchronized(this) {
				try {
					if (Common.audioService != null) {
						mHandler.sendEmptyMessage(3);
						while(true) {
							if (Common.audioService.isPlay()) {
								Thread.sleep(10);
								mHandler.sendEmptyMessage(1);
							} else {
								mHandler.sendEmptyMessage(2);
								Thread.sleep(1000);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
				case 1:
					proMusic.setMax(Common.audioService.max);
					proMusic.setProgress(Common.audioService.current);
					txtCode.setText(Common.audioService.mid);
					if (Common.audioService.current == 0)
						MusicName = "";
					if (Common.audioService.current < 5)
						showMusicName();
					break;
				case 2:
					proMusic.setProgress(0);
					MusicName = "";
					txtCode.setText("");
					txtName.setText("");
					break;
				case 3:
					MusicName = Common.audioService.getMusicName();
					showMusicName();
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void showMusicName() {
		if (Util.isNullOrEmpty(MusicName))
			mHandler.sendEmptyMessage(3);
		else
			txtName.setText(MusicName);
	}
	
	private void CopyMusic(String music) {
		Util.addClipboard(this, "music", music);
		Util.toastShortMessage(getApplicationContext(), "已复制代码到剪贴板");
	}
	
	private ServiceConnection sconn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName p1, IBinder p2) {
			Common.audioService = ((AudioService.AudioBinder)p2).getService();
			swtLoop.setChecked(Common.audioService.loop);
			swtOrder.setChecked(Common.audioService.order);
		}

		@Override
		public void onServiceDisconnected(ComponentName p1) {
			//mService = null;
		}
	};

	@Override
	protected void onResume() {
		init();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		EasyPreferences.putInt("piano_size", Common.PIANO_SIZE);
		EasyPreferences.apply();
		super.onDestroy();
	}
}
