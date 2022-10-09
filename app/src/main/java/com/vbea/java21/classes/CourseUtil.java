package com.vbea.java21.classes;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vbea.java21.data.ILearnList;
import com.vbes.util.VbeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vbe on 2022/9/27.
 */
public class CourseUtil {
    String path;
    AssetManager assetManager;
    public CourseUtil(Context context, String assetPath) {
        path = assetPath;
        assetManager = context.getAssets();
    }

    public <T extends ILearnList> List<T> getCourseList(Class<T> t) {
        List<T> list = new ArrayList<>();
        try {
            String json = Util.ReadFileToString(assetManager.open("course/" + path));
            if (!VbeUtil.isNullOrEmpty(json)) {
                Gson gson = new Gson();
                JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
                for (JsonElement obj : jArray) {
                    T item = gson.fromJson(obj, t);
                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
