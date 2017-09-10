package kr.gsil.dpkepco.model;

import android.graphics.Bitmap;

public class MobileEquipVO {
	private String id;
	private String gubun;
	private String title;
	private String content;
	private String check_result;
	private String check_comment;

	private String cont_name;
	
	private String category;
	private String ename;
	private String eform; 
	private String estand; 
	private String code1; 
	private String code2; 
	private String code3; 
	private String code4;
	private Bitmap image;
	
	private String realsize;
	private String rentcontname;
	private String signgubun;
	private String insuranceStart;
	private String insurenceEnd;
	private String deviceNum;
	private String deviceImg;
	
	private String check_start;
	private String check_end;
	
	
	private String driverName;
	private String driverLicense;
	private String driverPhone;
	private String driverRank;
	
	private String cont_id;
	private String site_id;
	
	private String u_id;
	private String c_id;
	private String category_id;
	
	private String check_gubun;
	private String check_id;
	private String checkdate;
	private String check_img;
	
	private String no;
	private String contract_form;
	private String status;
	
	private String smart;
	
	private String incomedate;
	private String outcomedate;
	public int check;
	private String confirm_id;
	private String tag_id;

	private String reg_user_id;
	private String reg_user_name;
	private String confirm_name;
	private String check_user_id;
	private String confirm_user_id;
	private String option_gubun;
	private String dstatus;
	private String workdate;
	private String reason;
	
	private String device_name;
	private String e_id;
	
	private String idlist;
	private String namelist;
	private String content_img;
	private String daily_id;
	
	private String mac;
	private String sensortype;
	private String data;
	private String time;
	private String tunnel;
	private String section;
	private String writetime;
	
