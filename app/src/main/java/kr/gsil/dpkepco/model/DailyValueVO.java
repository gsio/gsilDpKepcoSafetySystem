package kr.gsil.dpkepco.model;

import java.io.Serializable;

/**
 * Created by gsil on 2017. 9. 13..
 */

public class DailyValueVO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private int site_id;
    private int type;
    private String input_date;
    private double value;
    private String writetime;
    private String updatetime;
    private String writer_user_id;
    private String writer_user_name;
    private int isMobile;

    private double sum_value;//누적 굴진량
    private double avg_value;//
    private double month_avg_value;//
    private int day_count;//
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
    public String getInput_date() {
        return input_date;
    }
    public void setInput_date(String input_date) {
        this.input_date = input_date;
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
    public String getWriter_user_name() {
        return writer_user_name;
    }
    public void setWriter_user_name(String writer_user_name) {
        this.writer_user_name = writer_user_name;
    }
    public int getIsMobile() {
        return isMobile;
    }
    public void setIsMobile(int isMobile) {
        this.isMobile = isMobile;
    }
    public double getSum_value() {
        return sum_value;
    }
    public void setSum_value(double sum_value) {
        this.sum_value = sum_value;
    }
    public double getAvg_value() {
        return avg_value;
    }
    public void setAvg_value(double avg_value) {
        this.avg_value = avg_value;
    }
    public int getDay_count() {
        return day_count;
    }
    public void setDay_count(int day_count) {
        this.day_count = day_count;
    }


    public double getMonth_avg_value() {
        return month_avg_value;
    }

    public void setMonth_avg_value(double month_avg_value) {
        this.month_avg_value = month_avg_value;
    }

}