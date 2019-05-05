package com.vbea.java21.audio;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.media.AudioManager;
import android.media.AudioAttributes;

import com.vbea.java21.R;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class SoundLoad
{
	private SoundPool mSoundPool1;//,mSoundPool2;//,mSoundPool3;
	private AssetManager am;
	private List<Music> musicTop;
	private final String[] sound_alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};//常用音源26
	private final String[] sound_bass = {"A1","A2","A3","A4","A5","A6","A7","A8","A9","A10","A11","A12","A13","A14","A15","A16"};//低音16
	private final String[] sound_pitch = {"B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"};//高音10
	private final String[] sound_tone = {"C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15","C16","C17","C18","C19","C20","C21","C22","C23","C24","C25","C26","C27","C28","C29","C30","C31","C32","C33","C34","C35","C36"};//升降调36
	private HashMap<String, Integer> hash_music;//,hash_music2,hash_tone;
	private String path = "sound/%1$s.ogg";
	public boolean isCompeted = false;
	//private int playCode = 0;
	//private boolean soDown;
	public SoundLoad(Context context) throws Exception
	{
		am = context.getAssets();
		isCompeted = false;
		//start new Auduo 20170810
		mSoundPool1 = getSoundPool();
		hash_music = new HashMap<String, Integer>(88);
		//musicAll = new ArrayList<Music>();
		musicTop = new ArrayList<Music>();
		//hash_music2 = new HashMap<String, Integer>(88);
	}
	
	private SoundPool getSoundPool()
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
	
	public void load()
	{
		synaxMusic();
		load1();
	}
	//正常音节码解析
	private void load1()
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
	private void load2()
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
	private void load3()
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
			Common.AUDIO = isCompeted = true;
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
	}
	
	public void play(String[] ids) throws Exception
	{
		for (String id:ids)
			play(id);
	}
	
	private void synaxMusic()
	{
		try
		{
			String json = Util.ReadFileToString(am.open("music/music.json"));
			if (json != null)
			{
				Gson gson = new Gson();
				JsonParser jsp = new JsonParser();
				JsonArray jArray = jsp.parse(json).getAsJsonArray();
				for (JsonElement obj : jArray)
				{
					Music music = gson.fromJson(obj, Music.class);
					music.isTop = true;
					musicTop.add(music);
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("synaxMusic", e.toString());
		}
		finally
		{
			synaxMusic2();
		}
	}
	
	private void synaxMusic2()
	{
		try
		{
			String json = Util.ReadFileToString(am.open("music/music_all.json"));
			if (json != null)
			{
				Gson gson = new Gson();
				JsonParser jsp = new JsonParser();
				JsonArray jArray = jsp.parse(json).getAsJsonArray();
				for (JsonElement obj : jArray)
				{
					Music music = gson.fromJson(obj, Music.class);
					music.isTop = false;
					musicTop.add(music);
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("synaxMusic2", e.toString());
		}
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
	
	public Music getMusic(int m)
	{
		if (m < musicTop.size())
			return musicTop.get(m);
		return null;
	}
	
	public List<Music> getMusicList()
	{
		return musicTop;
	}
	
	/*public boolean isValid(int m)
	{
		return m < musicTop.size();
	}*/
	
	public int getMusicCount()
	{
		return musicTop.size();
	}
	
	public void clear()
	{
		if (mSoundPool1 != null)
		{
			mSoundPool1.autoPause();
			mSoundPool1.release();
		}
	}
	
	/*public void stop()
	{
		if (mSoundPool1 != null)
		{
			mSoundPool1.stop(playCode);
		}
	}*/
	
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
