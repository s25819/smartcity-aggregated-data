package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import lombok.extern.slf4j.Slf4j;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAverage;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityObserved;

import java.time.Instant;

@Slf4j
public class AirQualityAggregator implements Aggregator<AirQualityObserved, AirQualityAverage> {
    private double sumPm10 = 0, sumPm25 = 0, sumTemp = 0;
    private int sumHumidity = 0;
    private long count = 0;

    public AirQualityAggregator add(AirQualityObserved d) {
        sumPm10 += d.pm10;
        sumPm25 += d.pm2_5;
        sumTemp += d.temperature;
        sumHumidity += d.humidity;
        count++;
        return this;
    }

    public AirQualityAverage computeAverage(String sensorId, Instant start, Instant end) {
        log.info("Agregacja danych dla sensora {}: {} / {} / {} / {} / {}", sensorId, sumPm10, sumPm25, sumTemp, sumHumidity, count);

        return new AirQualityAverage(sensorId, sumPm10 / count, sumPm25 / count, sumTemp / count, (double) sumHumidity / count, start, end);
    }
}

