package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import java.time.Instant;
import java.util.Map;

public interface SensorAggregateData {
    String getSensorId();

    Instant getStartWindow();

    String getSensorType();

    Map<String, Double> getValues();
}