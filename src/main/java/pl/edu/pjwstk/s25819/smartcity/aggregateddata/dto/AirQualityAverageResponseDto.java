package pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto;

import java.time.LocalDateTime;

public record AirQualityAverageResponseDto(
        String sensorId,
        Double pm10,
        Double pm25,
        Double temperature,
        Double humidity,
        LocalDateTime startWindow,
        LocalDateTime endWindow
) {
}
