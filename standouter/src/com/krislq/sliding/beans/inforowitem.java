package com.krislq.sliding.beans;

public class inforowitem {
    private String imageId;
    private String title;
    private String desc;
    private String VID;
    private int votecont;
 
    public inforowitem(String images, String title, String desc) {
        this.imageId = images;
        this.title = title;
        this.desc = desc;
        this.VID=VID;
        this.votecont=votecont;
    }
   
    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}

