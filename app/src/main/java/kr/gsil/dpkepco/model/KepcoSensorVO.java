package kr.gsil.dpkepco.model;


import java.io.Serializable;

/**
 * Created by gsil on 2017. 9. 11..
 */

public class KepcoSensorVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String site_id;
    private int place_id;
    private double o2;//산소
    private double co;//일산화탄소
    private double h2s;//황화수소
    private double gas;//가연성가스
    private String writetime;
    public String getSite_id() {
        return site_id;
    }
    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
    public int getPlace_id() {
        return place_id;
    }
    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }
    public double getO2() {
        return o2;
    }
    public void setO2(double o2) {
        this.o2 = o2;
    }
    public double getCo() {
        return co;
    }
    public void setCo(double co) {
        this.co = co;
    }
    public double getH2s() {
        return h2s;
    }
    public void setH2s(double h2s) {
        this.h2s = h2s;
    }
    public double getGas() {
        return gas;
    }
    public void setGas(double gas) {
        this.gas = gas;
    }
    public String getWritetime() {
        return writetime;
    }
    public void setWritetime(String writetime) {
        this.writetime = writetime;
    }


}
