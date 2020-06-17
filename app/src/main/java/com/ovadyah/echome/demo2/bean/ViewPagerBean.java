package com.ovadyah.echome.demo2.bean;

public class ViewPagerBean implements MultiBaseBean {

    public static final int VIEW_PAGE_TYPE = 2;

    @Override
    public int itemType() {
        return VIEW_PAGE_TYPE;
    }
}
