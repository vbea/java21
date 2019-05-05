package com.vbea.java21;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TableRow;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;

public class MusicTest extends BaseActivity
{
	private SeekBar seekVelo, seekVelo2;
	private TextView txtVelocity, txtVelocity2, velUpper, velLower;
	private EditText txtMusic;
	private Button btnPlay;
	private TableRow tabRow1, tabRow2;
	private long velocity = 200, mvelo = 150;
	private boolean isStop = true;
	private int checkLog = 0;
	//private int keyHeight = 500;

	@Override
	protected void before()
	{
		setContentView(R.layout.musicedt);
	}

	
	@Override
	public void after()
	{
		enableBackButton();
		seekVelo = bind(R.id.seekVelocity);
		seekVelo2 = bind(R.id.seekVelocity2);
		txtVelocity = bind(R.id.txtVelocity);
		txtVelocity2 = bind(R.id.txtVelocity2);
		velUpper = bind(R.id.volUpper);
		velLower = bind(R.id.volLower);
		btnPlay = bind(R.id.btn_musicPlays);
		txtMusic = bind(R.id.txt_musicTest);
		tabRow1 = bind(R.id.testMusic_tb1);
		tabRow2 = bind(R.id.testMusic_tb2);
		//txtMusic.setText(""+velocity);
		String codes = getIntent().getStringExtra("music");
		if (codes != null)
			txtMusic.setText(codes);
		/*txtMusic.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (txtMusic.isFocused())
				{
					tabRow1.setVisibility(View.GONE);
					tabRow2.setVisibility(View.GONE);
					btnPlay.setVisibility(View.GONE);
				}
			}
		});*/
		
		velUpper.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				int p2 = seekVelo.getProgress();
				p2-=1;
				seekVelo.setProgress(p2);
				velocity = 5*(100-p2);
				txtVelocity.setText(""+p2);
			}
		});
		velLower.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				int p2 = seekVelo2.getProgress();
				p2-=1;
				seekVelo2.setProgress(p2);
				mvelo = 5*(90-p2);
				if (mvelo == 0)
					mvelo = 10;
				txtVelocity2.setText(""+p2);
			}
		});
		txtVelocity.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				int p2 = seekVelo.getProgress();
				p2+=1;
				seekVelo.setProgress(p2);
				velocity = 5*(100-p2);
				txtVelocity.setText(""+p2);
			}
		});
		txtVelocity2.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				int p2 = seekVelo2.getProgress();
				p2+=1;
				seekVelo2.setProgress(p2);
				mvelo = 5*(90-p2);
				if (mvelo == 0)
					mvelo = 10;
				txtVelocity2.setText(""+p2);
			}
		});
		seekVelo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar p1, int p2, boolean p3)
			{
				velocity = 5*(100-p2);
				txtVelocity.setText(""+p2);
				//Toast.makeText(getApplicationContext(), "progress:"+p2, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStartTrackingTouch(SeekBar p1)
			{
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar p1)
			{
				
			}
		});
		seekVelo2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar p1, int p2, boolean p3)
			{
				mvelo = 5*(90-p2);
				if (mvelo == 0)
					mvelo = 10;
				txtVelocity2.setText(""+p2);
			}

			@Override
			public void onStartTrackingTouch(SeekBar p1)
			{
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar p1)
			{
			}
		});
		
		btnPlay.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (isStop)
				{
					isStop = false;
					if (txtMusic.getText().toString().trim().equals(""))
						Util.toastShortMessage(getApplicationContext(), "请输入音节代码");
					else
					{
						try
						{
							String[] codes = txtMusic.getText().toString().trim().split(",");
							new MusicThread(codes).start();
						}
						catch (Exception e)
						{
							ExceptionHandler.log("PlayMusicCode",e.toString());
						}
					}
				}
				else
				{
					isStop = true;
					handler.sendEmptyMessage(1);
				}
			}
		});
	}
	
	private boolean test(String[] test)
	{
		Pattern pat = Pattern.compile("([A-z]|[ABCabc][1-9]|[Aa]1[0-6]|[Bb]10|[Cc][1-3][0-9]|-|_)");
		for (String code : test)
		{
			if (code.indexOf("_") > 0)
			{
				if (!test(code.split("_")))//递归检查
					return false;
				continue;
			}
			else if (code.indexOf("-") > 0)
			{
				checkLog += code.length() + 1;
				if (!test1(code.split("-")))
					return false;
				continue;
			}
			else
			{
				Matcher m = pat.matcher(code);
				checkLog += code.length() + 1;
				if (!m.matches())
					return false;
			}
		}
		return true;
	}
	
	private boolean test1(String[] test)
	{
		Pattern pat = Pattern.compile("([A-z]|[ABCabc][1-9]|[Aa]1[0-6]|[Bb]10|[Cc][1-3][0-9])");
		for (String code : test)
		{
			Matcher m = pat.matcher(code);
			if (!m.matches())
				return false;
		}
		return true;
	}
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					btnPlay.setText("测试");
					break;
				case 2:
					btnPlay.setText("正在检查格式…");
					break;
				case 3:
					//isStop = false;
					btnPlay.setText("停止");
					txtMusic.setSelection(0);
					break;
				case 4:
					if (txtMusic.getText().toString().length() >= msg.arg1)
						txtMusic.setSelection(msg.arg1);
					else
						txtMusic.setSelection(txtMusic.getText().toString().length());
					break;
				case 5:
					Util.toastShortMessage(getApplicationContext(), "格式不正确");
					if (txtMusic.getText().toString().length() >= checkLog)
						txtMusic.setSelection(checkLog);
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onDestroy()
	{
		isStop = true;
		super.onDestroy();
	}
	
	private class MusicThread extends Thread implements Runnable
	{
		private String[] codes;
		private int log = 0;
		public MusicThread(String[] c)
		{
			codes = c;
		}
		
		public boolean play(String code) throws Exception
		{
			if (code.indexOf("-") > 0)
				Common.SOUND.play(code.split("-"));
			else
				Common.SOUND.play(code);
			log += (code.length() + 1);
			if (code.equals("-"))
				return true;
			else if (code.equals("_"))
				return false;
			else
				return Character.isUpperCase(code.charAt(0));
		}
		
		private void send()
		{
			Message msg = Message.obtain();
			msg.what = 4;
			msg.arg1 = log-1;
			handler.sendMessage(msg);
		}
		
		public void run()
		{
			try
			{
				handler.sendEmptyMessage(2);
				Thread.sleep(500);
				checkLog = 0;
				if (!test(codes))
				{
					handler.sendEmptyMessage(5);
					return;
				}
				handler.sendEmptyMessage(3);
				if (Common.audioService.isPlay())
					Common.audioService.Stop();
				for (String code : codes)
				{
					if (isStop)
					{
						handler.sendEmptyMessage(1);
						break;
					}
					if (code.indexOf("_") > 0)
					{
						for (String s : code.split("_"))
						{
							if (play(s))
								sleep(mvelo/2);
							else
								sleep(mvelo/4);
							send();
						}
						sleep(mvelo/2);
					}
					else
					{
						if (play(code))
						{
							send();
							sleep(velocity);
						}
						else
						{
							send();
							sleep(mvelo);
						}
					}
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("PlayMusicThread", e.toString());
				//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
			finally
			{
				isStop = true;
				handler.sendEmptyMessage(1);
			}
		}
	}
}
