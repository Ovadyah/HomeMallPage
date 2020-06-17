package com.ovadyah.echome.demo2.bean;

/**
 * 设置列表对象数据
 */
public class DataItemBean implements MultiBaseBean {

    public static final int NORMAL_TYPE = 1;

    public String text;
    public String url;
    public int imgRes;

    public DataItemBean(String text, String url,int imgRes) {
        this.text = text;
        this.url = url;
        this.imgRes = imgRes;
    }

    @Override
    public int itemType() {
        return NORMAL_TYPE;
    }
}
