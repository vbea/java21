package com.vbea.java21.ui;

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.data.WebHelper;
import com.vbea.java21.data.Histories;
import com.vbea.java21.data.Histime;
import com.vbea.java21.adapter.HistoryAdapter;
import com.vbea.java21.view.HistoryDecoration;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.TimeAgo;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbes.util.VbeUtil;
import com.vbes.util.lis.DialogResult;
import com.vbes.util.list.StickyDecoration;

public class History extends BaseActivity
{
	private HistoryAdapter mAdapter;
	private WebHelper query;
	private List<Histories> mList;
	private List<Histime> dataList;
	//private HistoryDecoration hisDecoration;

	@Override
	protected void before()
	{
		setContentView(R.layout.musiclist);
	}

	@Override
	public void after()
	{
		enableBackButton();
		RecyclerView recyclerView = bind(R.id.music_recyclerView);
		mAdapter = new HistoryAdapter();
		init();
		recyclerView.addItemDecoration(new MyDividerDecoration(this));
		//hisDecoration = new HistoryDecoration(this, dataList, new DecorationLis());
		//recyclerView.addItemDecoration(hisDecoration);
		StickyDecoration.StickyOptions options = new StickyDecoration.StickyOptions();
		options.setStickyBackground(MyThemes.isNightTheme() ? R.color.textSecondary : R.color.dividers);
		options.setStickyHeight(R.dimen.sectioned_top);
		options.setStickyPadding(R.dimen.sectioned_alignBottom);
		options.setTextColor(R.color.textPrimary);
		options.setTextSize(14);
		recyclerView.addItemDecoration(new StickyDecoration(this, options, new DecorationLis()));
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(String url) {
				Intent intent = new Intent();
				intent.setData(Uri.parse(url));
				setResult(RESULT_OK, intent);
				supportFinishAfterTransition();
			}

            @Override
            public void onItemLongClick(String title, String url) {
                new AlertDialog.Builder(History.this)
                    .setTitle(R.string.history)
                    .setItems(R.array.array_hisaction, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface v, int b) {
                            v.dismiss();
                            switch (b) {
                                case 0:
                                    Intent intent = new Intent();
                                    intent.setData(Uri.parse(url));
                                    setResult(RESULT_OK, intent);
                                    supportFinishAfterTransition();
                                    break;
                                case 1:
                                    if (query.addBookmark(title, url) > 0)
                                        toastShortMessage("添加书签成功");
                                    else
                                        toastShortMessage("添加失败");
                            }
                        }
                    }).show();
            }
        });
	}

	private void init() {
		try {
			if (query == null)
				query = new WebHelper(this);
			Cursor cursor = query.listHistory();
			if (mList == null) {
				mList = new ArrayList<>();
			} else {
				mList.clear();
			}
			if (dataList == null) {
				dataList = new ArrayList<>();
			} else {
				dataList.clear();
			}
			TimeAgo timeAgo = new TimeAgo(new SimpleDateFormat("MM月dd日 E"));
			timeAgo.setIsTime(false);
			while (cursor.moveToNext()) {
				Histories his = new Histories();
				his.setUrl(cursor.getString(cursor.getColumnIndex(WebHelper.COL_URL)));
				his.setTitle(cursor.getString(cursor.getColumnIndex(WebHelper.COL_TITLE)));
				his.setCreate(Long.parseLong(cursor.getString(cursor.getColumnIndex(WebHelper.COL_CREATEON))));
				Histime hisTime = new Histime();
				hisTime.setDate(timeAgo.getTimeAgo(his.getCreateOn()));
				mList.add(his);
				dataList.add(hisTime);
				mAdapter.setData(mList);
				/*if (hisDecoration != null)
					hisDecoration.setData(dataList);*/
			}
		} catch (Exception e) {
			ExceptionHandler.log("history_query_init", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.history_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		VbeUtil.showConfirmCancelDialog(this, "清空历史记录", "确认操作？", new DialogResult() {
			@Override
			public void onConfirm() {
				try {
					query.clearHistory();
					Util.toastShortMessage(History.this, "操作成功");
					init();
					mAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					Util.toastShortMessage(History.this, "操作失败");
				}
			}

			@Override
			public void onCancel() {

			}
		});
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onFinish() {
		setResult(RESULT_CANCELED);
		super.onFinish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	class DecorationLis implements StickyDecoration.DecorationCallback {
		@Override
		public String getGroupId(int position) {
			if (dataList.get(position).getDate() != null)
				return dataList.get(position).getDate();
			return "";
		}

		/*@Override
		public String getGroupFirstLine(int position) {
			if (dataList.get(position).getDate() != null)
				return dataList.get(position).getDate();
			return "";
		}*/
	}
}
