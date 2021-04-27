package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.os.Handler;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import com.vbea.java21.data.ThemeItem;
import com.vbea.java21.adapter.ThemeAdapter;
import com.vbea.java21.classes.Common;

public class Themes extends BaseActivity
{
	private List<ThemeItem> mItem;
	private ThemeAdapter adapter;
	private SharedPreferences spf;

	@Override
	protected void before()
	{
		setContentView(R.layout.theme);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		RecyclerView recyclerView = bind(R.id.the_recyclerView);
		recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		mItem = new ArrayList<ThemeItem>();
		String[] color = getResources().getStringArray(R.array.array_theme1);
		for (int i=0; i<color.length;i++)
		{
			mItem.add(new ThemeItem(i,color[i]));
		}
		adapter = new ThemeAdapter(mItem);
		recyclerView.setAdapter(adapter);
		
		adapter.setOnItemClickListener(new ThemeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(int id)
			{
				if (Common.APP_THEME_ID == id)
					return;
				Common.APP_THEME_ID = id;
				SharedPreferences.Editor edt = spf.edit();
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
