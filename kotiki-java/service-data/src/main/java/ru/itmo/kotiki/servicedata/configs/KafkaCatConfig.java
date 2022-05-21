package ru.itmo.kotiki.servicedata.configs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaCatConfig {

    private String groupId = "app.1";

    private String replyTopic = "resultCat";
    private String kafkaServer= "localhost:9092";

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.TYPE_MAPPINGS, "cat:ru.itmo.kotiki.servicedata.transfer.CatBuffer");
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "app.1");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "cat:ru.itmo.kotiki.servicedata.transfer.CatBuffer");

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.itmo.kotiki.servicedata.transfer.CatBuffer");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ProducerFactory<String, CatBuffer> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, CatBuffer> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, CatBuffer, CatBuffer> replyKafkaTemplate(ProducerFactory<String, CatBuffer> pf, KafkaMessageListenerContainer<String, CatBuffer> container){
        ReplyingKafkaTemplate<String, CatBuffer, CatBuffer> temp = new ReplyingKafkaTemplate<>(pf, container);
        temp.setSharedReplyTopic(true);

        return temp;
    }

    @Bean
    public KafkaMessageListenerContainer<String, CatBuffer> replyContainer(ConsumerFactory<String, CatBuffer> cf) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConsumerFactory<String, CatBuffer> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),new JsonDeserializer<>(CatBuffer.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CatBuffer>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CatBuffer> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }
}
