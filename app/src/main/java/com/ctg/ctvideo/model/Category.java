package com.ctg.ctvideo.model;

import java.util.List;

public class Category {
    private String title;
    private List<Video> vodList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Video> getVodList() {
        return vodList;
    }

    public void setVodList(List<Video> vodList) {
        this.vodList = vodList;
    }
}
