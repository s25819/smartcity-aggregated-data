package pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AvailableSensorsRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AvailableSensorsResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.SensorAggregateResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.SensorAggregateData;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.repositories.AirQualityAveragesRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorAggregatesServiceImpl implements pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.SensorAggregatesService {

    private final AirQualityAveragesRepository airRepo;
//    private final TrafficFlowAveragesRepository trafficRepo;

    @Override
    public List<SensorAggregateResponseDto> getAggregates(String sensorType, String sensorId, String start, String end, String resolution) {
        Instant startTime = Instant.parse(start);
        Instant endTime = (end == null || end.isBlank()) ? getEndOfDay(startTime) : Instant.parse(end);

        List<? extends SensorAggregateData> records = switch (sensorType.toUpperCase()) {
            case "AIR_QUALITY" -> (sensorId == null)
                    ? airRepo.findByStartWindowBetween(startTime, endTime)
                    : airRepo.findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime);
//            case "TRAFFIC_FLOW" -> (sensorId == null)
//                    ? trafficRepo.findByStartWindowBetween(startTime, endTime)
//                    : trafficRepo.findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime);
            default -> throw new IllegalArgumentException("Nieznany typ sensora: " + sensorType);
        };

        return aggregate(records, resolution);
    }

    private List<SensorAggregateResponseDto> aggregate(List<? extends SensorAggregateData> records, String resolution) {
        return records.stream()
                .collect(Collectors.groupingBy(r -> roundInstant(r.getStartWindow(), resolution)))
                .entrySet().stream()
                .flatMap(entry -> {
                    Instant timestamp = entry.getKey();
                    Map<String, List<SensorAggregateData>> bySensor = entry.getValue().stream()
                            .collect(Collectors.groupingBy(SensorAggregateData::getSensorId));

                    return bySensor.entrySet().stream().map(sensorGroup -> {
                        String sensorId = sensorGroup.getKey();
                        List<SensorAggregateData> group = sensorGroup.getValue();
                        Map<String, Double> avgValues = group.get(0).getValues().keySet().stream()
                                .collect(Collectors.toMap(
                                        k -> k,
                                        k -> group.stream().mapToDouble(e -> e.getValues().getOrDefault(k, 0.0)).average().orElse(0)
                                ));
                        return new SensorAggregateResponseDto(sensorId, group.get(0).getSensorType(), timestamp, avgValues);
                    });
                }).sorted(Comparator.comparing(SensorAggregateResponseDto::timestamp)).toList();
    }

    private Instant getEndOfDay(Instant instant) {
        return instant.atZone(ZoneId.of("UTC")).toLocalDate()
                .atTime(23, 59, 59, 999_999_999)
                .atZone(ZoneId.of("UTC")).toInstant();
    }

    private Instant roundInstant(Instant instant, String resolution) {
        ZonedDateTime zdt = instant.atZone(ZoneId.of("UTC"));
        ZonedDateTime rounded = switch (resolution) {
            case "1min" -> zdt.truncatedTo(ChronoUnit.MINUTES);
            case "5min" -> zdt.withMinute((zdt.getMinute() / 5) * 5).truncatedTo(ChronoUnit.MINUTES);
            case "15min" -> zdt.withMinute((zdt.getMinute() / 15) * 15).truncatedTo(ChronoUnit.MINUTES);
            case "1h" -> zdt.truncatedTo(ChronoUnit.HOURS);
            case "1d" -> zdt.truncatedTo(ChronoUnit.DAYS);
            default -> zdt.truncatedTo(ChronoUnit.SECONDS);
        };
        return rounded.toInstant();
    }

    @Override
    public List<AvailableSensorsResponseDto> getAvailableSensors(AvailableSensorsRequestDto request) {
        var sensors = switch (request.sensorType().toUpperCase()) {
            case "AIR_QUALITY" -> airRepo.findDistinctSensorIds();
            default -> throw new IllegalArgumentException("Nieznany typ czujnika: " + request.sensorType());
        };

        return sensors.stream().map(AvailableSensorsResponseDto::new).toList();
    }
}
