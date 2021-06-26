package com.ddr.myweather;

import java.io.Serializable;
import java.util.Date;

public class WeatherModel implements Serializable {

    private String dayOfWeek;
    private Date date;
    private String temperature;
    private Double humidity;
    private String windSpeed;
    private String weatherType;
    private String weatherDescription;
    private String iconId;
    private Double rainVolume;
    private String cityName;
    private String country;
    private Integer population;
    private Integer timezone;
    private Double lon;
    private Double lat;
    private Integer iconNumber;

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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getIconNumber() {
        return iconNumber;
    }

    public void setIconNumber(Integer iconNumber) {
        this.iconNumber = iconNumber;
    }

    @Override
    public String toString() {
        return "WeatherModel{" +
                "dayOfWeek='" + dayOfWeek + '\'' +
                ", date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", weatherType='" + weatherType + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", iconId='" + iconId + '\'' +
                ", rainVolume=" + rainVolume +
                ", cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", population=" + population +
                ", timezone=" + timezone +
                ", lon=" + lon +
                ", lat=" + lat +
                ", iconNumber=" + iconNumber +
                '}';
    }
}
