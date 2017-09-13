package kr.gsil.dpkepco.model;

import java.io.Serializable;

/**
 * Created by gsil on 2017. 9. 11..
 */

public class KepcoMonitorVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private double total_const;//총 연장
    private double total_meter;//누계굴진
    private double depth;//심도
    private double remain_meter;//잔량
    private double today_meter;//오늘(일) 굴진량
    private double this_mon_meter;//월 굴진량
    private double avg_meter;//평균 굴진량
    private double month_avg_meter;//월평균 굴진량

    //select * From kepco_section;
    //굴진거리에 따른 문구
    private String text1;//지층구분
    private String text2;//RMR암반등급
    private String text3;//그라우팅타입
    private String text4;//Target압력

    //select * From timely_value;
    //수시 입력 데이터
    private double value1;//염분농도
    private double value2;//막장압력
    private double value3;//오탁수 처리량
    private double value4;//굴진 상태
    private double value5;//총 인력
    private double value6;//수위
    public double getTotal_meter() {
        return total_meter;
    }
    public void setTotal_meter(double total_meter) {
        this.total_meter = total_meter;
    }
    public double getDepth() {
        return depth;
    }
    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getToday_meter() {
        return today_meter;
    }
    public void setToday_meter(double today_meter) {
        this.today_meter = today_meter;
    }
    public double getThis_mon_meter() {
        return this_mon_meter;
    }
    public void setThis_mon_meter(double this_mon_meter) {
        this.this_mon_meter = this_mon_meter;
    }
    public double getAvg_meter() {
        return avg_meter;
    }
    public void setAvg_meter(double avg_meter) {
        this.avg_meter = avg_meter;
    }
    public double getMonth_avg_meter() {
        return month_avg_meter;
    }
    public void setMonth_avg_meter(double month_avg_meter) {
        this.month_avg_meter = month_avg_meter;
    }
    public String getText1() {
        return text1;
    }
    public void setText1(String text1) {
        this.text1 = text1;
    }
    public String getText2() {
        return text2;
    }
    public void setText2(String text2) {
        this.text2 = text2;
    }
    public String getText3() {
        return text3;
    }
    public void setText3(String text3) {
        this.text3 = text3;
    }
    public String getText4() {
        return text4;
    }
    public void setText4(String text4) {
        this.text4 = text4;
    }
    public double getValue1() {
        return value1;
    }
    public void setValue1(double value1) {
        this.value1 = value1;
    }
    public double getValue2() {
        return value2;
    }
    public void setValue2(double value2) {
        this.value2 = value2;
    }
    public double getValue3() {
        return value3;
    }
    public void setValue3(double value3) {
        this.value3 = value3;
    }
    public double getValue4() {
        return value4;
    }
    public void setValue4(double value4) {
        this.value4 = value4;
    }
    public double getValue5() {
        return value5;
    }
    public void setValue5(double value5) {
        this.value5 = value5;
    }
    public double getValue6() {
        return value6;
    }
    public void setValue6(double value6) {
        this.value6 = value6;
    }
    public double getTotal_const() {
        return total_const;
    }
    public void setTotal_const(double total_const) {
        this.total_const = total_const;
    }
    public double getRemain_meter() {
        return remain_meter;
    }
    public void setRemain_meter(double remain_meter) {
        this.remain_meter = remain_meter;
    }

}