package kr.gsil.dpkepco.model;

import java.util.ArrayList;

public class MobileMetaVO {
	private String id;
	private String class_code;
	private String code;
	private String codename;
	private String code_level;
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String num_info;
	private String damage;
	private String bin;
	private String kang;
	private String manage;
	private String work_detail;
	private String work_detail_code;
	private String work_level;
	private String work_level_code;
	private String startdate;
	private String enddate;
	
	private String manager;
	private String device;
	private String work_date;
	private String work_place;
	private String worker;
	
	private String gubun;
	
	private int check = 0;
	private int count = 0;
	
	
	private ArrayList<MobileMetaVO> subList = new ArrayList<MobileMetaVO>();
	
	public MobileMetaVO () {
		
	}
	
	public MobileMetaVO ( String id, String code , String level1 , String codename, ArrayList<MobileMetaVO> subList ) {
		this.id = id;
		this.code = code;
		this.level1 = level1;
		this.codename = codename;
		this.subList = subList;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
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

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public ArrayList<MobileMetaVO> getSubList() {
		return subList;
	}
	public void setSubList(MobileMetaVO meta) {
		this.subList.add(meta);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClass_code() {
		return class_code;
	}
	public void setClass_code(String class_code) {
		this.class_code = class_code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodename() {
		return codename;
	}
	public void setCodename(String codename) {
		this.codename = codename;
	}
	public String getCode_level() {
		return code_level;
	}
	public void setCode_level(String code_level) {
		this.code_level = code_level;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getLevel4() {
		return level4;
	}
	public void setLevel4(String level4) {
		this.level4 = level4;
	}
	public String getNum_info() {
		return num_info;
	}
	public void setNum_info(String num_info) {
		this.num_info = num_info;
	}
	public String getDamage() {
		return damage;
	}
	public void setDamage(String damage) {
		this.damage = damage;
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
	
	
	
}
