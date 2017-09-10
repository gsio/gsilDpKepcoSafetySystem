package kr.gsil.dpkepco.model;

public class MobileNoticeVO {
	String id;

	String title;
	String date;
	String hit;
	String contents;
	
	String site_id;
	
	String filepath1;
	String filename1;
	String filepath2;
	String filename2;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		
		this.hit = hit;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getFilepath1() {
		return filepath1;
	}
	public void setFilepath1(String filepath1) {
		this.filepath1 = filepath1;
	}
	public String getFilename1() {
		return filename1;
	}
	public void setFilename1(String filename1) {
		this.filename1 = filename1;
	}
	public String getFilepath2() {
		return filepath2;
	}
	public void setFilepath2(String filepath2) {
		this.filepath2 = filepath2;
	}
	public String getFilename2() {
		return filename2;
	}
	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}
}
