package pl.edu.pjwstk.s25819.smartcity.aggregateddata.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAggregateEntity;

import java.time.Instant;
import java.util.List;

public interface AirQualityAveragesRepository extends JpaRepository<AirQualityAggregateEntity, Long> {

    List<AirQualityAggregateEntity> findBySensorIdAndStartWindowBetween(String sensorId, Instant start, Instant end);

    List<AirQualityAggregateEntity> findByStartWindowBetween(Instant start, Instant end);

    @Query("SELECT DISTINCT a.sensorId FROM AirQualityAggregateEntity a")
    List<String> findDistinctSensorIds();
}
