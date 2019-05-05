package com.vbea.java21.net;

/**
 * Created by Vbe on 2019/01/22.
 */

public interface ApiCallback {
    void onSuccess(String data);
    void onFailed(String msg);
}
