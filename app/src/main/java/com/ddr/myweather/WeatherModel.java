package com.ddr.myweather;

import java.io.Serializable;
import java.util.Date;

public class WeatherModel implements Serializable {

    private String dayOfWeek;
    private Date date;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private String weatherType;
    private String weatherDescription;
    private String iconId;
    private Double rainVolume;

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public Double getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(Double rainVolume) {
        this.rainVolume = rainVolume;
    }

    @Override
    public String toString() {
        return "Model{" +
                "dayOfWeek='" + dayOfWeek + '\'' +
                ", date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", weatherType='" + weatherType + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", iconId='" + iconId + '\'' +
                ", rainVolume=" + rainVolume +
                '}';
    }
}
