package kr.gsil.dpkepco.model.weather;

/**
 * Created by gsil on 2017. 9. 15..
 */

public class WeatherInfoVO {

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getAstronomy() {
        return astronomy;
    }

    public void setAstronomy(String astronomy) {
        this.astronomy = astronomy;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }

    public String getToday_temp() {
        return today_temp;
    }

    public void setToday_temp(String today_temp) {
        this.today_temp = today_temp;
    }

    public String getToday_code() {
        return today_code;
    }

    public void setToday_code(String today_code) {
        this.today_code = today_code;
    }

    public String getToday_text() {
        return today_text;
    }

    public void setToday_text(String today_text) {
        this.today_text = today_text;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    String city = null;
    String region = null;
    String condition = null;
    String wind = null;
    String astronomy = null;
    String atmosphere = null;
    String today_temp = null;
    String today_code = null;
    String today_text = null;
    String humidity = null;

    /**
     * //야후 파싱 셈플
     JSONObject jsonObj = new JSONObject(result);
     CustomJsonObject item = new CustomJsonObject(jsonObj);
     CustomJsonObject query = new CustomJsonObject(item.getString("query"));
     CustomJsonObject results = new CustomJsonObject(query.getString("results"));

     CustomJsonObject channel = new CustomJsonObject(results.getString("channel"));
     weather.setWind(channel.getString("wind"));//channel.wind; //바람세기 등//,"wind":{"chill":"77","direction":"90","speed":"14"}
     weather.setAstronomy(channel.getString("astronomy"));//channel.astronomy; //sunset,sunrise 시간//,"astronomy":{"sunrise":"6:14 am","sunset":"6:40 pm"}

     CustomJsonObject location = new CustomJsonObject(channel.getString("location"));
     weather.setCity(location.getString("city"));//location.city; //지역명 영문
     weather.setCity(location.getString("region"));//location.region; //지역명 영문
     CustomJsonObject atmosphere = new CustomJsonObject(channel.getString("atmosphere"));
     weather.setHumidity(atmosphere.getString("humidity"));//atmosphere.humidity + '%'; //강수확률//<<<<<<<<<<
     CustomJsonObject channel_item = new CustomJsonObject(channel.getString("item"));
     CustomJsonObject condition = new CustomJsonObject(channel_item.getString("condition"));

     //Math.round((condition.temp - 32)*5/9)  + '℃';  //화씨 -> 섭씨변환
     int temp = Math.round((Integer.valueOf(condition.getString("temp")) - 32)*5/9);
     weather.setToday_temp(String.valueOf(temp));//<<<<<<<<<<
     String today_code = condition.getString("code");//<<<<<<<<<<
     weather.setToday_code(today_code); //condition.code;
     String condition_text = "";//condition.getString("text");
     //condition_text = condition.getString("text");
     condition_text = Weather.w_condition_text[Integer.valueOf(today_code)];
     weather.setToday_text(condition_text); //condition.text;//<<<<<<<<<<
     */
}
