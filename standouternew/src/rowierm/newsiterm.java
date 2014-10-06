package rowierm;

public class newsiterm {
	private String photourl;
	private String sendername;
	private String message;
	private String senderid;
	private String msgtype;

	public newsiterm(String photourl, String sendername, String message,
			String senderid, String msgtype) {
		this.photourl = photourl;
		this.sendername = sendername;
		this.senderid = senderid;
		this.message = message;
		this.msgtype = msgtype;
	}

	public void setphotourl(String photourl) {
		this.photourl = photourl;
	}

	public String getphotourl() {
		return this.photourl;
	}

	public void setsendername(String sendername) {
		this.sendername = sendername;
	}

	public String getsendername() {
		return this.sendername;
	}

	public void setsenderid(String senderid) {
		this.senderid = senderid;
	}

	public String getsenderid() {
		return this.senderid;
	}

	public void setmessage(String message) {
		this.message = message;
	}

	public String getmessage() {
		return this.message;
	}

	public void setmsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getmsgtype() {
		return this.msgtype;
	}

}
