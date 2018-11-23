package com.vbea.java21;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.SocialShare;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQShareMessage extends BaseActivity
{
    private View mContainer_title, mContainer_summary, mContainer_audioUrl, mContainer_targetUrl, mContainer_imgUrl, mContainer_appName;
    private TextView title, imageUrl, targetUrl, summary, appName = null;// app名称，用于手Q显示返回
    private RadioButton mRadioBtn_localImage, mRadioBtn_netImage, mRadioBtnShareTypeDefault, mRadioBtnShareTypeAudio, mRadioBtnShareTypeImg, mRadioBtnShareTypeApp;
    private CheckBox mCheckBox_qzoneAutoOpen, mCheckBox_qzoneItemHide;
    private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
    private EditText mEditTextAudioUrl;
    private int mExtarFlag = 0x00;

	@Override
	protected void before()
	{
		setContentView(R.layout.qq_share_activity);
	}

	@Override
    protected void after()
	{
		enableBackButton();
        title = bind(R.id.shareqq_title);
        imageUrl = bind(R.id.shareqq_image_url);
        targetUrl = bind(R.id.shareqq_target_url);
        summary = bind(R.id.shareqq_summary);
        appName = bind(R.id.shareqq_app_name);
		Button btnCommit = bind(R.id.shareqq_commit);
        mEditTextAudioUrl = bind(R.id.et_shareqq_audioUrl);
        // mContainer_qzone_special = bind(R.id.qzone_specail_radio_container);
        mContainer_title = bind(R.id.qqshare_title_container);
        mContainer_summary = bind(R.id.qqshare_summary_container);
        mContainer_audioUrl = bind(R.id.qqshare_audioUrl_container);
        mContainer_targetUrl = bind(R.id.qqshare_targetUrl_container);
        mContainer_imgUrl = bind(R.id.qqshare_imageUrl_container);
        mContainer_appName = bind(R.id.qqshare_appName_container);
        mRadioBtn_localImage = bind(R.id.radioBtn_local_image);
		mRadioBtn_netImage = bind(R.id.radioBtn_net_image);
        mRadioBtnShareTypeDefault = bind(R.id.radioBtn_share_type_default);
        mRadioBtnShareTypeAudio = bind(R.id.radioBtn_share_type_audio);
        mRadioBtnShareTypeImg = bind(R.id.radioBtn_share_type_image);
        mRadioBtnShareTypeApp = bind(R.id.radioBtn_share_type_app);
        mCheckBox_qzoneItemHide = bind(R.id.checkBox_qzone_item_hide);
        mCheckBox_qzoneAutoOpen = bind(R.id.checkBox_qzone_auto_open);
		initShareUI(shareType);
			
		btnCommit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final Bundle params = new Bundle();
				if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE)
				{
					params.putString(QQShare.SHARE_TO_QQ_TITLE, title.getText().toString());
					params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl.getText().toString());
					params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary.getText().toString());
                }
				if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE)
					params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl.getText().toString());
				else
					params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
				params.putString(shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE ? QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL : QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
				params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName.getText().toString());
				params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
				params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
				if (shareType == QQShare.SHARE_TO_QQ_TYPE_AUDIO)
				{
					params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, mEditTextAudioUrl.getText().toString());
				}
				if ((mExtarFlag & QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN) != 0)
				{
					Util.toastShortMessage(getApplicationContext(),"在好友选择列表会自动打开分享到qzone的弹窗");
				}
				else if ((mExtarFlag & QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE) != 0)
				{
					Util.toastShortMessage(getApplicationContext(),"在好友选择列表隐藏了qzone分享选项");
				}
				SocialShare.shareToQQ(QQShareMessage.this, params, qqShareListener);
				return;
			}
		});
		
		mRadioBtn_netImage.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE)
				{
					// 纯图分享只能支持本地图片
					mRadioBtn_localImage.setChecked(true);
					startPickLocaleImage();
					Util.toastShortMessage(getApplicationContext(), "纯图分享只支持本地图片");
				}
				else
					imageUrl.setText("");
			}
		});
		
		mRadioBtn_localImage.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				startPickLocaleImage();
			}
		});
		
		mRadioBtnShareTypeAudio.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO;
				initShareUI(shareType);
			}
		});
		
		mRadioBtnShareTypeDefault.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
				initShareUI(shareType);
			}
		});
		
		mRadioBtnShareTypeImg.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE;
				initShareUI(shareType);
			}
		});
		
		mRadioBtnShareTypeApp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				shareType = QQShare.SHARE_TO_QQ_TYPE_APP;
				initShareUI(shareType);
			}
		});
        mCheckBox_qzoneAutoOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (mCheckBox_qzoneItemHide.isChecked())
				{
					mCheckBox_qzoneAutoOpen.setChecked(false);
					Util.toastShortMessage(getApplicationContext(), "Qzone隐藏选项打开时, 不能自动弹Qzone窗口");
				}
				else
				{
					if (isChecked) // 最后一个二进制位置为1, 其他位不变
						mExtarFlag |= QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
					else // 最后一个二进制位置为0, 其他位不变
						mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
				}
			}
		});
		
		mCheckBox_qzoneItemHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (mCheckBox_qzoneAutoOpen.isChecked()) {
					mCheckBox_qzoneItemHide.setChecked(false);
					Util.toastShortMessage(getApplicationContext(), "Qzone自动弹窗选项打开时, 不能隐藏Qzone Item.");
				}
				else
				{
					if (isChecked) // 倒数第二位置为1, 其他位不变
						mExtarFlag |= QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
					else // 倒数第二位置为0, 其他位不变
						mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
				}
			}
		});
    }

    /**
     * 初始化UI
     * @param shareType
     */
    private void initShareUI(int shareType)
	{
        switch (shareType) {
            case QQShare.SHARE_TO_QQ_TYPE_IMAGE:
                mContainer_title.setVisibility(View.GONE);
                mContainer_summary.setVisibility(View.GONE);
                mContainer_audioUrl.setVisibility(View.GONE);
                mContainer_targetUrl.setVisibility(View.GONE);
                mContainer_imgUrl.setVisibility(View.VISIBLE);
                mContainer_appName.setVisibility(View.GONE);
                mRadioBtn_localImage.setChecked(true);
                imageUrl.setText(null);
                //startPickLocaleImage(this);
                targetUrl.setVisibility(View.VISIBLE);
                return;
            case QQShare.SHARE_TO_QQ_TYPE_AUDIO:
                mContainer_audioUrl.setVisibility(View.VISIBLE);
                title.setText("不要每天陪我聊天因为我害怕会喜欢上你");
                imageUrl.setText("http://y.gtimg.cn/music/photo_new/T002R300x300M000003KIU6V02sS7C.jpg?max_age=2592000");
                mEditTextAudioUrl.setText("http://ws.stream.qqmusic.qq.com/C100000kuo2H2xJqfA.m4a?fromtag=0");
                targetUrl.setText("http://c.y.qq.com/v8/playsong.html?songid=109325260&songmid=000kuo2H2xJqfA&songtype=0&source=mqq&_wv=1");
                summary.setText("乔紫乔");
                appName.setText("QQ音乐");
                targetUrl.setVisibility(View.VISIBLE);
                break;
            case QQShare.SHARE_TO_QQ_TYPE_DEFAULT:
                targetUrl.setVisibility(View.VISIBLE);
                mContainer_audioUrl.setVisibility(View.GONE);
                title.setText("分享标题");
                imageUrl.setText("");
                targetUrl.setText("http://www.qq.com");
                summary.setText("分享内容");
                appName.setText("21天学通Java");
                break;
            case QQShare.SHARE_TO_QQ_TYPE_APP:
                targetUrl.setVisibility(View.GONE);
                title.setText("【推荐】《21天学通Java》");
                summary.setText("学习|11.59MB|785万次下载|4.6/5星");
                imageUrl.setText("http://url.cn/424xgoi");
                targetUrl.setText("http://url.cn/424xgot");
                break;
        }
        mContainer_title.setVisibility(View.VISIBLE);
        mContainer_summary.setVisibility(View.VISIBLE);
        mContainer_targetUrl.setVisibility(View.VISIBLE);
        mContainer_imgUrl.setVisibility(View.VISIBLE);
        mContainer_appName.setVisibility(View.VISIBLE);
        mRadioBtn_netImage.setChecked(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{ 
        if (requestCode == Constants.REQUEST_QQ_SHARE)
		{
        	Tencent.onActivityResultData(requestCode,resultCode,data,qqShareListener);
        }
		else if (requestCode == 0)
		{
        	String path = null;
            if (resultCode == Activity.RESULT_OK)
			{
                if (data != null && data.getData() != null)
				{
                    // 根据返回的URI获取对应的SQLite信息
                    Uri uri = data.getData();
                    path = Util.getPath(this, uri);
                }
            }
            if (path != null)
                imageUrl.setText(path);
            else
			{
            	if(shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE)
            		Util.toastShortMessage(getApplicationContext(), "请重新选择图片");
            }
        }
    }

    private final void startPickLocaleImage()
	{
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        if (android.os.Build.VERSION.SDK_INT >= Util.Build_VERSION_KITKAT)
            intent.setAction(Util.ACTION_OPEN_DOCUMENT);
        else
            intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
    }

	IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel()
		{
            if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
                Util.toastShortMessage(getApplicationContext(), "onCancel: ");
            }
        }
        @Override
        public void onComplete(Object response)
		{
            // TODO Auto-generated method stub
            Util.toastShortMessage(getApplicationContext(), "onComplete: " + response.toString());
        }
        @Override
        public void onError(UiError e)
		{
            // TODO Auto-generated method stub
            Util.toastShortMessage(getApplicationContext(), "onError: " + e.errorMessage + "e");
        }
    };

    @Override
    protected void onDestroy()
	{
        super.onDestroy();
    }
}
