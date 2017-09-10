package kr.gsil.dpkepco.model;

import android.graphics.Bitmap;

public class MobileRiskVO {
	private String id;
	private String site_id;
	private String cont_id;
	private String year;
	private String month;
	private String chasu;
	private String status;
	private String reason;
	private String u_id;
	private String spot_id;
	
	private String c_id;
	private String r_id;
	private String code;
	private String work_detail;
	private String work_detail_code;
	private String work_level;
	private String work_level_code;
	private String device;
	private String work_date;
	private String work_place;
	private String worker;
	private String risk_content;
	private String d_worker;
	private String d_kind;
	private String bin;
	private String kang;
	private String manage;
	private String manager;
	private String gubun;
	
	private String cname;
	private String sname;
	private String startdate;
	private String enddate;
	private String statusname;
	
	private String sub_yesno;
	private String sub_reason;
	private String sub_image;
	private String sub_user;
	private String main_yesno;
	private String main_reason;
	private String main_image;
	private String main_user;
	private String command;
	private String check_id;
	private String inspect_id;
	private String mea_id;
	private String dataimg;
	private String duedate;
	private String check_user_id;
	
	private String name;
	private String checkdate;
	
	private Bitmap image = null;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	public String getCheck_user_id() {
		return check_user_id;
	}
	public void setCheck_user_id(String check_user_id) {
		this.check_user_id = check_user_id;
	}
	public String getDataimg() {
		return dataimg;
	}
	public void setDataimg(String dataimg) {
		this.dataimg = dataimg;
	}
	public String getMea_id() {
		return mea_id;
	}
	public void setMea_id(String mea_id) {
		this.mea_id = mea_id;
	}
	public String getInspect_id() {
		return inspect_id;
	}
	public void setInspect_id(String inspect_id) {
		this.inspect_id = inspect_id;
	}
	public String getCheckdate() {
		return checkdate;
	}
	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getCheck_id() {
		return check_id;
	}
	public void setCheck_id(String check_id) {
		this.check_id = check_id;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getSub_yesno() {
		return sub_yesno;
	}
	public void setSub_yesno(String sub_yesno) {
		this.sub_yesno = sub_yesno;
	}
	public String getSub_reason() {
		return sub_reason;
	}
	public void setSub_reason(String sub_reason) {
		this.sub_reason = sub_reason;
	}
	public String getSub_image() {
		return sub_image;
	}
	public void setSub_image(String sub_image) {
		this.sub_image = sub_image;
	}
	public String getSub_user() {
		return sub_user;
	}
	public void setSub_user(String sub_user) {
		this.sub_user = sub_user;
	}
	public String getMain_yesno() {
		return main_yesno;
	}
	public void setMain_yesno(String main_yesno) {
		this.main_yesno = main_yesno;
	}
	public String getMain_reason() {
		return main_reason;
	}
	public void setMain_reason(String main_reason) {
		this.main_reason = main_reason;
	}
	public String getMain_image() {
		return main_image;
	}
	public void setMain_image(String main_image) {
		this.main_image = main_image;
	}
	public String getMain_user() {
		return main_user;
	}
	public void setMain_user(String main_user) {
		this.main_user = main_user;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getWork_detail_code() {
		return work_detail_code;
	}
	public void setWork_detail_code(String work_detail_code) {
		this.work_detail_code = work_detail_code;
	}
	public String getWork_level_code() {
		return work_level_code;
	}
	public void setWork_level_code(String work_level_code) {
		this.work_level_code = work_level_code;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getCont_id() {
		return cont_id;
	}
	public void setCont_id(String cont_id) {
		this.cont_id = cont_id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getChasu() {
		return chasu;
	}
	public void setChasu(String chasu) {
		this.chasu = chasu;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getSpot_id() {
		return spot_id;
	}
	public void setSpot_id(String spot_id) {
		this.spot_id = spot_id;
	}
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String c_id) {
		this.c_id = c_id;
	}
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getWork_detail() {
		return work_detail;
	}
	public void setWork_detail(String work_detail) {
		this.work_detail = work_detail;
	}
	public String getWork_level() {
		return work_level;
	}
	public void setWork_level(String work_level) {
		this.work_level = work_level;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getWork_date() {
		return work_date;
	}
	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}
	public String getWork_place() {
		return work_place;
	}
	public void setWork_place(String work_place) {
		this.work_place = work_place;
	}
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getRisk_content() {
		return risk_content;
	}
	public void setRisk_content(String risk_content) {
		this.risk_content = risk_content;
	}
	public String getD_worker() {
		return d_worker;
	}
	public void setD_worker(String d_worker) {
		this.d_worker = d_worker;
	}
	public String getD_kind() {
		return d_kind;
	}
	public void setD_kind(String d_kind) {
		this.d_kind = d_kind;
	}
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public String getKang() {
		return kang;
	}
	public void setKang(String kang) {
		this.kang = kang;
	}
	public String getManage() {
		return manage;
	}
	public void setManage(String manage) {
		this.manage = manage;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	
	
}
