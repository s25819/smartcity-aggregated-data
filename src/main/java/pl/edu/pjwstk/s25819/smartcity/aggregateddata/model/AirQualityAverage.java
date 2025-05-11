package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import java.time.Instant;

public class AirQualityAverage {
    public String sensorId;
    public double avgPm10;
    public double avgPm25;
    public double avgTemperature;
    public double avgHumidity;
    public Instant start;
    public Instant end;

    public AirQualityAverage(String sensorId, double avgPm10, double avgPm25, double avgTemperature, double avgHumidity, Instant start, Instant end) {
        this.sensorId = sensorId;
        this.avgPm10 = avgPm10;
        this.avgPm25 = avgPm25;
        this.avgTemperature = avgTemperature;
        this.avgHumidity = avgHumidity;
        this.start = start;
        this.end = end;
    }
}
