package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import pl.edu.pjwstk.s25819.smartcity.sensors.avro.model.AirQualityObserved;

import java.time.Instant;

public record AirQualityObservedAdapter(AirQualityObserved avroData) implements ObservedData {

    @Override
    public String getId() {
        return avroData.getId().toString();
    }

    @Override
    public String getType() {
        return avroData.getType().toString();
    }

    @Override
    public Instant getDateObserved() {
        return avroData.getDateObserved();
    }

    @Override
    public String getSensorKey() {
        return avroData.getId().toString();
    }
}
