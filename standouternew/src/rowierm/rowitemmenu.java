package rowierm;

public class rowitemmenu {
	private String contestname;
	private String legoimageurl;
	private Boolean isopen;
	private Boolean useopen;

	public rowitemmenu(String contestname, String legoimageurl, Boolean isopen,
			Boolean useopen) {
		this.contestname = contestname;
		this.legoimageurl = legoimageurl;
		this.isopen = isopen;
		this.useopen = useopen;
	}

	public String getcontestname() {
		return this.contestname;
	}

	public void setcontestname(String contestname) {
		this.contestname = contestname;
	}

	public String getlegoimageurl() {
		return this.legoimageurl;
	}

	public void setlegoimageurl(String legoimageurl) {
		this.legoimageurl = legoimageurl;
	}

	public Boolean getisopen() {
		return this.isopen;
	}

	public void setisopen(Boolean isopen) {
		this.isopen = isopen;
	}

	public Boolean getuseopen() {
		return this.useopen;
	}

	public void setuseopen(Boolean useopen) {
		this.useopen = useopen;
	}

}
