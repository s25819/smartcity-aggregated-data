package pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAggregateEntity;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.repositories.AirQualityAveragesRepository;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.AirQualityAveragesService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AirQualityAveragesServiceImpl implements AirQualityAveragesService {
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private final AirQualityAveragesRepository repository;

    public List<AirQualityAverageResponseDto> getAverages(AirQualityAverageRequestDto request) {
        validateRequest(request);

        try {
            Instant startInstant = Instant.parse(request.startTime());
            Instant endInstant = Instant.parse(request.endTime());

            var results = repository.findBySensorIdAndStartWindowBetween(
                    request.sensorId(), startInstant, endInstant);

            if (results == null) return List.of();

            return results.stream()
                    .map(this::mapEntityToDto)
                    .toList();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Nieprawidłowy format daty. Wymagany format ISO-8601.", e);
        }
    }

    /**
     * Waliduje parametry zapytania.
     *
     * @param request obiekt z parametrami zapytania
     * @throws IllegalArgumentException gdy parametry są nieprawidłowe
     */
    private void validateRequest(AirQualityAverageRequestDto request) {
        Assert.hasText(request.sensorId(), "Identyfikator czujnika nie może być pusty");
        Assert.hasText(request.startTime(), "Czas początkowy nie może być pusty");
        Assert.hasText(request.endTime(), "Czas końcowy nie może być pusty");
    }

    /**
     * Mapuje encję bazodanową na obiekt DTO odpowiedzi.
     *
     * @param entity encja zawierająca dane z bazy danych
     * @return obiekt DTO zawierający dane do odpowiedzi
     */
    private AirQualityAverageResponseDto mapEntityToDto(AirQualityAggregateEntity entity) {
        return new AirQualityAverageResponseDto(
                entity.getSensorId(),
                entity.getAvgPm10(),
                entity.getAvgPm25(),
                entity.getAvgTemperature(),
                entity.getAvgHumidity(),
                LocalDateTime.ofInstant(entity.getStartWindow(), DEFAULT_ZONE_ID),
                LocalDateTime.ofInstant(entity.getEndWindow(), DEFAULT_ZONE_ID)
        );
    }
}