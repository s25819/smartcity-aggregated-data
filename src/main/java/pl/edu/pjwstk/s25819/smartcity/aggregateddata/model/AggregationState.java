package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.Instant;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AggregationState {
    public double sumPm10 = 0;
    public double sumPm25 = 0;
    public double sumTemp = 0;
    public int sumHumidity = 0;
    public long count = 0;

    public AggregationState add(AirQualityObserved d) {
        sumPm10 += d.pm10;
        sumPm25 += d.pm2_5;
        sumTemp += d.temperature;
        sumHumidity += d.humidity;
        count++;
        return this;
    }

    public AirQualityAverage computeAverage(String sensorId, Instant start, Instant end) {
        return new AirQualityAverage(sensorId, sumPm10 / count, sumPm25 / count, sumTemp / count, (double) sumHumidity / count, start, end);
    }
}