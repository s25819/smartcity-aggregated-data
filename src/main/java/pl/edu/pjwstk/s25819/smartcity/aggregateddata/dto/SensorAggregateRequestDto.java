package pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorAggregateRequestDto(

        @NotNull(message = "Identyfikator sensora musi być podany")
        String sensorId,

        String sensorType,

        @NotBlank(message = "Początek okna czasowego musi być podany")
        String startTime,

        String endTime,

        @NotBlank(message = "Rozdzielczość nie może być pusta")
        String resolution

) {
        public SensorAggregateRequestDto(
                String sensorId,
                String sensorType,
                String startTime,
                String endTime
        ) {
                this(sensorId, sensorType, startTime, endTime, "1min");
        }
}
