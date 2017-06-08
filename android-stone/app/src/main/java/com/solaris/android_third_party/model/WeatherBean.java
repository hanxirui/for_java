package com.solaris.android_third_party.model;

/**
 * Created by hanxirui on 2016/12/24.
 */

public class WeatherBean {
    public WeatherinfoBean weatherinfo;

    public class WeatherinfoBean {
        public String city;
        public String cityid;
        public String temp1;
        public String temp2;
        public String weather;
        public String img1;
        public String img2;
        public String ptime;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("city:");
            sb.append(city);
            sb.append("\r\n");
            sb.append("cityid:");
            sb.append(cityid);
            sb.append("\r\n");
            sb.append("temp1:");
            sb.append(temp1);
            sb.append("\r\n");
            sb.append("temp2:");
            sb.append(temp2);
            sb.append("\r\n");
            sb.append("weather:");
            sb.append(weather);
            sb.append("\r\n");
            sb.append("img1:");
            sb.append(img1);
            sb.append("\r\n");
            sb.append("img2:");
            sb.append(img2);
            sb.append("\r\n");
            sb.append("ptime:");
            sb.append(ptime);
            return sb.toString();
        }
    }
}
