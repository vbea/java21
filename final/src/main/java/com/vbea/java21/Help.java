package com.vbea.java21;

import android.view.MenuItem;
import android.view.Menu;
import android.webkit.WebView;

import com.vbea.java21.classes.Common;

public class Help extends BaseActivity
{
	@Override
	protected void before()
	{
		setContentView(R.layout.help);
	}

	@Override
	public void after()
	{
		enableBackButton();
		WebView web = bind(R.id.webHelp);
		web.loadUrl("file:///android_asset/java/help.html");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.feed_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.item_feedback)
		{
			//Common.startActivityOptions(Help.this, Feedback.class);
		}
		return true;
	}
	
}
