package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.config.KafkaTopicsConfig;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAggregationState;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.serdes.JsonSerde;
import pl.edu.pjwstk.s25819.smartcity.sensors.avro.model.AirQualityAggregate;
import pl.edu.pjwstk.s25819.smartcity.sensors.avro.model.AirQualityObserved;


import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class AverageCalculationsStreamProcessing {

    private final KafkaTopicsConfig kafkaTopicsConfig;

    private static final Duration WINDOW_SIZE = Duration.ofSeconds(30);

    private final SpecificAvroSerde<AirQualityObserved> airQualityObservedSerde;
    private final SpecificAvroSerde<AirQualityAggregate> airQualityAggregateSerde;

    public void buildPipeline(StreamsBuilder builder) {
        JsonSerde<AirQualityAggregationState> stateSerde = new JsonSerde<>(AirQualityAggregationState.class);
        
        var consumedConfig = Consumed.with(Serdes.String(), airQualityObservedSerde);
        var producedConfig = Produced.with(Serdes.String(), airQualityAggregateSerde);
        
        builder.stream(kafkaTopicsConfig.getAirQualityTopic(), consumedConfig)
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(WINDOW_SIZE))
                .aggregate(
                        AirQualityAggregationState::new,
                        (key, value, aggregate) -> aggregate.add(value),
                        Materialized.with(Serdes.String(), stateSerde)
                )
                .toStream()
                .map(this::calculateAverages)
                .to(kafkaTopicsConfig.getAirQualityAveragesTopic(), producedConfig);
    }

    private KeyValue<String, AirQualityAggregate> calculateAverages(Windowed<String> windowedKey, AirQualityAggregationState aggState) {
        Instant startTime = Instant.ofEpochMilli(windowedKey.window().start());
        Instant endTime = Instant.ofEpochMilli(windowedKey.window().end());
        String sensorId = windowedKey.key();
        
        AirQualityAggregate avgResult = aggState.computeAverage(sensorId, startTime, endTime);
        return new KeyValue<>(sensorId, avgResult);
    }
}