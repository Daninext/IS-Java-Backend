package ru.itmo.kotiki.servicedata.configs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
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
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaOwnerConfig {

    private String groupId = "owner";
    private String replyTopic = "resultOwner";
    private String kafkaServer= "localhost:9092";

    @Bean
    public Map<String, Object> ownerProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.TYPE_MAPPINGS, "owner:ru.itmo.kotiki.servicedata.transfer.OwnerBuffer");
        return props;
    }

    @Bean
    public Map<String, Object> ownerConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(JsonDeserializer.TYPE_MAPPINGS, "owner:ru.itmo.kotiki.servicedata.transfer.OwnerBuffer");

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.itmo.kotiki.servicedata.transfer.OwnerBuffer");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ProducerFactory<String, OwnerBuffer> ownerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(ownerProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, OwnerBuffer> ownerKafkaTemplate() {
        return new KafkaTemplate<>(ownerProducerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, OwnerBuffer, OwnerBuffer> ownerReplyKafkaTemplate(ProducerFactory<String, OwnerBuffer> pf, KafkaMessageListenerContainer<String, OwnerBuffer> container){
        ReplyingKafkaTemplate<String, OwnerBuffer, OwnerBuffer> temp = new ReplyingKafkaTemplate<>(pf, container);
        temp.setSharedReplyTopic(true);

        return temp;
    }

    @Bean
    public KafkaMessageListenerContainer<String, OwnerBuffer> ownerReplyContainer(ConsumerFactory<String, OwnerBuffer> cf) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConsumerFactory<String, OwnerBuffer> ownerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(ownerConsumerConfigs(),new StringDeserializer(),new JsonDeserializer<>(OwnerBuffer.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OwnerBuffer>> ownerKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OwnerBuffer> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(ownerConsumerFactory());
        factory.setReplyTemplate(ownerKafkaTemplate());
        return factory;
    }
}
