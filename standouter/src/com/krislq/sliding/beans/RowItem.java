package com.krislq.sliding.beans;

public class RowItem {
    private String imageId;
    private String title;
    private String desc;
    private String VID;
    private int votecont;
    private String titlename;
    private boolean flag;
 
    public RowItem(String images, String title, String titlename,String desc,String VID,int votecont,boolean flag) {
        this.imageId = images;
        this.title = title;
        this.titlename = titlename;
        this.desc = desc;
        this.VID=VID;
        this.votecont=votecont;
        this.flag=flag;
    }
    public boolean getflag(){
    	return flag;
    }
    public void setglag(boolean flag) {
    	this.flag=flag;
    }
    public int getvotecont() {
        return votecont;
    }
    public void setvotecont(int votecont) {
    	this.votecont=votecont;
    }
    public String getVID() {
        return VID;
    }
    public void setVID(String VID) {
        this.VID = VID;
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
    public String getTitlename() {
        return titlename;
    }
    public void setTitlename(String titlename) {
        this.titlename = titlename;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}

