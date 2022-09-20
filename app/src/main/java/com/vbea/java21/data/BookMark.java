package com.vbea.java21.data;

import java.io.Serializable;

/**
 * Created by Vbe on 2022/9/19.
 */
public class BookMark implements Serializable {
    private Integer id;
    private String title;
    private String url;

    public String getId() {
        if (id != null)
            return String.valueOf(id);
        else
            return "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
