package com.vbea.java21.net;

import java.io.IOException;
import java.util.Map;

import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
/**
 * Created by Vbe on 2019/1/22.
 */
public class ApiRequest {
    /*private OkHttpClient client;
    private Gson gson;
    private String baseUrl = "";

    public ApiRequest() {
        client = new OkHttpClient();
        gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    }

    public ApiRequest(String _baseUrl) {
        this();
        baseUrl = _baseUrl;
    }

    public void request(Method method, String url, Map<String, Object> query, ApiCallback callback) {
        Request request = null;
        if (method == Method.HTTP_GET) {
            request = new Request.Builder().url(baseUrl + url + getUrlParams(query)).get().build();
        } else if (method == Method.HTTP_POST) {
            request = new Request.Builder().url(baseUrl + url).post(getRequestBody(query)).build();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        //ExceptionHandler.log("responseApi(" + url + ")", res);
                        JsonElement json = string2Json(res);
                        if (json != null) {
                            callback.onSuccess(json);
                        }
                    } else
                        callback.onFailed("err:" + response.code());
                } catch (Exception e) {
                    callback.onFailed(e.toString());
                }
            }
        });
    }

    public void request(String url, Map<String, Object> query, ApiCallback listener) {
        request(Method.HTTP_GET, url, query, listener);
    }

    private String getUrlParams(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (Util.isNullOrEmpty(sb.toString())) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return sb.toString();
    }

    private RequestBody getRequestBody(Map map) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json2String(map));
    }

    private String json2String(Object obj) {
        return gson.toJson(obj);
    }

    public JsonElement string2Json(String str) throws Exception {
        return new JsonParser().parse(str);
    }

    public <T> T string2Bean(String str, Class<T> cls) {
        return gson.fromJson(str, cls);
    }

    public <T> T json2Bean(JsonElement json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public enum Method {
        HTTP_GET,
        HTTP_POST
    }*/
}
