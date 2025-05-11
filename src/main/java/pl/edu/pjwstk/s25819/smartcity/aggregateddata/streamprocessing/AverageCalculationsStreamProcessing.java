package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AggregationState;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityAverage;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.model.AirQualityObserved;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.serdes.JsonSerde;


import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
public class AverageCalculationsStreamProcessing {

    private static final Duration WINDOW_SIZE = Duration.ofSeconds(30);

    public void buildPipeline(StreamsBuilder builder) {
        JsonSerde<AirQualityObserved> inputSerde = new JsonSerde<>(AirQualityObserved.class);
        JsonSerde<AggregationState> stateSerde = new JsonSerde<>(AggregationState.class);
        JsonSerde<AirQualityAverage> outputSerde = new JsonSerde<>(AirQualityAverage.class);

        builder.stream("sensor-airqualitysensor", Consumed.with(Serdes.String(), inputSerde))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(30)))
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