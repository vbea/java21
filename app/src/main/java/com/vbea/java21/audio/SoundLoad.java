package com.vbea.java21.audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.HashMap;

import android.os.Environment;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;
import android.media.AudioManager;
import android.media.AudioAttributes;

import com.vbea.java21.R;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Common;

public class SoundLoad
{
	private SoundPool mSoundPool1;//,mSoundPool2;//,mSoundPool3;
	private AssetManager am;
	private String[] MusicNames, MusicCodes;
	private int[] MusicMax, MusicMin;
	//private File soundFile,file;
	private String[] sound_alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};//常用音源26
	private String[] sound_bass = {"A1","A2","A3","A4","A5","A6","A7","A8","A9","A10","A11","A12","A13","A14","A15","A16"};//低音16
	private String[] sound_pitch = {"B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"};//高音10
	private String[] sound_tone = {"C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15","C16","C17","C18","C19","C20","C21","C22","C23","C24","C25","C26","C27","C28","C29","C30","C31","C32","C33","C34","C35","C36"};//升降调36
	private HashMap<String, Integer> hash_music,hash_music2;//,hash_tone;
	private String path = "sound/%1$s.ogg";
	public boolean isCometed = false;
	//private boolean soDown;
	public SoundLoad(Context context) throws Exception
	{
		/*soundFile = new File(Environment.getExternalStorageDirectory() + "/ZDApp/Sound");//音源目录
		if (!soundFile.exists())
		{
			//isFile = false;
			soundFile.mkdirs();
			unZIP(context);
		}
		if (soundFile.list().length < 88)
			unZIP(context);*/
		am = context.getAssets();
		isCometed = false;
		//start new Auduo 20170810
		mSoundPool1 = getSoundPool();
		//mSoundPool2 = getSoundPool();
		//end new Audio
		//new SoundPool(88, AudioManager.STREAM_MUSIC, 0);
		//mSoundPool2 = new SoundPool(26, AudioManager.STREAM_MUSIC, 0);
		//mSoundPool3 = new SoundPool(36, AudioManager.STREAM_MUSIC, 0);
		MusicNames = context.getResources().getStringArray(R.array.music_names);
		MusicCodes = context.getResources().getStringArray(R.array.music_codes);
		MusicMax = context.getResources().getIntArray(R.array.music_sleepmax);
		MusicMin = context.getResources().getIntArray(R.array.music_sleepmin);
		//path = soundFile.getAbsolutePath() + file.separator;
		hash_music = new HashMap<String, Integer>(88);
		hash_music2 = new HashMap<String, Integer>(88);
		//hash_base = new Hashtable<String, Integer>(26);
		//hash_tone = new Hashtable<String, Integer>(36);
		/*mSoundPool1.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
		{
			@Override
			public void onLoadComplete(SoundPool pool, int id,int s)
			{
				isCometed = true;
			}
		});*/
		//soDown = true;
	}
	
	public SoundPool getSoundPool()
	{
		SoundPool.Builder mSoundPool = new SoundPool.Builder();
		mSoundPool.setMaxStreams(10);
		AudioAttributes.Builder aab = new AudioAttributes.Builder();
		aab.setUsage(AudioAttributes.USAGE_MEDIA);
		aab.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
		aab.setLegacyStreamType(AudioManager.STREAM_MUSIC);
		mSoundPool.setAudioAttributes(aab.build());
		return mSoundPool.build();
	}
	/*public boolean isFile()
	{
		isFile = true;
		if (soundFile.list().length < 88)
			isFile = false;
		return isFile;
	}*/
	
	public void load()
	{
		load1();
		/*try
		{
			if (soundFile.list().length == 88)
			{
				for (File p : soundFile.listFiles())
				{
					hash_music.put(p.getName().replace(".ogg", ""), mSoundPool1.load(p.getAbsolutePath(), 1));
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("SoundLoad.load",e.toString());
		}*/
	}
	//正常音节码解析
	public void load1()
	{
		try
		{
			//26正常声音解析
			for (int i = 0; i<sound_alpha.length; i++)
			{
				if (!hash_music.containsKey(sound_alpha[i]))
					hash_music.put(sound_alpha[i], mSoundPool1.load(am.openFd(String.format(path, sound_alpha[i])), 1));
				/*if (!hash_music2.containsKey(sound_alpha[i]))
					hash_music2.put(sound_alpha[i], mSoundPool2.load(am.openFd(String.format(path, sound_alpha[i])), 1));*/
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("SoundLoad.load1",e.toString());
		}
		finally
		{
			load2();
		}
	}
	//高低音节补充码解析
	public void load2()
	{
		try
		{
			//16低音解析
			for (int i = 0; i<sound_bass.length; i++)
			{
				if (!hash_music.containsKey(sound_bass[i]))
					hash_music.put(sound_bass[i], mSoundPool1.load(am.openFd(String.format(path, sound_bass[i])), 1));
				/*if (!hash_music2.containsKey(sound_bass[i]))
					hash_music2.put(sound_bass[i], mSoundPool2.load(am.openFd(String.format(path, sound_bass[i])), 1));*/
			}
			//10高音解析
			for (int i = 0; i<sound_pitch.length; i++)
			{
				if (!hash_music.containsKey(sound_pitch[i]))
					hash_music.put(sound_pitch[i], mSoundPool1.load(am.openFd(String.format(path, sound_pitch[i])), 1));
				/*if (!hash_music2.containsKey(sound_pitch[i]))
					hash_music2.put(sound_pitch[i], mSoundPool2.load(am.openFd(String.format(path, sound_pitch[i])), 1));*/
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("SoundLoad.load2", e.toString());
		}
		finally
		{
			load3();
		}
	}
	
	//升降调音节码解析
	public void load3()
	{
		try
		{
			//36升降调解析
			for (int i = 0; i<sound_tone.length; i++)
			{
				if (!hash_music.containsKey(sound_tone[i]))
					hash_music.put(sound_tone[i], mSoundPool1.load(am.openFd(String.format(path, sound_tone[i])), 1));
				/*if (!hash_music2.containsKey(sound_tone[i]))
					hash_music2.put(sound_tone[i], mSoundPool2.load(am.openFd(String.format(path, sound_tone[i])), 1));*/
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("SoundLoad.load3", e.toString());
		}
		finally
		{
			Common.AUDIO = isCometed = true;
		}
	}
	
	public boolean isNull()
	{
		return (hash_music.size() == 88);
	}
	
	public void play(String id) throws Exception
	{
		if (id.equals("-") || id.equals("_"))
			return;
		id = id.toUpperCase();
		mSoundPool1.play(hash_music.get(id), 1, 1, 3, 0, 1);
		//mSoundPool2.play(hash_music2.get(id), 0, 1, 3, 0, 1);
		/*if (soDown)
		{
			soDown = false;
			mSoundPool1.play(hash_music.get(id), 1, 1, 1, 0, 1);
		}
		else
		{
			soDown = true;
			mSoundPool2.play(hash_music2.get(id), 1, 1, 1, 0, 1);
		}*/
	}
	
	public void play(String[] ids) throws Exception
	{
		for (String id:ids)
			play(id);
	}
	
	/*public void stop(String id) throws Exception
	{
		if (id.equals("-") || id.equals("_"))
			return;
		id = id.toUpperCase();
		mSoundPool1.pause(hash_music.get(id));
	}
	
	public void stop(String[] ids) throws Exception
	{
		for (String id:ids)
			stop(id);
	}*/
	
	public String getMusic(int m)
	{
		if (m < MusicCodes.length)
			return MusicCodes[m];
		return "";
	}
	
	public boolean isValid(int m)
	{
		return m < MusicCodes.length;
	}
	
	public int getMusicCout()
	{
		return MusicNames.length;
	}
	
	public String[] getMusicArray(int m)
	{
		if (m < MusicCodes.length)
			return MusicCodes[m].split(",");
		return null;
	}
	
	public String getMusicName(int m)
	{
		//ExceptionHandler.log("getMusicName", m + "," + MusicNames);
		if (MusicNames != null && m >= 0 && m < MusicNames.length)
			return MusicNames[m];
		return "";
	}
	
	public int getMusicMax(int m)
	{
		try
		{
			if (m < MusicMax.length)
				return MusicMax[m];
		}
		catch (Exception e)
		{
			ExceptionHandler.log("sleepMax", e.toString());
		}
		return 200;
	}
	
	public int getMusicMin(int m)
	{
		try
		{
			if (m < MusicMin.length)
				return MusicMin[m];
		}
		catch (Exception e)
		{
			ExceptionHandler.log("sleepMin", e.toString());
		}
		return 150;
	}
	
	/*public String[] getRandomMusic()
	{
		int m = (int)(Math.random() * 15);
		switch (m)
		{
			case 0:
				return mic_canon.split(",");
			case 1:
				return mic_alice.split(",");
			case 2:
				return mic_skyp.split(",");
			case 3:
				return mic_freu.split(",");
			case 4:
				return mic_hisouten.split(",");
			case 5:
				return mic_interstellar.split(",");
			case 6:
				return mic_leave.split(",");
			case 7:
				return mic_river.split(",");
			case 8:
				return mic_songb.split(",");
			case 9:
				return mic_maiden.split(",");
			case 10:
				return mic_mario.split(",");
			case 11:
				return mic_doraemon.split(",");
			default:
				return mic_night5.split(",");
		}
	}*/
	
	public void clear()
	{
		if (mSoundPool1 != null) {
			mSoundPool1.autoPause();
			mSoundPool1.release();
		}
		/*if (mSoundPool2 != null)
			mSoundPool2.release();*/
		//if (mSoundPool3 != null)
			//mSoundPool3.release();
	}
	
	/*public void unZIP(Context context) throws IOException
	{
		InputStream is = context.getAssets().open("sound.zip");
		if (is == null)
			return;
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry zipe = zis.getNextEntry();
		byte[] buffer = new byte[1024*1024];//1M
		int count;//解压字节数
		while (zipe != null)
		{
			if (!zipe.isDirectory())
			{
				file = new File(soundFile.getAbsolutePath() + File.separator + zipe.getName());
				if (!file.exists())
				{
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					while ((count = zis.read(buffer)) > 0)
					{
						fos.write(buffer, 0, count);
					}
					fos.close();
				}
			}
			zipe = zis.getNextEntry();
		}
		zis.close();
	}*/
}
