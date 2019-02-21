package com.vbea.java21.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.vbea.java21.R;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.Util;

/**
 * Created by Vbe on 2018/10/10.
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocialShare.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SocialShare.onSuccess(((SendAuth.Resp)baseResp).code);
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
        finishAndRemoveTask();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        SocialShare.handleIntent(getIntent(), this);
    }
}
