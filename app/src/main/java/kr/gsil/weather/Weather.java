package kr.gsil.weather;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by gsil on 2017. 9. 15..
 */

public class Weather {
    /***YAHOO**/
    public static String[] location_name = {"서울특별시","대구광역시","인천광역시",
            "광주광역시","대전광역시","울산광역시",
            "세종특별자치시","경기도","강원도",
            "충청북도","충청남도","전라북도",
            "전라남도","경상북도","경상남도","제주특별자치도"};
    //var location_name_en = ["Seoul","Gyeongsangbuk-Do","Jaeju-Do"];
    public static String[] location_code = {"1132599","1132466","1132496",
            "1132481","1132467","1132578",
            "1122639","2345969","2345966", //세종시는 공주시위치를 넣어주었음
            "2345965","2345973","2345964",
            "2345972","2345970","2345967","2345963"};

    public static String[] w_condition_text = {"토네이도","열대폭풍우","태풍","맹렬한 뇌우","뇌우", //4
            "눈/비", "비/진눈깨비","눈/진눈깨비","결빙성의 이슬비","이슬비",//9
            "어는비"," 소나기","소나기"," 눈을 동반한 돌풍","소나기",//14
            "날린 눈","눈","우박","진눈깨비","먼지",//19
            "안개","안개","연기","거센 바람","바람",//24
            "추움","흐림","구름 많음","구름 많음","구름 조금",//29
            "구름 조금","갬","맑음","갬","갬", //34
            "비/우박","맑음(hot)","뇌우","뇌우","뇌우",//39
            "소나기","폭설","소나기","폭설","구름 조금",//44
            "구름 조금","눈/소나기","뇌우"};//47


    public static String getYahooUrl(String addr){
        if(addr == "" || addr == null) addr = "서울특별시";
        String[] tok = addr.split(" ");
        String cur_location_name = tok[0];
        String cur_location_code = "";
        /**location_name 으로 code로 변환**/
        for(int i = 0 ; i < location_name.length ; i++){
            if(location_name[i].equals(cur_location_name)){
                cur_location_code = location_code[i];
                break;
            }
        }
        Log.e("getYahooUrl","cur_location_name = "+cur_location_name + " cur_location_code = "+cur_location_code);
        return "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid=" + cur_location_code +"&format=json";
    }

    /**
     var city = data.query.results.channel.location.city; //지역명 영문
     var region = data.query.results.channel.location.region; //지역명 영문

     var condition = data.query.results.channel.item.condition; //바람세기 등
     var wind = data.query.results.channel.wind; //바람세기 등
     var astronomy = data.query.results.channel.astronomy; //sunset,sunrise 시간
     var atmosphere = data.query.results.channel.atmosphere;// 강수량 등

     var today_temp = Math.round((condition.temp - 32)*5/9)  + '℃';  //화씨 -> 섭씨변환

     */
}
