package com.ctg.ctvideo.model;

public class Video {
    public String url;
    public String title;
    public String pic;

    public Video(){
    }

    public Video(String title, String url, String pic) {
        this.title = title;
        this.url = url;
        this.pic = pic;
    }
}
