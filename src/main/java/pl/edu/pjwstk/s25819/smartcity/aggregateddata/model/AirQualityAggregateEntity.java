package pl.edu.pjwstk.s25819.smartcity.aggregateddata.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "air_quality_averages")
@Data
@NoArgsConstructor
public class AirQualityAggregateEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String sensorId;
    private Double avgPm10;
    private Double avgPm25;
    private Double avgTemperature;
    private Double avgHumidity;

    @Column(name = "start_window")
    private Instant startWindow;

    @Column(name = "end_window")
    private Instant endWindow;
}
