package com.vbea.java21.classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

public class SocialShare {
    public static Tencent mTencent;
    public static IWXAPI mWeiXin, wxLogin;
    //public static AuthInfo mSina;
    //public static IWeiboShareAPI mSinaShare;
    public static final String QQ_AppKey = "1101326556";
    public static final String Sina_AppKey = "309212800";
    public static final String WeiXin_AppKey = "wx4073ed26b640eb16";//aa6bf263bb94528f3674bdb308762016";
    public static final String WX_APPID = "wxbc12a3bddf2ea5ee";
    public static final String WX_SECRET = "6fe27b0103ef4c7b3094d1ade741ca6f";
    public static final String SCOPE = //Sina_应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private static OnWxLoginListener listener;
    public static int WX_TYPE = 0;//0:share 1:login

    public static void onStart(Context context) {
        try {
            if (mTencent == null)
                mTencent = Tencent.createInstance(QQ_AppKey, context);
            if (mWeiXin == null) {
                mWeiXin = WXAPIFactory.createWXAPI(context, WeiXin_AppKey, true);
                mWeiXin.registerApp(WeiXin_AppKey);
            }
            if (wxLogin == null) {
                wxLogin = WXAPIFactory.createWXAPI(context, WX_APPID, true);
                wxLogin.registerApp(WX_APPID);
            }
            /*if (mSina == null)
                mSina = new AuthInfo(context, Sina_AppKey, "https://api.weibo.com/oauth2/default.html", SCOPE);
            if (mSinaShare == null)
                mSinaShare = WeiboShareSDK.createWeiboAPI(context, Sina_AppKey);*/
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public static boolean isWXInstall() {
        return mWeiXin.isWXAppInstalled();
    }

    public static void shareToQQ(Activity activity, Bundle params, boolean isQzone, IUiListener qqShareListener) {
        if (isQzone)
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00 | QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        else
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
        shareToQQ(activity, params, qqShareListener);
    }

    public static void shareToQQ(Activity activity, Bundle params, IUiListener qqShareListener) {
        if (mTencent != null)
            mTencent.shareToQQ(activity, params, qqShareListener);
    }

    public static void shareToWeixin(String title, String desc, String url, Bitmap bitmap) {
        shareToWeixin(title, desc, url, bitmap, SendMessageToWX.Req.WXSceneSession);
    }

    public static void shareToWeixinZone(String title, String desc, String url, Bitmap bitmap) {
        shareToWeixin(title, desc, url, bitmap, SendMessageToWX.Req.WXSceneTimeline);
    }

    public static void shareToWeixinFavorite(String title, String desc, String url, Bitmap bitmap) {
        shareToWeixin(title, desc, url, bitmap, SendMessageToWX.Req.WXSceneFavorite);
    }

    public static void shareToWeixin(String title, String desc, String url, Bitmap bt, int scene) {
        if (mWeiXin == null)
            return;
        WX_TYPE = 0;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        try {
            if (bt != null)
                msg.thumbData = Util.bmpToByteArray(bt, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = scene;
            mWeiXin.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/*public static void shareToSina(Activity act, String text)
	{
		if (mSinaShare != null)
		{
			TextObject txt = new TextObject();
			txt.text = text;
			shareToSina(act, txt);
		}
	}
	
	public static void shareToSina(Activity act, TextObject text)
	{
		if (mSinaShare != null)
		{
			WeiboMessage msg = new WeiboMessage();
			msg.mediaObject = text;
			SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
			request.transaction = String.valueOf(System.currentTimeMillis());
			request.message = msg;
			mSinaShare.sendRequest(act, request);
		}
	}
	
	public static void shareToSina(Activity act, String text, WeiboAuthListener lis)
	{
		if (mSinaShare != null)
		{
			TextObject txt = new TextObject();
			txt.text = text;
			shareToSina(act, txt, "", lis);
		}
	}
	
	public static void shareToSina(Activity act, TextObject text, String token, WeiboAuthListener lis)
	{
		if (mSinaShare != null)
		{
			WeiboMessage msg = new WeiboMessage();
			msg.mediaObject = text;
			SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
			request.transaction = String.valueOf(System.currentTimeMillis());
			request.message = msg;
			mSinaShare.sendRequest(act, request, mSina, token, lis);
		}
	}*/

    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 20190122-vbe
     * code-微信登录
     */
    public static void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        if (WX_TYPE == 0)
            mWeiXin.handleIntent(intent, handler);
        else
            wxLogin.handleIntent(intent, handler);
    }

    public static void loginFromWeixin(OnWxLoginListener lis) {
        WX_TYPE = 1;
        listener = lis;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "java21";
        wxLogin.sendReq(req);
    }

    public static void onSuccess(String code) {
        if (listener != null)
            listener.onSuccess(code);
    }

    public static void onError() {
        if (listener != null)
            listener.onError();
    }

    public interface OnWxLoginListener {
        void onSuccess(String code);

        void onError();
    }
}
