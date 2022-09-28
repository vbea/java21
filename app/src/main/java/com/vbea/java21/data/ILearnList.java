package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.classes.Util;

/**
 * Created by Vbe on 2018/12/10.
 */
public abstract class ILearnList {
    String host = "https://static-8bd3ee52-efd7-4a4a-be5e-ead8969bf63d.bspapp.com/app/";
    public String title;
    public String prefix;
    public String remark;
    public String url;
    public Boolean isTitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String tle) {
        title = tle;
    }

    public String getSubTitle() {
        return remark;
    }

    public void setSubTitle(String sub) {
        remark = sub;
    }

    public abstract String getPrefix();

    public void setPrefix(String pre) {
        prefix = pre;
    }

    public abstract String getUrl();

    public void setUrl(String u) {
        url = u;
    }

    public Boolean isTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean is) {
        isTitle = is;
    }

    public abstract boolean isRead();
}
