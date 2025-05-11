package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.ObservedData;

import java.time.Instant;

public interface Aggregator<T extends ObservedData, R> {
    Aggregator<T, R> add(T data);
    R computeAverage(String sensorId, Instant windowStart, Instant windowEnd);
}