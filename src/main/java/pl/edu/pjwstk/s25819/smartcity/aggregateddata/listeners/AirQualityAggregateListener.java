package pl.edu.pjwstk.s25819.smartcity.aggregateddata.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAggregateEntity;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.repositories.AirQualityAveragesRepository;
import pl.edu.pjwstk.s25819.smartcity.sensors.avro.model.AirQualityAggregate;

@Component
@RequiredArgsConstructor
public class AirQualityAggregateListener {

    private final AirQualityAveragesRepository repository;

    @KafkaListener(topics = "${spring.kafka.topics.airquality-averages}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void handleMessages(AirQualityAggregate message) {

        AirQualityAggregateEntity entity = new AirQualityAggregateEntity();
        entity.setAvgPm10(message.getAvgPm10());
        entity.setAvgPm25(message.getAvgPm25());
        entity.setAvgTemperature(message.getAvgTemperature());
        entity.setAvgHumidity(message.getAvgHumidity());
        entity.setSensorId(message.getSensorId().toString());
        entity.setStartWindow(message.getStart());
        entity.setEndWindow(message.getEnd());

        repository.save(entity);
    }
}
