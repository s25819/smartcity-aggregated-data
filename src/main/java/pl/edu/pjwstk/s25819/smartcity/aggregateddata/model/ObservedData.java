package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import java.time.Instant;

public interface ObservedData {
    String getId();
    String getType();
    Instant getDateObserved();
    String getSensorKey();
}
