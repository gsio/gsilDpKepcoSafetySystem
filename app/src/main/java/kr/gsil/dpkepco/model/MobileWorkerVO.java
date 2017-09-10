package kr.gsil.dpkepco.model;

import android.graphics.Bitmap;

public class MobileWorkerVO {
	private String id;
	private String phone;
	private String gubun;
	private String name;
	private String jumin;
	private String country;
	private String passno;
	private String first;
	private String t_id;
	private String cont_id;
	private String singoyn;
	private String cname;
	private String t_name;
	private int check = 0;
	private int check2 = 0;
	
	private int ju = 0;
	private int ya = 0;
	
	private String mon;
	private String after;
	private String content;
	private String workdate;
	private String mw_id;
	private String tagid;
	private String place;
	private String firstdate;
	private String title;
	private String option;
	private String delyn;
	private String delcontent;
	private String startyn;
	private String status;
	private String t_gubun;
	private String pno;
	private String edudate;
	
	private String license;
	private String memo;
	private String u_id;
	
	
	private Bitmap image;
	private String imageName;
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getMemo() {
		if( memo == null ) memo = "";
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getEdudate() {
		return edudate;
	}
	public void setEdudate(String edudate) {
		this.edudate = edudate;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public String getT_gubun() {
		return t_gubun;
	}
	public void setT_gubun(String t_gubun) {
		this.t_gubun = t_gubun;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartyn() {
		return startyn;
	}
	public void setStartyn(String startyn) {
		this.startyn = startyn;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	public String getDelcontent() {
		return delcontent;
	}
	public void setDelcontent(String delcontent) {
		this.delcontent = delcontent;
	}
	public int getJu() {
		return ju;
	}
	public void setJu(int ju) {
		this.ju = ju;
	}
	public int getYa() {
		return ya;
	}
	public void setYa(int ya) {
		this.ya = ya;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getFirstdate() {
		return firstdate;
	}
	public void setFirstdate(String firstdate) {
		this.firstdate = firstdate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCheck2() {
		return check2;
	}
	public void setCheck2(int check2) {
		this.check2 = check2;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	public String getMw_id() {
		return mw_id;
	}
	public void setMw_id(String mw_id) {
		this.mw_id = mw_id;
	}
	public String getMon() {
		return mon;
	}
	public void setMon(String mon) {
		this.mon = mon;
	}
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	public int getCheck() {
		return check;
	}
	public void setCheck(int check) {
		this.check = check;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getT_name() {
		return t_name;
	}
	public void setT_name(String t_name) {
		this.t_name = t_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJumin() {
		return jumin;
	}
	public void setJumin(String jumin) {
		this.jumin = jumin;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPassno() {
		return passno;
	}
	public void setPassno(String passno) {
		this.passno = passno;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getT_id() {
		return t_id;
	}
	public void setT_id(String t_id) {
		this.t_id = t_id;
	}
	public String getCont_id() {
		return cont_id;
	}
	public void setCont_id(String cont_id) {
		this.cont_id = cont_id;
	}
	public String getSingoyn() {
		return singoyn;
	}
	public void setSingoyn(String singoyn) {
		this.singoyn = singoyn;
	}
	
	
}
