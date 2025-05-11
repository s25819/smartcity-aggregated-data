package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityObserved implements ObservedData {
    public String id;
    public String type;
    public String dateObserved;
    public Location location;
    public double pm10;
    public double pm2_5;
    public double temperature;
    public int humidity;

    public static class Location {
        public String type;
        public double[] coordinates;
    }

    @Override
    public Instant getDateObserved() {
        return Instant.parse(dateObserved);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getSensorKey() {
        return id.replace("AirQualityObserved:", "");
    }
}
