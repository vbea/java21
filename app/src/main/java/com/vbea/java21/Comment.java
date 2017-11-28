package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Button; 
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

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
import org.json.JSONObject;

public class Comment extends AppCompatActivity
{
	String resId, title, reference, url, reply;
	Integer type;
	TextView emptyView;
	EditText edtComment;
	Button btnComment;
	List<Comments> mList;
	RecyclerView recyclerView;
	CommentAdapter mAdapter;
	SimpleDateFormat dateformat;
	InputMethodManager imm;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		recyclerView = (RecyclerView) findViewById(R.id.com_recyclerView);
		edtComment = (EditText) findViewById(R.id.edt_comment);
		btnComment = (Button) findViewById(R.id.btnComment);
		emptyView = (TextView) findViewById(R.id.comment_emptyview);
		title = getIntent().getStringExtra("title");
		resId = getIntent().getStringExtra("id");
		url = getIntent().getStringExtra("url");
		type = getIntent().getIntExtra("type", 0);
		if (resId == null || type == 0)
		{
			Util.toastShortMessage(this, "不支持的参数");
			supportFinishAfterTransition();
		}
		else
			getData();
		if (title != null)
			tool.setTitle(title + " 的评论");
		reference = "";
		reply = "";
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		dateformat = new SimpleDateFormat("M-d HH:mm");
		mList = new ArrayList<Comments>();
		mAdapter = new CommentAdapter(mList);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
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
				btnComment.setEnabled(count > 0);
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
				edtComment.clearFocus();
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
				edtComment.requestFocus();
				imm.showSoftInput(edtComment, InputMethodManager.SHOW_FORCED);
			}

			@Override
			public void onDelete(final String id, final int position)
			{
				deleteDialog(new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						deleteComment(id, position);
					}
				});
			}
		});
	}
	
	private void init()
	{
		imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
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
	
	private void getData()
	{
		emptyView.setText("请稍候...");
		BmobQuery<Comments> sql = new BmobQuery<Comments>();
		sql.addWhereEqualTo("id", resId);
		sql.addWhereEqualTo("type", type);
		sql.order("-createdAt");
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
			}
		});
	}
	
	private void addComment(String text)
	{
		Comments coms = new Comments();
		coms.user = Common.mUser.name;
		coms.name = Common.mUser.nickname;
		coms.isVip = Common.isVipUser();
		coms.id = resId;
		coms.type = type;
		coms.title = title;
		coms.url = url;
		coms.comment = text;
		coms.reply = reply;
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
		mList.add(0, coms);
		mHandler.sendEmptyMessage(1);
	}
	
	private void deleteDialog(DialogInterface.OnClickListener lis)
	{
		Util.showConfirmCancelDialog(this, android.R.string.dialog_alert_title, "您确定要删除此评论？", lis);
	}
	
	private void deleteComment(String id, int p)
	{
		Comments com = mList.get(p);
		if (com.getObjectId() == id)
		{
			mList.remove(p);
			com.delete(id, new UpdateListener()
			{
				@Override
				public void done(BmobException p1)
				{
					if (p1 == null)
						mHandler.sendEmptyMessage(3);
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
