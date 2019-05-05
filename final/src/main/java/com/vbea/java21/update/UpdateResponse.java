package com.vbea.java21.update;

import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.Util;

/**
 * Created by Vbe on 2018/12/7.
 */
public class UpdateResponse {
    /*public BmobFile path;
    public String path_md5;
    public String version = null;
    public Integer version_i;
    public String updateLog = null;
    public long target_size;
    public String format_size;
    public Boolean isforce;

    public UpdateResponse(AppVersion app) {
        this.updateLog = app.getUpdate_log();
        this.version = app.getVersion();
        this.version_i = app.getVersion_i();
        if (this.version_i == null) {
            this.version_i = 0;
        }
        this.isforce = app.getIsforce();
        if (this.isforce == null) {
            this.isforce = false;
        }
        if (app.getPath() != null) {
            this.path = app.getPath();
            this.path_md5 = MD5Util.getMD5(this.path.getFileUrl());
        }
        try {
            this.target_size = Long.parseLong(app.getTarget_size());
            this.format_size = Util.getFormatSize(this.target_size);
        } catch (Exception var3) {
            this.target_size = 0L;
        }
    }*/
}
