package pl.edu.pjwstk.s25819.smartcity.aggregateddata.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonSerde<T> implements Serde<T> {
    private final ObjectMapper objectMapper;

    public JsonSerde(Class<T> clazz) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        serializer = (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException("Serializacja JSON nie powiodła się", e);
            }
        };

        deserializer = (topic, data) -> {

            log.info("Deserializacja JSON: {}", new String(data, StandardCharsets.UTF_8));

            try {
                return objectMapper.readValue(data, clazz);
            } catch (Exception e) {
                throw new RuntimeException("Deserializacja JSON nie powiodła się", e);
            }
        };
    }

    private final Serializer<T> serializer;
    private final Deserializer<T> deserializer;

    @Override
    public Serializer<T> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }
}