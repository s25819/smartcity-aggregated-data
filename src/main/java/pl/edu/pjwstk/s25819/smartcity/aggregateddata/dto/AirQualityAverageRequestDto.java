package pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AirQualityAverageRequestDto(
        @NotBlank String sensorId,

        @NotBlank
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2})?Z", message = "data musi być zgodna z formatem YYYY-MM-DDThh-mm-ssZ")
        String startTime,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2})?Z", message = "data musi być zgodna z formatem YYYY-MM-DDThh-mm-ssZ")
        String endTime,

        @NotBlank
        @Pattern(regexp = "30s|1min|5min|15min|1h|1d", message = "rozdzielczność ma być z przedziału: 30s, 1min, 5min, 15min, 1h, 1d")
        String resolution
) {
}
