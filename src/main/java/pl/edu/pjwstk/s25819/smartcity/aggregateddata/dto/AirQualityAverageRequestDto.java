package pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto;

public record AirQualityAverageRequestDto(String sensorId,
                                          String startTime,
                                          String endTime) {
}
