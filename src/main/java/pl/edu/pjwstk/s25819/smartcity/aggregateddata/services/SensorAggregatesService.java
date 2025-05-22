package pl.edu.pjwstk.s25819.smartcity.aggregateddata.services;

import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AvailableSensorsRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AvailableSensorsResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.SensorAggregateResponseDto;

import java.util.List;

public interface SensorAggregatesService {
    List<SensorAggregateResponseDto> getAggregates(String sensorType, String sensorId, String start, String end, String resolution);

    List<AvailableSensorsResponseDto> getAvailableSensors(AvailableSensorsRequestDto request);
}
