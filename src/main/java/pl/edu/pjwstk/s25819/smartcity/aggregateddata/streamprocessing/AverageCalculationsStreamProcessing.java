package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.config.KafkaTopicsConfig;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AggregationState;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAverage;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.serdes.JsonSerde;
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

    public void buildPipeline(StreamsBuilder builder) {
        JsonSerde<AggregationState> stateSerde = new JsonSerde<>(AggregationState.class);
        JsonSerde<AirQualityAverage> outputSerde = new JsonSerde<>(AirQualityAverage.class);

        builder.stream(kafkaTopicsConfig.getAirQualityTopic() , Consumed.with(Serdes.String(), airQualityObservedSerde))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(WINDOW_SIZE))
                .aggregate(
                        AggregationState::new,
                        (key, value, aggregate) -> aggregate.add(value),
                        Materialized.with(Serdes.String(), stateSerde)
                )
                .toStream()
                .map((windowedKey, aggState) -> {
                    Instant start = Instant.ofEpochMilli(windowedKey.window().start());
                    Instant end = Instant.ofEpochMilli(windowedKey.window().end());
                    AirQualityAverage avg = aggState.computeAverage(windowedKey.key(), start, end);
                    return new KeyValue<>(windowedKey.key(), avg);
                })
                .to("smartcity-averages", Produced.with(Serdes.String(), outputSerde));
    }
}