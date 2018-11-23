package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.Handler;
import android.os.Message;
import android.widget.Button; 
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Comments;
import com.vbea.java21.list.CommentAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.exception.BmobException;

public class Comment extends BaseActivity
{
	/**
	* 邠心工作室
	* 21天学通Java
	* 评论页面
	*/
	private String resId, title, reference, url, reply;
	private Integer type;
	private TextView emptyView;
	private EditText edtComment;
	private Button btnComment;
	private List<Comments> mList;
	private RecyclerView recyclerView;
	private CommentAdapter mAdapter;
	private SimpleDateFormat dateformat;
	private InputMethodManager imm;
	private SwipeRefreshLayout refreshLayout;

	@Override
	protected void before()
	{
		setContentView(R.layout.comment);
	}

	@Override
	public void after()
	{
		recyclerView = bind(R.id.com_recyclerView);
		edtComment = bind(R.id.edt_comment);
		btnComment = bind(R.id.btnComment);
		emptyView = bind(R.id.comment_emptyview);
		refreshLayout = bind(R.id.comm_refresh);
		title = getIntent().getStringExtra("title");
		resId = getIntent().getStringExtra("id");
		url = getIntent().getStringExtra("url");
		type = getIntent().getIntExtra("type", 0);
		refreshLayout.setColorSchemeResources(MyThemes.getColorPrimary(), MyThemes.getColorAccent());
		if (Util.isNullOrEmpty(resId) || type == 0)
		{
			Util.toastShortMessage(this, "不支持的参数");
			supportFinishAfterTransition();
		}
		else
		{
			try
			{
				getData(true);
			}
			catch (Exception e)
			{
				mHandler.sendEmptyMessage(6);
			}
		}
		if (title != null)
			toolbar.setTitle(title + " 的评论");
		reference = "";
		reply = "";
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		dateformat = new SimpleDateFormat("M-d HH:mm");
		mList = new ArrayList<Comments>();
		mAdapter = new CommentAdapter(mList, this);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		if (!Common.isLogin())
		{
			hideInput();
			btnComment.setEnabled(false);
			edtComment.setEnabled(false);
			edtComment.setHint("登录后可发表评论");
		}
		else if (Common.mUser.role == 0)
		{
			edtComment.setEnabled(false);
			edtComment.setHint("您已被禁言，无法评论，请联系管理员解禁");
		}
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (reference != null && reference.length() > 0)
					clear();
				else
					supportFinishAfterTransition();
			}
		});
		
		edtComment.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				btnComment.setEnabled(s.toString().trim().length() > 0);
			}

			@Override
			public void afterTextChanged(Editable p1)
			{
			
			}
		});
		
		btnComment.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				addComment(edtComment.getText().toString().trim());
				edtComment.setText("");
				hideInput();
			}
		});
		
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				try
				{
					if (Common.isNet(Comment.this))
						getData(false);
					else
						Util.toastShortMessage(getApplicationContext(), "咦，网络去哪儿了？");
				}
				catch(Exception e)
				{
					mHandler.sendEmptyMessage(6);
				}
			}
		});
		
		mAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener()
		{
			@Override
			public void onReply(String user, String nick, String comment)
			{
				String reat = "回复@" + nick + "：";
				reply = user;
				edtComment.setHint(reat);
				reference = reat + comment;
				showInput();
			}

			@Override
			public void onDelete(final Comments comm, final int position)
			{
				deleteDialog(new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						deleteComment(comm, position);
					}
				});
			}
			
			@Override
			public void onEndorse(Comments comm, int p, boolean ed)
			{
				if (comm.endorse == null)
					comm.endorse = "";
				if (ed || !ed && !comm.endorse.contains(Common.getUsername()))
				{
					comm.endorse = addOrRemoveUserAction(comm.endorse, !ed);
					updateComment(comm);
				}
			}

			@Override
			public void onOppose(Comments comm, int p, boolean ed)
			{
				if (comm.oppose == null)
					comm.oppose = "";
				if (ed || !ed && !comm.oppose.contains(Common.getUsername()))
				{
					comm.oppose = addOrRemoveUserAction(comm.oppose, !ed);
					updateComment(comm);
				}
			}
		});
		showInput();
	}
	
	private void init()
	{
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		if (reference.length() > 0)
			clear();
		if (mList.size() > 0)
		{
			emptyView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			mAdapter.setList(mList);
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			emptyView.setVisibility(View.VISIBLE);
			emptyView.setText("期待你的神评论");
			recyclerView.setVisibility(View.GONE);
		}
	}
	
	private void getData(boolean cache) throws Exception
	{
		emptyView.setText("请稍候...");
		BmobQuery<Comments> sql = new BmobQuery<Comments>();
		sql.addWhereEqualTo("id", resId);
		sql.addWhereEqualTo("type", type);
		sql.order("-createdAt");
		if (cache)
			sql.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
		sql.findObjects(new FindListener<Comments>()
		{
			@Override
			public void done(List<Comments> list, BmobException e)
			{
				if (e == null)
				{
					mList = list;
					mHandler.sendEmptyMessage(1);
				}
				else
					mHandler.sendEmptyMessage(6);
			}
		});
	}
	
	private String addOrRemoveUserAction(String old, boolean add)
	{
		StringBuilder newStr = new StringBuilder("");
		if (!Util.isNullOrEmpty(old))
		{
			if (add)
			{
				newStr.append(old);
				newStr.append(",");
				newStr.append(Common.getUsername());
			}
			else
			{
				if (old.indexOf(Common.getUsername()+",") >= 0)
					newStr.append(old.replace(Common.getUsername()+",", ""));
				else if (old.indexOf(Common.getUsername()) >= 0)
					newStr.append(old.replace(Common.getUsername(), ""));
			}
		}
		else if (add)
		{
			newStr.append(Common.getUsername());
		}
		return Util.removeEmptyItem(newStr.toString().split(","));
	}
	
	private void deleteDialog(DialogInterface.OnClickListener lis)
	{
		Util.showConfirmCancelDialog(this, android.R.string.dialog_alert_title, "您确定要删除此评论？", lis);
	}
	
	private void showInput()
	{
		if (Common.isLogin())
		{
			edtComment.requestFocus();
			imm.showSoftInput(edtComment, InputMethodManager.SHOW_FORCED);
		}
	}
	
	private void hideInput()
	{
		edtComment.clearFocus();
		imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
	}
	
	private void addComment(String text)
	{
		Comments coms = new Comments();
		coms.user = Common.mUser.name;
		coms.name = Common.mUser.nickname;
		//coms.isVip = Common.isVipUser();
		coms.id = resId;
		coms.type = type;
		coms.title = title;
		coms.comment = text;
		coms.device = Util.getDeviceModel();
		coms.reference = reference;
		coms.date = dateformat.format(new Date());
		coms.save(new SaveListener<String>()
		{
			@Override
			public void done(String p1, BmobException e)
			{
				//if (e == null)
				//mHandler.sendEmptyMessage(2);
			}
		});
		if (!Util.isNullOrEmpty(reply))
		{
			Common.myInbox.addMessage(resId, reply, Common.mUser.nickname + "回复了你的评论：" + text, title, url, type);
		}
		mList.add(0, coms);
		mHandler.sendEmptyMessage(1);
	}
	
	private void updateComment(Comments com)
	{
		if (com.getObjectId() != null)
		{
			com.update(com.getObjectId(), new UpdateListener()
			{
				@Override
				public void done(BmobException p1)
				{
					if (p1 != null)
						ExceptionHandler.log("CommUpdate", p1.toString());
					mHandler.sendEmptyMessage(5);
				}
			});
		}
	}
	
	private void deleteComment(Comments com, final int p)
	{
		if (com.getObjectId() != null)
		{
			com.delete(com.getObjectId(), new UpdateListener()
			{
				@Override
				public void done(BmobException p1)
				{
					if (p1 == null) {
						mList.remove(p);
						mHandler.sendEmptyMessage(3);
					}
					else
						mHandler.sendEmptyMessage(4);
				}
			});
		}
	}
	
	private void clear()
	{
		reference = "";
		reply = "";
		edtComment.setHint(R.string.comm_hint);
	}

	@Override
	protected void onResume()
	{
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		super.onResume();
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					init();
					break;
				case 2:
					Util.toastShortMessage(getApplicationContext(), "发表成功");
					break;
				case 3:
					init();
					Util.toastShortMessage(getApplicationContext(), "删除成功");
					break;
				case 4:
					Util.toastShortMessage(getApplicationContext(), "删除失败");
					break;
				case 5:
					mAdapter.unLock();
					init();
					break;
				case 6:
					emptyView.setVisibility(View.VISIBLE);
					emptyView.setText("加载失败\n请检查你的网络连接");
					recyclerView.setVisibility(View.GONE);
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if (reference != null && reference.length() > 0)
			{
				clear();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
