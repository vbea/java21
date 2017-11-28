package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.content.Intent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import com.vbea.java21.list.ThemeItem;
import com.vbea.java21.list.ThemeAdapter;
import com.vbea.java21.classes.Common;

public class Themes extends AppCompatActivity
{
	private List<ThemeItem> mItem;
	private ThemeAdapter adapter;
	private SharedPreferences spf;
	private SharedPreferences.Editor edt;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme);
		
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.the_recyclerView);
		recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
		setSupportActionBar(tool);
		//tool.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
		//tool.setTitleTextColor(getResources().getColor(R.color.white));
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		edt = spf.edit();
		mItem = new ArrayList<ThemeItem>();
		String[] color = getResources().getStringArray(R.array.array_theme1);
		for (int i=0; i<color.length;i++)
		{
			mItem.add(new ThemeItem(i,color[i]));
		}
		adapter = new ThemeAdapter(mItem);
		recyclerView.setAdapter(adapter);
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//updated on 20170610
				//reload();
				supportFinishAfterTransition();
			}
		});
		
		adapter.setOnItemClickListener(new ThemeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(int id)
			{
				if (Common.APP_THEME_ID == id)
					return;
				Common.APP_THEME_ID = id;
				edt.putInt("theme", id);
				edt.commit();
				//MyThemes.ISCHANGED = true;
				//finishAfterTransition();
				reload();
				
				//intent.addCategory(Intent.CATEGORY_LAUNCHER);
				//intent.setComponent(new ComponentName(getPackageName(), getPackageName() + ".activity.Main"));
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//PendingIntent pend = PendingIntent.getActivity(Themes.this, 0, intent, 0);
				//finishAfterTransition();
				
				//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				//释放占用的内存资源
				//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				//android.os.Process.killProcess(android.os.Process.myPid());
				//overridePendingTransition(0, 0);
				//adapter.notifyDataSetChanged();*/
			}
		});
	}
	
	public void reload()
	{
		if (Common.isCanUploadUserSetting())
			Common.updateUser();
		MyThemes.ISCHANGED = true;
		ActivityManager mng = ActivityManager.getInstance();
		mng.FinishAllActivities();
		Common.startActivityOptions(Themes.this, Main.class);
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				finish();
			}
		}, 500);
	}

	/* updated on 20170610
	@Override
	public void onBackPressed()
	{
		reload();
	}*/
}
