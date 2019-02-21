package com.vbea.java21.net;

import com.google.gson.JsonElement;

/**
 * Created by Vbe on 2019/01/22.
 */

public interface ApiCallback {
    void onSuccess(JsonElement data);
    void onFailed(String msg);
}
