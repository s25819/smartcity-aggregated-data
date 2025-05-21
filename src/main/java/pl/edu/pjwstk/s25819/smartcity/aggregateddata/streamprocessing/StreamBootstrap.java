package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamBootstrap {

    private final StreamsBuilder builder;
    private final StreamsConfig streamsConfig;
    private final AverageCalculationsStreamProcessing streamLogic;

    private KafkaStreams streams;

    @PostConstruct
    public void start() {
        streamLogic.buildPipeline(builder);
        Topology topology = builder.build();
        this.streams = new KafkaStreams(topology, streamsConfig);
        this.streams.start();
    }

    @PreDestroy
    public void stop() {
        if (streams != null) {
            streams.close();
        }
    }
}