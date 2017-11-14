package kr.gsil.dpkepco;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import kr.gsil.dpkepco.model.*;

import kr.gsil.dpkepco.model.MobileEquipVO;
import kr.gsil.dpkepco.model.weather.WeatherInfoVO;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class UserSystemApplication extends Application {
    /**
     * SharedPreferences DB ?��?��
     */
    public SharedPreferences pref = null;
    public SharedPreferences.Editor editer = null;
    //로그?�� ?���?
    private boolean isLogin = false;
    private String pid = "";
    private String type = "";
    private String userid = "";
    private String cont_id = "";
    private String site_id = "";
    private String tag_id = "";
    private String id = "";
    private String name = "";
    private String cname = "";
    private String rtype = "";
    private String year = "";
    private String month = "";
    private String chasu = "";
    private String status = "";
    private String spot_id = "";
    private String updateGubun = "0";
    private String r_id = "";
    private String site_name = "";
    private boolean isMaster = false;
    private boolean isGps = false;
    private boolean isReflash = false;

    private Runnable mRunnable = null;

    private String idlist = "";
    private String namelist = "";

    /*************
     * ScheOption
     * ***/
    private String daily;
    private String spot;
    private String risk;
    private String equip;
    private String tunnal;
    private String cycle;
    private String measure;
    private String etc;
    /////////////////////////

    private MobileMetaVO oneMeta = null;
    private MobileMetaVO twoMeta = null;
    private MobileRiskVO tempRisk = null;
    private MobileRiskVO selectedRiskVo = null;
    private MobileSpotVO tempSpot = null;
    private MobileMeasureVO tempMea = null;
    private MobileWorkerVO mv = null;
    private MobileUserVO mu = null;
    private MobileEquipVO eq = null;
    private MobileTunelVO t = null;
    private MobileEquipVO eItem = null;
    private MobileUserVO gsil = null;

    private MobileInfoVO mi = null;
    private MobileNoticeVO mn = null;

    public ArrayList<Activity> dList = new ArrayList<Activity>();
    ArrayList<MobileVO> qList = new ArrayList<MobileVO>();
    ArrayList<MobileMetaVO> saveMetaList = new ArrayList<MobileMetaVO>();
    ArrayList<MobileMetaVO> teamMetaList = new ArrayList<MobileMetaVO>();
    ArrayList<MobileRiskVO> saveRiskCheckList = new ArrayList<MobileRiskVO>();
    ArrayList<MobileEquipVO> eList = new ArrayList<MobileEquipVO>();
    ArrayList<MobileWorkerVO> locationWorkerList = new ArrayList<MobileWorkerVO>();
    ArrayList<MobileUserVO> beaconlist = new ArrayList<MobileUserVO>();

    //

    ArrayList<MobileEquipVO> dailyEquipList = new ArrayList<MobileEquipVO>();
    ArrayList<MobileWorkerVO> dailyWorkerList = new ArrayList<MobileWorkerVO>();
    ArrayList<MobileUserVO> dailyUserList = new ArrayList<MobileUserVO>();
    ArrayList<MobileWorkerVO> searchWorkerList = new ArrayList<MobileWorkerVO>();
    ArrayList<MobileUserVO> searchUserList = new ArrayList<MobileUserVO>();

    ArrayList<MobileVO> mainContent = new ArrayList<MobileVO>();

    private Activity pact = null;

    private Activity spotAct =  null;
    private Activity spotCheckAct = null;

    String filename = "";

    MobileVO item = new MobileVO();

    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    private Bitmap oneImage = null;

    String selectItems = "";

    KepcoMonitorVO kepcoMonitorVO = null;
    KepcoSensorVO kepcoSensorVO = null;

    KepcoRecoDataVO kepcoRecoDataVO = null;
    public void setKepcoData(KepcoMonitorVO kepcoMonitorVO, KepcoSensorVO kepcoSensorVO){
        this.kepcoMonitorVO = kepcoMonitorVO;
        this.kepcoSensorVO = kepcoSensorVO;
    }


    public KepcoMonitorVO getKepcoMonitor(){
        return kepcoMonitorVO;
    }

    public KepcoSensorVO getKepcoSensor(){
        return kepcoSensorVO;
    }

    public void setKepcoRecoData(KepcoRecoDataVO kepcoRecoDataVO){
        this.kepcoRecoDataVO = null;
        this.kepcoRecoDataVO = kepcoRecoDataVO;
    }

    public KepcoRecoDataVO getKepcoRecoData(){
        return this.kepcoRecoDataVO;
    }

    public ArrayList<MobileUserVO> getBeaconlist() {
        return beaconlist;
    }

    public void setBeaconlist(ArrayList<MobileUserVO> beaconlist) {
        this.beaconlist = beaconlist;
    }

    public Runnable getmRunnable() {
        return mRunnable;
    }

    public void setmRunnable(Runnable mRunnable) {
        this.mRunnable = mRunnable;
    }


    public boolean isReflash() {
        return isReflash;
    }

    public void setReflash(boolean isReflash) {
        this.isReflash = isReflash;
    }

    public ArrayList<MobileVO> getMainContent() {
        return mainContent;
    }

    public void setMainContent(ArrayList<MobileVO> mainContent) {
        this.mainContent = mainContent;
    }

    public MobileUserVO getGsil() {
        return gsil;
    }

    public void setGsil(MobileUserVO gsil) {
        this.gsil = gsil;
    }

    public String getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(String selectItems) {
        this.selectItems = selectItems;
    }

    //
    public String getIdlist() {
        return idlist;
    }

    public MobileUserVO getMu() {
        return mu;
    }

    public void setMu(MobileUserVO mu) {
        this.mu = mu;
    }

    public void setIdlist(String idlist) {
        this.idlist = idlist;
    }

    public String getNamelist() {
        return namelist;
    }

    public void setNamelist(String namelist) {
        this.namelist = namelist;
    }

    public Activity getSpotAct() {
        return spotAct;
    }

    public void setSpotAct(Activity spotAct) {
        this.spotAct = spotAct;
    }

    public Activity getSpotCheckAct() {
        return spotCheckAct;
    }

    public void setSpotCheckAct(Activity spotCheckAct) {
        this.spotCheckAct = spotCheckAct;
    }

    public MobileTunelVO getT() {
        return t;
    }


    public void initElist() {
        this.eList = null;
        this.eList = new ArrayList<MobileEquipVO>();
    }

    public ArrayList<MobileEquipVO> geteList() {
        return eList;
    }

    public void seteList(MobileEquipVO e) {
        this.eList.add(e);
    }


    public MobileEquipVO getEq() {
        return eq;
    }

    public void setEq(MobileEquipVO eq) {
        this.eq = eq;
    }


    public void setT(MobileTunelVO t) {
        this.t = t;
    }

    public MobileRiskVO getSelectedRiskVo() {
        return selectedRiskVo;
    }

    public void setSelectedRiskVo(MobileRiskVO selectedRiskVo) {
        this.selectedRiskVo = selectedRiskVo;
    }

    public MobileMeasureVO getTempMea() {
        return tempMea;
    }

    public void setTempMea(MobileMeasureVO tempMea) {
        this.tempMea = tempMea;
    }

    public MobileNoticeVO getMn() {
        return mn;
    }

    public void setMn(MobileNoticeVO mn) {
        this.mn = mn;
    }

    public MobileInfoVO getMi() {
        return mi;
    }

    public void setMi(MobileInfoVO mi) {
        this.mi = mi;
    }

    public Bitmap getOneImage() {
        return oneImage;
    }

    public void setOneImage(Bitmap oneImage) {
        this.oneImage = oneImage;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MobileSpotVO getTempSpot() {
        return tempSpot;
    }

    public void setTempSpot(MobileSpotVO tempSpot) {
        this.tempSpot = tempSpot;
    }

    public MobileRiskVO getTempRisk() {
        return tempRisk;
    }

    public void setTempRisk(MobileRiskVO tempRisk) {
        this.tempRisk = tempRisk;
    }

    public MobileMetaVO getOneMeta() {
        return oneMeta;
    }

    public void setOneMeta(MobileMetaVO oneMeta) {
        this.oneMeta = oneMeta;
    }

    public MobileMetaVO getTwoMeta() {
        return twoMeta;
    }

    public void setTwoMeta(MobileMetaVO twoMeta) {
        this.twoMeta = twoMeta;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getUpdateGubun() {
        return updateGubun;
    }

    public void setUpdateGubun(String updateGubun) {
        this.updateGubun = updateGubun;
    }

    public ArrayList<MobileMetaVO> getSaveMetaList() {
        return saveMetaList;
    }

    public void initSaveMetaList() {
        this.saveMetaList = null;
        this.saveMetaList = new ArrayList<MobileMetaVO>();
    }

    public void initTempMetaList() {
        this.teamMetaList = null;
        this.teamMetaList = new ArrayList<MobileMetaVO>();
    }
    public ArrayList<MobileMetaVO> getTeamMetaList() {
        return teamMetaList;
    }

    public void setTeamMetaList(MobileMetaVO meta) {
        if( this.teamMetaList != null ) {
            this.teamMetaList.add(meta);
        } else {
            this.teamMetaList = new ArrayList<MobileMetaVO>();
            this.teamMetaList.add(meta);
        }
    }

    public void setSaveMetaList(MobileMetaVO meta) {
        if( this.saveMetaList != null ) {
            this.saveMetaList.add(meta);
        } else {
            this.saveMetaList = new ArrayList<MobileMetaVO>();
            this.saveMetaList.add(meta);
        }
    }

    public void initSearchWorkerList() {
        this.searchWorkerList = null;
        this.searchWorkerList = new ArrayList<MobileWorkerVO>();
    }

    public ArrayList<MobileWorkerVO> getSearchWorkerList() {
        return searchWorkerList;
    }

    public void setSearchWorkerList(ArrayList<MobileWorkerVO> searchWorkerList) {
        this.searchWorkerList = searchWorkerList;
    }


    public void initSearchUserList() {
        this.searchUserList = null;
        this.searchUserList = new ArrayList<MobileUserVO>();
    }

    public ArrayList<MobileUserVO> getSearchUserList() {
        return searchUserList;
    }

    public void setSearchUserList(ArrayList<MobileUserVO> searchUserList) {
        this.searchUserList = searchUserList;
    }

    public void initRiskCheckList() {
        this.saveRiskCheckList = null;
        this.saveRiskCheckList = new ArrayList<MobileRiskVO>();
    }

    public ArrayList<MobileRiskVO> getSaveRiskCheckList() {
        return saveRiskCheckList;
    }

    public void setSaveRiskCheckList(MobileRiskVO risk) {
        if( this.saveRiskCheckList != null ) {
            this.saveRiskCheckList.add(risk);
        } else {
            this.saveRiskCheckList = new ArrayList<MobileRiskVO>();
            this.saveRiskCheckList.add(risk);
        }
    }

    public void initLocationWorkerList() {
        this.locationWorkerList = null;
        this.locationWorkerList = new ArrayList<MobileWorkerVO>();
    }

    public ArrayList<MobileWorkerVO> getLocationWorkerList() {
        return locationWorkerList;
    }

    public void setLocationWorkerList(MobileWorkerVO worker) {
        if( this.locationWorkerList != null ) {
            this.locationWorkerList.add(worker);
        } else {
            this.locationWorkerList = new ArrayList<MobileWorkerVO>();
            this.locationWorkerList.add(worker);
        }
    }


    public void initDailyUserList() {
        this.dailyUserList = null;
        this.dailyUserList = new ArrayList<MobileUserVO>();
    }

    public void initDailyWorkerList() {
        this.dailyWorkerList = null;
        this.dailyWorkerList = new ArrayList<MobileWorkerVO>();
    }

    public void initDailyEquipList() {
        this.dailyEquipList = null;
        this.dailyEquipList = new ArrayList<MobileEquipVO>();
    }

    public void settingOptionWokrer( String id, String gubun ) {
        if( this.dailyWorkerList != null && this.dailyWorkerList.size() > 0 ) {
            for( int i = 0; i < this.dailyWorkerList.size(); i++ ) {
                MobileWorkerVO m = ( MobileWorkerVO )this.dailyWorkerList.get(i);
                if( m.getId().equals(id) ) {
                    m.setOption(gubun);
                    return;
                }
            }
        }
    }

    public ArrayList<MobileWorkerVO> getDailyWorkerList() {
        return dailyWorkerList;
    }

    public void setDailyWorkerList(MobileWorkerVO worker) {
        if( this.dailyWorkerList != null ) {
            this.dailyWorkerList.add(worker);
        } else {
            this.dailyWorkerList = new ArrayList<MobileWorkerVO>();
            this.dailyWorkerList.add(worker);
        }

    }

    public ArrayList<MobileEquipVO> getDailyEquipList() {
        return dailyEquipList;
    }

    public void setDailyEquipList(MobileEquipVO e) {
        if( this.dailyEquipList != null ) {
            this.dailyEquipList.add(e);
        } else {
            this.dailyEquipList = new ArrayList<MobileEquipVO>();
            this.dailyEquipList.add(e);
        }

    }

    public ArrayList<MobileUserVO> getDailyUserList() {
        return dailyUserList;
    }

    public void setDailyUserList(MobileUserVO user) {
        if( this.dailyUserList != null ) {
            this.dailyUserList.add(user);
        } else {
            this.dailyUserList = new ArrayList<MobileUserVO>();
            this.dailyUserList.add(user);
        }
    }

    public boolean checkListItem( MobileMetaVO meta ) {
        if( this.saveMetaList != null && this.saveMetaList.size() > 0 ) {
            for( int i = 0; i < this.saveMetaList.size(); i++ ) {
                MobileMetaVO m = this.saveMetaList.get(i);
                if( m.getCode().equals(meta.getCode()) ) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(String spot_id) {
        this.spot_id = spot_id;
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

    public MobileWorkerVO getMv() {
        return mv;
    }

    public void setMv(MobileWorkerVO mv) {
        this.mv = mv;
    }

    public Activity getPact() {
        return pact;
    }

    public void setPact(Activity pact) {
        this.pact = pact;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ArrayList<Bitmap> getImageList() {
        return imageList;
    }

    public void setImage(Bitmap image) {
        this.imageList.add(image);
    }

    public boolean deleteImge( Bitmap value ) {
        boolean result = false;
        if( imageList != null && imageList.size() > 0 ) {
            for( int i = 0 ; i < imageList.size(); i++ ) {
                if( value.equals((Bitmap)imageList.get(i)) ) {
                    imageList.remove(i);
                    return true;
                }
            }
        }
        return result;
    }


    public void initImageList() {
        this.imageList = null;
        this.imageList = new ArrayList<Bitmap>();
    }




    public MobileEquipVO geteItem() {
        return eItem;
    }

    public void seteItem(MobileEquipVO eItem) {
        this.eItem = eItem;
    }

    public MobileVO getItem() {
        return item;
    }

    public void setItem(MobileVO item) {
        this.item = item;
    }

    public void initQlist() {
        qList = null;
        qList = new ArrayList<MobileVO>();
    }

    public ArrayList<MobileVO> getqList() {
        return qList;
    }

    public void setqList(MobileVO q) {
        this.qList.add(q);
    }

    public void setDlist(Activity act) {
        this.dList.add(act);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getBaseContext().getSharedPreferences("gsildangjinsystem", Activity.MODE_PRIVATE);
        editer = pref.edit();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(10 * 1024 * 1024) // 2 Mb
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .imageDownloader(new BaseImageDownloader(getApplicationContext()))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//		.enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
    /**
     * 로그인 유무
     * @return
     */
    public boolean isLogin() {
        if(isLogin==false){
            return isAutoLogin();
        }
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * 자동 로그인
     * @return
     */
    public boolean isAutoLogin(){
        return pref.getBoolean("autologin", false);
    }

    public void setAutoLogin(boolean value){
        editer.putBoolean("autologin", value);
        editer.commit();
    }



    public boolean isGps() {
        return pref.getBoolean("isGps", false);
    }

    public void setGps(boolean isGps) {
        editer.putBoolean("isGps", isGps);
        editer.commit();
    }

    public String getPid() {
        return pref.getString("pid", pid);
    }

    public void setPid(String pid) {
        editer.putString("pid", pid);
        editer.commit();
    }


    public String getUserid() {
        return pref.getString("userid", userid);
    }

    public void setUserid(String userid) {
        editer.putString("userid", userid);
        editer.commit();
    }

    public String getCont_id() {
        return pref.getString("cont_id", cont_id);
    }

    public void setCont_id(String cont_id) {
        editer.putString("cont_id", cont_id);
        editer.commit();
    }


    public String getSite_id() {
        return pref.getString("site_id", site_id);
    }

    public void setSite_id(String site_id) {
        editer.putString("site_id", site_id);
        editer.commit();
    }

    public String getRtype() {
        return pref.getString("rtype", rtype);
    }

    public void setRtype(String rtype) {
        editer.putString("rtype", rtype);
        editer.commit();
    }


    public String getCname() {
        return pref.getString("cname", cname);
    }

    public void setCname(String cname) {
        editer.putString("cname", cname);
        editer.commit();
    }



    public String getName() {
        return pref.getString("name", name);
    }

    public void setName(String name) {
        editer.putString("name", name);
        editer.commit();
    }

    public String getType() {
        return pref.getString("type", type);
    }

    public void setType(String type) {
        editer.putString("type", type);
        editer.commit();
    }



    public String getId() {
        return pref.getString("id", id);
    }

    public void setId(String id) {
        editer.putString("id", id);
        editer.commit();
    }

    private String phone = "";



    public String getPhone() {
        return pref.getString("phone", phone);
    }

    public void setPhone(String phone) {
        editer.putString("phone", phone);
        editer.commit();
    }

    public String getDaily() {
        return pref.getString("daily", daily);
    }

    public void setDaily(String daily) {
        editer.putString("daily", daily);
        editer.commit();
    }

    public String getSpot() {
        return pref.getString("spot", spot);
    }

    public void setSpot(String spot) {
        editer.putString("spot", spot);
        editer.commit();
    }

    public String getRisk() {
        return pref.getString("risk", risk);
    }

    public void setRisk(String risk) {
        editer.putString("risk", risk);
        editer.commit();
    }

    public String getEquip() {
        return pref.getString("equip", equip);
    }

    public void setEquip(String equip) {
        editer.putString("equip", equip);
        editer.commit();
    }

    public String getTunnal() {
        return pref.getString("tunnal", tunnal);
    }

    public void setTunnal(String tunnal) {
        editer.putString("tunnal", tunnal);
        editer.commit();
    }

    public String getCycle() {
        return pref.getString("cycle", cycle);
    }

    public void setCycle(String cycle) {
        editer.putString("cycle", cycle);
        editer.commit();
    }

    public String getMeasure() {
        return pref.getString("measure", measure);
    }

    public void setMeasure(String measure) {
        editer.putString("measure", measure);
        editer.commit();
    }

    public String getEtc() {
        return pref.getString("etc", etc);
    }

    public void setEtc(String etc) {
        editer.putString("etc", etc);
        editer.commit();
    }

    @SuppressWarnings("deprecation")
    public static void showNotification(Context ctx, int nIDIcon, int id, String strTicker, String strTitle, String strText, Intent intent, boolean fSound, int number) {
        // TODO Auto-generated method stub

        final NotificationManager nm = (NotificationManager)ctx.getSystemService(NOTIFICATION_SERVICE);
        String version;
        double ver = 0.0;
        version = android.os.Build.VERSION.RELEASE;
        version = version.split("\\.")[0]+"."+version.split("\\.")[1].replace("\\.", "");
        ver = Double.parseDouble(version);
        PendingIntent intentPending = PendingIntent.getActivity(ctx, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setAutoCancel(true)
                .setContentIntent(intentPending)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(strTitle)
                .setContentText(strText)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(nIDIcon)
                .setNumber(number)
                ;

        Notification noti =builder.build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(id, noti);

    }

    /**
     * Weather Info
     */
    private WeatherInfoVO weatherInfo = null;
    public void setWeatherInfo(WeatherInfoVO info){
        this.weatherInfo = info;
    }
    public WeatherInfoVO getWeatherInfo(){
        return weatherInfo;
    }
    int weatherCallCnt = 0;
    public int getWeatherCallCnt() {
        return pref.getInt("weatherCallCnt", weatherCallCnt);
    }

    public void setWeatherCallCnt(int weatherCallCnt) {
        this.weatherCallCnt = weatherCallCnt;
        editer.putInt("weatherCallCnt", weatherCallCnt);
        editer.commit();
    }

    long weatherCallTime = 0L;
    public long getWeatherCallTime(){
        weatherCallTime = pref.getLong("weatherCallTime", weatherCallTime);
        return weatherCallTime;
    }

    public void setWeatherCallTime(long time){
        weatherCallTime = time;
        editer.putLong("weatherCallTime", weatherCallTime);
        editer.commit();
    }
}