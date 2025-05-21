package pl.edu.pjwstk.s25819.smartcity.aggregateddata.config;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.topics.replication-factor:3}")
    private int replicationFactor;

    public final KafkaTopicsConfig kafkaTopicsConfig;

    @Bean
    public StreamsConfig streamsConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "smartcity-averages-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        props.put("schema.registry.url", schemaRegistryUrl);
        return new StreamsConfig(props);
    }

    @Bean
    public NewTopic airQualityAveragesTopic() {
        return TopicBuilder
                .name(kafkaTopicsConfig.getAirQualityAveragesTopic())
                .replicas(replicationFactor)
                .partitions(3)
                .build();
    }

    @Bean
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public <T extends SpecificRecord> SpecificAvroSerde<T> specificAvroSerde() {
        Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", schemaRegistryUrl);
        SpecificAvroSerde<T> serde = new SpecificAvroSerde<>();
        serde.configure(serdeConfig, false); // false = value, true = key
        return serde;
    }
}