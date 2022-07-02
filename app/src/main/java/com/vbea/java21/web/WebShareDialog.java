package com.vbea.java21.web;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.vbea.java21.R;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.Util;
import com.vbea.java21.ui.HtmlViewer;
import com.vbes.util.VbeUtil;
import com.vbes.util.view.MyAlertDialog;

/**
 * Created by Vbe on 2021/3/11.
 */
public class WebShareDialog implements IUiListener {
    private String shareUrl;
    private String shareTitle;
    private Activity mActivity;
    private BottomSheetDialog mBSDialog;
    private String[] shareModel = {"网页标题", "你有一份惊喜", "婚礼纪"};
    private String[] shareTitles = {"", "叮！你有一份惊喜待查收~", "吴德彬和肖璐的婚礼邀请"};
    private String[] shareMessage = {"", "点击查收~(✪ω✪)~", "我们将在6月14日举行婚礼，诚挚邀请您的到来"};

    public WebShareDialog(Activity activity) {
        mActivity = activity;
        mBSDialog = new BottomSheetDialog(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.sharelayout, null);
        LinearLayout share_qq = (LinearLayout) view.findViewById(R.id.btn_share_qq);
        LinearLayout share_qzone = (LinearLayout) view.findViewById(R.id.btn_share_qzone);
        LinearLayout share_wx = (LinearLayout) view.findViewById(R.id.btn_share_wx);
        LinearLayout share_wxpy = (LinearLayout) view.findViewById(R.id.btn_share_wxline);
        LinearLayout share_sina = (LinearLayout) view.findViewById(R.id.btn_share_sina);
        LinearLayout share_web = (LinearLayout) view.findViewById(R.id.btn_share_browser);
        LinearLayout share_link = (LinearLayout) view.findViewById(R.id.btn_share_copylink);
        LinearLayout share_more = (LinearLayout) view.findViewById(R.id.btn_share_more);
        mBSDialog.setContentView(view);

        share_qq.setOnClickListener(new QQShareListener());
        share_qzone.setOnClickListener(new QQShareListener());

        share_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                intent.setType("text/html");
                mActivity.startActivity(intent);
                mBSDialog.dismiss();
            }
        });

        share_web.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(shareUrl);
                intent.setData(uri);
                mActivity.startActivity(intent);
            }
        });

        share_link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBSDialog.dismiss();
                ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newRawUri("url", Uri.parse(shareUrl)));
                }
                Util.toastShortMessage(mActivity, "已复制到剪贴板");
            }
        });

        share_wx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (shareUrl.contains("wedding")) {
                    new MyAlertDialog(activity).setTitle("设置分享模板").setItems(shareModel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                SocialShare.shareToWeixin(shareTitle, shareTitle, shareUrl, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.share_wedding));
                            } else {
                                SocialShare.shareToWeixin(shareTitles[which], shareMessage[which], shareUrl, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.share_wedding));
                            }
                        }
                    }).show();
                } else {
                    SocialShare.shareToWeixin(shareTitle, shareTitle, shareUrl, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.web_share_icon));
                }
            }
        });

        share_wxpy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SocialShare.shareToWeixinZone(shareTitle, shareTitle, shareUrl, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.web_share_icon));
            }
        });

        share_sina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SocialShare.shareToWeixinFavorite(shareTitle, shareTitle, shareUrl, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.web_share_icon));
            }
        });
    }

    public void showShare(String title, String url) {
        shareTitle = title;
        shareUrl = url;
        mBSDialog.show();
    }

    public void result(Intent data) {
        Tencent.handleResultData(data, this);
    }

    public void hide() {
        if (mBSDialog != null)
            mBSDialog.dismiss();
    }

    @Override
    public void onCancel() {
        Util.toastShortMessage(mActivity, "分享取消");
    }

    @Override
    public void onComplete(Object response) {
        Util.toastShortMessage(mActivity, "分享成功");
    }

    @Override
    public void onError(UiError e) {
        //Util.toastShortMessage(getApplicationContext(), "onError: " + e.errorMessage + "e");
    }

    class QQShareListener implements View.OnClickListener {
        public void onClick(View v) {
            Bundle params = new Bundle();
            params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareTitle);
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mActivity.getString(R.string.app_name));
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            SocialShare.shareToQQ(mActivity, params, v.getId() == R.id.btn_share_qzone, WebShareDialog.this);
        }
    }
}
