package com.vbea.java21.web;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vbes.util.VbeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vbe on 2021/1/18.
 */
public class UriScheme {
    private final List<Scheme> schemeList = new ArrayList<>();

    public UriScheme(Context context) {
        init(context);
    }

    public void init(Context context) {
        AssetManager am = context.getAssets();
        try {
            String json = VbeUtil.ReadFileToString(am.open("web/schemes.json"));
            if (json != null) {
                Gson gson = new Gson();
                JsonParser jsp = new JsonParser();
                JsonArray jArray = jsp.parse(json).getAsJsonArray();
                for (JsonElement obj : jArray) {
                    schemeList.add(gson.fromJson(obj, Scheme.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAppNameByUrl(String url) {
        for (Scheme item : schemeList) {
            String scheme = item.getScheme();
            if (url.startsWith(scheme)) {
                return getAppNameByScheme(item.getName());
            }
        }
        return getAppNameByScheme("");
    }

    private String getAppNameByScheme(String scheme) {
        if (VbeUtil.isNullOrEmpty(scheme)) {
            return "网页想要打开第三方应用，是否允许？";
        } else {
            return "网页请求打开“" + scheme + "”，是否允许？";
        }
    }

    static class Scheme {
        String name;
        String scheme;

        public String getName() {
            if (scheme != null) {
                return name;
            } else {
                return "";
            }
        }

        public String getScheme() {
            if (scheme != null) {
                return scheme;
            } else {
                return "";
            }
        }
    }
}
