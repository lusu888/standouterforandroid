package rowierm;

public class RowItem {
	private String imageurl;
	private String videowriter;
	private String videoname;
	private int votecount;
	private String videowiterid;
	private String videoid;
	private String videourl;
	private boolean showflag;

	public RowItem(String imageurl, String videowriter, String videoname,
			int votecount, String videowiterid, String videoid,
			String videourl, boolean showflag) {
		this.imageurl = imageurl;
		this.videowriter = videowriter;
		this.videoname = videoname;
		this.votecount = votecount;
		this.videowiterid = videowiterid;
		this.videoid = videoid;
		this.videourl = videourl;
		this.showflag = showflag;
	}

	public boolean getflag() {
		return showflag;
	}

	public void setglag(boolean showflag) {
		this.showflag = showflag;
	}

	public String getimageurl() {
		return imageurl;
	}

	public void setimageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getvideowriterl() {
		return videowriter;
	}

	public void setvideowriter(String videowriter) {
		this.videowriter = videowriter;
	}

	public String getvideoname() {
		return videoname;
	}

	public void setvideoname(String videoname) {
		this.videoname = videoname;
	}

	public int getvotecount() {
		return votecount;
	}

	public void setvotecountl(int votecount) {
		this.votecount = votecount;
	}

	public String getvideowiterid() {
		return videowiterid;
	}

	public void setvideowiterid(String videowiterid) {
		this.videowiterid = videowiterid;
	}

	public String getvideoid() {
		return videoid;
	}

	public void setvideoid(String videoid) {
		this.videoid = videoid;
	}

	public String getvideourl() {
		return videourl;
	}

	public void setvideourl(String videourl) {
		this.videourl = videourl;
	}

	@Override
	public String toString() {
		return imageurl + "\n" + videowriter;
	}
}
