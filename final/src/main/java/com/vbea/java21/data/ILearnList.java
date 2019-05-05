package com.vbea.java21.data;

/**
 * Created by Vbe on 2018/12/10.
 */
public interface ILearnList {
    String getObjectId();
    String getTitle();
    String getSubTitle();
    String getPrefix();
    String getUrl();
    Integer getOrder();
    Boolean isTitle();
    boolean isRead();
}
