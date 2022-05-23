package ru.itmo.kotiki.servicedata.configs;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }

    @Bean
    public Queue addOwnerQueue() {
        return new Queue("addOwner");
    }

    @Bean
    public Queue updateOwnerQueue() {
        return new Queue("updateOwner");
    }

    @Bean
    public Queue getOwnerByIdrQueue() {
        return new Queue("getOwnerById");
    }

    @Bean
    public Queue getAllOwnersQueue() {
        return new Queue("getAllOwners");
    }

    @Bean
    public Queue removeOwnerQueue() {
        return new Queue("removeOwner");
    }

    @Bean
    public Queue addCatQueue() {
        return new Queue("addCat");
    }

    @Bean
    public Queue addCatFriendQueue() {
        return new Queue("addCatFriend");
    }

    @Bean
    public Queue removeCatFriendQueue() {
        return new Queue("removeCatFriend");
    }

    @Bean
    public Queue getCatByIdQueue() {
        return new Queue("getCatById");
    }

    @Bean
    public Queue getAllCatsQueue() {
        return new Queue("getAllCats");
    }

    @Bean
    public Queue getCatsByBreedQueue() {
        return new Queue("getCatsByBreed");
    }

    @Bean
    public Queue getCatsByColorQueue() {
        return new Queue("getCatsByColor");
    }

    @Bean
    public Queue updateCatQueue() {
        return new Queue("updateCat");
    }

    @Bean
    public Queue removeCatQueue() {
        return new Queue("removeCat");
    }
}
