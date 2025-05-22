package pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SensorAggregateResponseDto(
        String sensorId,
        String sensorType,
        Instant timestamp,
        Map<String, Double> values
) {
}
