package com.vbea.java21.update;

import cn.bmob.v3.BmobObject;

/**
 * Created by Vbe on 2019/12/5.
 */
public class AppVersion extends BmobObject {
    private String update_log;
    private String version;
    private Integer version_i;
    private Boolean isforce;
    private String link;
    private String path_md5;
    private String target_size;
    private String platform;
    private String channel;
    private String old_code;
    private Integer download;
    private Integer install;

    public AppVersion() {
    }

    public String getUpdate_log() {
        return this.update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getVersion_i() {
        return this.version_i;
    }

    public void setVersion_i(Integer version_i) {
        this.version_i = version_i;
    }

    public Boolean getIsforce() {
        return this.isforce;
    }

    public void setIsforce(Boolean isforce) {
        this.isforce = isforce;
    }

    public String getTarget_size() {
        return this.target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOld_code() {
        return old_code;
    }

    public void setOld_code(String old_code) {
        this.old_code = old_code;
    }

    public String getPath_md5() {
        return path_md5;
    }

    public void setPath_md5(String path_md5) {
        this.path_md5 = path_md5;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public void addDownload() {
        this.download = download + 1;
    }

    public Integer getInstall() {
        return install;
    }

    public void setInstall(Integer install) {
        this.install = install;
    }

    public void addInstall() {
        this.install = install + 1;
    }
}
