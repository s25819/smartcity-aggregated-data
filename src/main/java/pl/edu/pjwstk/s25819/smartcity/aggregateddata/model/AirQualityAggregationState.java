package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing.Aggregator;
import pl.edu.pjwstk.s25819.smartcity.sensors.avro.model.AirQualityAggregate;
import pl.edu.pjwstk.s25819.smartcity.sensors.avro.model.AirQualityObserved;

import java.time.Instant;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AirQualityAggregationState implements Aggregator<AirQualityObserved, AirQualityAggregate> {
    public double sumPm10 = 0;
    public double sumPm25 = 0;
    public double sumTemp = 0;
    public int sumHumidity = 0;
    public long count = 0;

    public AirQualityAggregationState add(AirQualityObserved d) {
        sumPm10 += d.getPm10();
        sumPm25 += d.getPm25();
        sumTemp += d.getTemperature();
        sumHumidity += d.getHumidity();
        count++;
        return this;
    }

    public AirQualityAggregate computeAverage(String sensorId, Instant start, Instant end) {
        return new AirQualityAggregate(sensorId, sumPm10 / count, sumPm25 / count, sumTemp / count, (double) sumHumidity / count, start, end);
    }
}