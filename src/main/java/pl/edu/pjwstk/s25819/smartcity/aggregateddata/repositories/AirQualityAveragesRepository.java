package pl.edu.pjwstk.s25819.smartcity.aggregateddata.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAggregateEntity;

import java.time.Instant;
import java.util.List;

public interface AirQualityAveragesRepository extends JpaRepository<AirQualityAggregateEntity, Long> {

    List<AirQualityAggregateEntity> findBySensorIdAndStartWindowBetween(String sensorId, Instant startWindow, Instant endWindow);
}
