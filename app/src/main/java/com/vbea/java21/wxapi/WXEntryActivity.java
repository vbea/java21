package com.vbea.java21.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.Util;

/**
 * Created by Vbe on 2018/10/10.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Override
    protected void before() {
        SocialShare.handleIntent(getIntent(), this);
    }

    @Override
    protected void after() {

    }

    @Override
    public void onReq(BaseReq baseReq) {
        finishAfterTransition();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("onResp", "" +baseResp.errCode);
        if (SocialShare.WX_TYPE == 1) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    SocialShare.onSuccess(((SendAuth.Resp) baseResp).code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Util.toastShortMessage(getApplicationContext(), getString(R.string.wx_auth_cancel));
                    SocialShare.onError();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Util.toastShortMessage(getApplicationContext(), getString(R.string.wx_auth_denied));
                    SocialShare.onError();
                    break;
            }
        } else {
            Util.toastShortMessage(getApplicationContext(), "分享成功");
        }
        finishAfterTransition();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        SocialShare.handleIntent(getIntent(), this);
    }
}
