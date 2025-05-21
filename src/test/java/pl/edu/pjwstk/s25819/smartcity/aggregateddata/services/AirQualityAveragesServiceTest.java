package pl.edu.pjwstk.s25819.smartcity.aggregateddata.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAggregateEntity;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.repositories.AirQualityAveragesRepository;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.impl.AirQualityAveragesServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AirQualityAveragesServiceTest {

    @Mock
    private AirQualityAveragesRepository repository;

    @InjectMocks
    private AirQualityAveragesServiceImpl service;

    public AirQualityAveragesServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowExceptionWhenInvalidDateFormat() {
        // Arrange
        String sensorId = "sensor123";
        String invalidStartTime = "invalid-date";
        String invalidEndTime = "invalid-date";
        AirQualityAverageRequestDto request = new AirQualityAverageRequestDto(
                sensorId,
                invalidStartTime,
                invalidEndTime
        );

        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> service.getAverages(request));
        assertEquals("Nieprawidłowy format daty. Wymagany format ISO-8601.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSensorIdIsMissing() {
        // Arrange
        String invalidSensorId = "";
        Instant startTime = Instant.parse("2023-10-01T00:00:00Z");
        Instant endTime = Instant.parse("2023-10-01T23:59:59Z");
        AirQualityAverageRequestDto request = new AirQualityAverageRequestDto(
                invalidSensorId,
                startTime.toString(),
                endTime.toString()
        );

        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> service.getAverages(request));
        assertEquals("Identyfikator czujnika nie może być pusty", exception.getMessage());
    }

    @Test
    void shouldReturnAveragesWhenValidRequest() {
        // Arrange
        String sensorId = "sensor123";
        Instant startTime = Instant.parse("2023-10-01T00:00:00Z");
        Instant endTime = Instant.parse("2023-10-01T23:59:59Z");
        AirQualityAverageRequestDto request = new AirQualityAverageRequestDto(
                sensorId,
                startTime.toString(),
                endTime.toString()
        );

        AirQualityAggregateEntity entity = new AirQualityAggregateEntity();
        entity.setSensorId(sensorId);
        entity.setAvgPm10(15.5);
        entity.setAvgPm25(8.3);
        entity.setAvgTemperature(20.0);
        entity.setAvgHumidity(65.0);
        entity.setStartWindow(startTime);
        entity.setEndWindow(endTime);

        when(repository.findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime))
                .thenReturn(List.of(entity));

        // Act
        List<AirQualityAverageResponseDto> response = service.getAverages(request);

        // Assert
        assertEquals(1, response.size());
        AirQualityAverageResponseDto result = response.get(0);
        assertEquals(sensorId, result.sensorId());
        assertEquals(15.5, result.pm10());
        assertEquals(8.3, result.pm25());
        assertEquals(20.0, result.temperature());
        assertEquals(65.0, result.humidity());
        assertEquals(LocalDateTime.ofInstant(startTime, ZoneId.systemDefault()), result.startWindow());
        assertEquals(LocalDateTime.ofInstant(endTime, ZoneId.systemDefault()), result.endWindow());
        verify(repository, times(1)).findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime);
    }

    @Test
    void shouldReturnEmptyListWhenNoDataFound() {
        // Arrange
        String sensorId = "sensor123";
        Instant startTime = Instant.parse("2023-10-01T00:00:00Z");
        Instant endTime = Instant.parse("2023-10-01T23:59:59Z");
        AirQualityAverageRequestDto request = new AirQualityAverageRequestDto(
                sensorId,
                startTime.toString(),
                endTime.toString()
        );

        when(repository.findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime))
                .thenReturn(List.of());

        // Act
        List<AirQualityAverageResponseDto> response = service.getAverages(request);

        // Assert
        assertEquals(0, response.size());
        verify(repository, times(1)).findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime);
    }

    @Test
    void shouldHandleNullRepositoryResponse() {
        // Arrange
        String sensorId = "sensor123";
        Instant startTime = Instant.parse("2023-10-01T00:00:00Z");
        Instant endTime = Instant.parse("2023-10-01T23:59:59Z");
        AirQualityAverageRequestDto request = new AirQualityAverageRequestDto(
                sensorId,
                startTime.toString(),
                endTime.toString()
        );

        when(repository.findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime))
                .thenReturn(null);

        // Act
        List<AirQualityAverageResponseDto> response = service.getAverages(request);

        // Assert
        assertEquals(0, response.size());
        verify(repository, times(1)).findBySensorIdAndStartWindowBetween(sensorId, startTime, endTime);
    }
}