	private String temperature;
	private String humidity;
	private String oxygen;
	private String co;
	private String co2;
	private String o2;
	
	
	private String ntemperature;
	private String nhumidity;
	private String noxygen;
	private String nco;
	private String nco2;
	
	
	
	
	public String getO2() {
		return o2;
	}
	public void setO2(String o2) {
		this.o2 = o2;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getOxygen() {
		return oxygen;
	}
	public void setOxygen(String oxygen) {
		this.oxygen = oxygen;
	}
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public String getCo2() {
		return co2;
	}
	public void setCo2(String co2) {
		this.co2 = co2;
	}
	public String getNtemperature() {
		return ntemperature;
	}
	public void setNtemperature(String ntemperature) {
		this.ntemperature = ntemperature;
	}
	public String getNhumidity() {
		return nhumidity;
	}
	public void setNhumidity(String nhumidity) {
		this.nhumidity = nhumidity;
	}
	public String getNoxygen() {
		return noxygen;
	}
	public void setNoxygen(String noxygen) {
		this.noxygen = noxygen;
	}
	public String getNco() {
		return nco;
	}
	public void setNco(String nco) {
		this.nco = nco;
	}
	public String getNco2() {
		return nco2;
	}
	public void setNco2(String nco2) {
		this.nco2 = nco2;
	}
	public String getTunnel() {
		return tunnel;
	}
	public void setTunnel(String tunnel) {
		this.tunnel = tunnel;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getWritetime() {
		return writetime;
	}
	public void setWritetime(String writetime) {
		this.writetime = writetime;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSensortype() {
		return sensortype;
	}
	public void setSensortype(String sensortype) {
		this.sensortype = sensortype;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDaily_id() {
		return daily_id;
	}
	public void setDaily_id(String daily_id) {
		this.daily_id = daily_id;
	}
	public String getContent_img() {
		return content_img;
	}
	public void setContent_img(String content_img) {
		this.content_img = content_img;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getIdlist() {
		if( idlist == null ) idlist =  "";
		return idlist;
	}
	public void setIdlist(String idlist) {
		this.idlist = idlist;
	}
	public String getNamelist() {
		if( namelist == null ) namelist = "";
		return namelist;
	}
	public void setNamelist(String namelist) {
		this.namelist = namelist;
	}
	public String getE_id() {
		return e_id;
	}
	public void setE_id(String e_id) {
		this.e_id = e_id;
	}
	public String getCont_name() {
		return cont_name;
	}
	public void setCont_name(String cont_name) {
		this.cont_name = cont_name;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDstatus() {
		return dstatus;
	}
	public void setDstatus(String dstatus) {
		this.dstatus = dstatus;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	public String getOption_gubun() {
		return option_gubun;
	}
	public void setOption_gubun(String option_gubun) {
		this.option_gubun = option_gubun;
	}
	public String getCheck_user_id() {
		return check_user_id;
	}
	public void setCheck_user_id(String check_user_id) {
		this.check_user_id = check_user_id;
	}
	public String getConfirm_user_id() {
		return confirm_user_id;
	}
	public void setConfirm_user_id(String confirm_user_id) {
		this.confirm_user_id = confirm_user_id;
	}
	public String getReg_user_id() {
		return reg_user_id;
	}
	public void setReg_user_id(String reg_user_id) {
		this.reg_user_id = reg_user_id;
	}
	public String getReg_user_name() {
		return reg_user_name;
	}
	public void setReg_user_name(String reg_user_name) {
		this.reg_user_name = reg_user_name;
	}
	public String getConfirm_name() {
		return confirm_name;
	}
	public void setConfirm_name(String confirm_name) {
		this.confirm_name = confirm_name;
	}
	public String getTag_id() {
		return tag_id;
	}
	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
	public String getConfirm_id() {
		return confirm_id;
	}
	public void setConfirm_id(String confirm_id) {
		this.confirm_id = confirm_id;
	}
	public int getCheck() {
		return check;
	}
	public void setCheck(int check) {
		this.check = check;
	}
	public String getIncomedate() {
		return incomedate;
	}
	public void setIncomedate(String incomedate) {
		this.incomedate = incomedate;
	}
	public String getOutcomedate() {
		return outcomedate;
	}
	public void setOutcomedate(String outcomedate) {
		this.outcomedate = outcomedate;
	}
	public String getSmart() {
		return smart;
	}
	public void setSmart(String smart) {
		this.smart = smart;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContract_form() {
		return contract_form;
	}
	public void setContract_form(String contract_form) {
		this.contract_form = contract_form;
	}
	public String getCheck_gubun() {
		return check_gubun;
	}
	public void setCheck_gubun(String check_gubun) {
		this.check_gubun = check_gubun;
	}
	public String getCheck_id() {
		return check_id;
	}
	public void setCheck_id(String check_id) {
		this.check_id = check_id;
	}
	public String getCheckdate() {
		return checkdate;
	}
	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getCheck_img() {
		return check_img;
	}
	public void setCheck_img(String check_img) {
		this.check_img = check_img;
	}
	public String getCont_id() {
		return cont_id;
	}
	public void setCont_id(String cont_id) {
		this.cont_id = cont_id;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getCheck_start() {
		return check_start;
	}
	public void setCheck_start(String check_start) {
		this.check_start = check_start;
	}
	public String getCheck_end() {
		return check_end;
	}
	public void setCheck_end(String check_end) {
		this.check_end = check_end;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverLicense() {
		return driverLicense;
	}
	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public String getDriverRank() {
		return driverRank;
	}
	public void setDriverRank(String driverRank) {
		this.driverRank = driverRank;
	}
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getRealsize() {
		return realsize;
	}
	public void setRealsize(String realsize) {
		this.realsize = realsize;
	}
	public String getRentcontname() {
		return rentcontname;
	}
	public void setRentcontname(String rentcontname) {
		this.rentcontname = rentcontname;
	}
	public String getSigngubun() {
		return signgubun;
	}
	public void setSigngubun(String signgubun) {
		this.signgubun = signgubun;
	}
	public String getInsuranceStart() {
		return insuranceStart;
	}
	public void setInsuranceStart(String insuranceStart) {
		this.insuranceStart = insuranceStart;
	}
	public String getInsurenceEnd() {
		return insurenceEnd;
	}
	public void setInsurenceEnd(String insurenceEnd) {
		this.insurenceEnd = insurenceEnd;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getDeviceImg() {
		return deviceImg;
	}
	public void setDeviceImg(String deviceImg) {
		this.deviceImg = deviceImg;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	
	
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String c_id) {
		this.c_id = c_id;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEform() {
		return eform;
	}
	public void setEform(String eform) {
		this.eform = eform;
	}
	public String getEstand() {
		return estand;
	}
	public void setEstand(String estand) {
		this.estand = estand;
	}
	public String getCode1() {
		return code1;
	}
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public String getCode3() {
		return code3;
	}
	public void setCode3(String code3) {
		this.code3 = code3;
	}
	public String getCode4() {
		return code4;
	}
	public void setCode4(String code4) {
		this.code4 = code4;
	}
	public String getCheck_result() {
		return check_result;
	}
	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}
	public String getCheck_comment() {
		return check_comment;
	}
	public void setCheck_comment(String check_comment) {
		this.check_comment = check_comment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
