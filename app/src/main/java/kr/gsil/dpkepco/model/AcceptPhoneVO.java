package kr.gsil.dpkepco.model;

import java.util.ArrayList;

/**
 * Created by gsil on 2017. 10. 10..
 */

public class AcceptPhoneVO {

    private boolean needDiff = false;
    ArrayList<String> list = new ArrayList<String>();

    public boolean isNeedDiff() {
        return needDiff;
    }

    public void setNeedDiff(boolean needDiff) {
        this.needDiff = needDiff;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public boolean isContainPhoneNumber(String phoneNumber){
        return list.contains(phoneNumber);
    }
}
