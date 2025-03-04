package com.vbea.java21.ui;

import android.app.AlertDialog;
import android.net.Uri;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.data.BookMark;
import com.vbea.java21.data.WebHelper;
import com.vbea.java21.adapter.BookAdapter;
import com.vbea.java21.view.BookmarkDialog;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbes.util.VbeUtil;
import com.vbes.util.lis.DialogResult;

public class Bookmark extends BaseActivity
{
	private BookAdapter mAdapter;
	private WebHelper query;

	@Override
	protected void before() {
		setContentView(R.layout.musiclist);
	}

	@Override
	public void after() {
		enableBackButton(R.id.toolbar);
		init();
		RecyclerView recyclerView = bind(R.id.music_recyclerView);
		recyclerView.addItemDecoration(new MyDividerDecoration(this));
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		mAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(String url) {
				Intent intent = new Intent();
				intent.setData(Uri.parse(url));
				setResult(RESULT_OK, intent);
				supportFinishAfterTransition();
			}

			@Override
			public void onItemLongClick(BookMark bookMark) {
				new AlertDialog.Builder(Bookmark.this)
				.setTitle(R.string.bookmark)
				.setItems(R.array.array_bookaction, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface v, int b) {
						v.dismiss();
						switch (b) {
							case 0:
								Intent intent = new Intent();
								intent.setData(Uri.parse(bookMark.getUrl()));
								setResult(RESULT_OK, intent);
								supportFinishAfterTransition();
								break;
							case 1:
								onUpdateBookmark(bookMark);
								break;
							case 2:
								VbeUtil.showConfirmCancelDialog(Bookmark.this, "删除书签", "确认操作？", new DialogResult() {
									@Override
									public void onConfirm() {
										if (onDeleteBookmark(bookMark.getId())) {
											Util.toastShortMessage(getApplicationContext(), "删除成功");
											init();
											//mAdapter.notifyDataSetChanged();
										} else {
											Util.toastShortMessage(getApplicationContext(), "删除失败");
										}
									}

									@Override
									public void onCancel() {

									}
								});
								break;
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
			if (mAdapter == null)
				mAdapter = new BookAdapter(query.listBookmark());
			else
				mAdapter.setData(query.listBookmark());
			//ExceptionHandler.log("bookmark_query", "count=" + mAdapter.getItemCount());
		} catch (Exception e) {
			ExceptionHandler.log("bookmark_query", e);
		}
	}

	private void onAddBookmark() {
		if (query != null) {
			BookmarkDialog.showAddDialog(Bookmark.this, new BookMark(), new BookmarkDialog.CallBack() {
				@Override
				public void onCallback(String id, BookMark target) {
					long r = query.addBookmark(target.getTitle(), target.getUrl());
					if (r > 0) {
						Util.toastShortMessage(getApplicationContext(), "添加成功");
						init();
					} else {
						Util.toastShortMessage(getApplicationContext(), "添加失败");
					}
				}
			});
		}
	}
	
	private void onUpdateBookmark(BookMark item) {
		if (query != null) {
			BookmarkDialog.showEditDialog(this, item, new BookmarkDialog.CallBack() {
				@Override
				public void onCallback(String id, BookMark target) {
					try {
						int r = query.updateBookmark(target, id);
						if (r > 0) {
							Util.toastShortMessage(getApplicationContext(), "修改成功");
							init();
						} else {
							Util.toastShortMessage(getApplicationContext(), "修改失败");
						}
					} catch (Exception e) {
						ExceptionHandler.log("editBookmark", e);
					}
				}
			});
		} else {
			Util.toastShortMessage(getApplicationContext(), "修改失败");
		}
	}
	
	private boolean onDeleteBookmark(String id) {
		if (query != null) {
			try {
				return (query.deleteBookmark(id) > 0);
			} catch (Exception e) {
				ExceptionHandler.log("deleteBookmark", e);
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bookmark_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.item_add_book) {
			onAddBookmark();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
