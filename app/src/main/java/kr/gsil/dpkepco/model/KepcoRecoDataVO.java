package kr.gsil.dpkepco.model;

/**
 * Created by gsil on 2017. 9. 26..
 */

public class KepcoRecoDataVO {
    int countTotal = 0;
    int countWorker = 0;
    int countManager = 0;
    int countVip = 0;

    public KepcoRecoDataVO(){
        countTotal = 0;
        countWorker = 0;
        countManager = 0;
        countVip = 0;
    }

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public void addCountTotal(){
        this.countTotal++;
    }

    public int getCountWorker() {
        return countWorker;
    }

    public void setCountWorker(int countWorker) {
        this.countWorker = countWorker;
    }

    public void addCountWorker(){
        this.countWorker++;
    }

    public int getCountManager() {
        return countManager;
    }

    public void setCountManager(int countManager) {
        this.countManager = countManager;
    }

    public void addCountManager(){
        this.countManager++;
    }

    public int getCountVip() {
        return countVip;
    }

    public void setCountVip(int countVip) {
        this.countVip = countVip;
    }

    public void addCountVip(){
        this.countVip++;
    }

}
