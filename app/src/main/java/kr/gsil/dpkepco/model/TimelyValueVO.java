package kr.gsil.dpkepco.model;


import java.io.Serializable;

/**
 * Created by gsil on 2017. 9. 13..
 */

public class TimelyValueVO implements Serializable{

    /**
     type
     1: 염분농도
     2: 막장압력
     3: 오탁수 처리량
     99: 굴진현황
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private int site_id;
    private int type;
    private String write_date;
    private double value;
    private String writetime;
    private String writetime_hms;
    private String updatetime;
    private String writer_user_id;
    private String writer_user_name;
    private int isMobile;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSite_id() {
        return site_id;
    }
    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getWrite_date() {
        return write_date;
    }
    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public String getWritetime() {
        return writetime;
    }
    public void setWritetime(String writetime) {
        this.writetime = writetime;
    }
    public String getUpdatetime() {
        return updatetime;
    }
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public String getWriter_user_id() {
        return writer_user_id;
    }
    public void setWriter_user_id(String writer_user_id) {
        this.writer_user_id = writer_user_id;
    }
    public int getIsMobile() {
        return isMobile;
    }
    public void setIsMobile(int isMobile) {
        this.isMobile = isMobile;
    }
    public String getWriter_user_name() {
        return writer_user_name;
    }
    public void setWriter_user_name(String writer_user_name) {
        this.writer_user_name = writer_user_name;
    }
    public String getWritetime_hms() {
        return writetime_hms;
    }
    public void setWritetime_hms(String writetime_hms) {
        this.writetime_hms = writetime_hms;
    }

}
