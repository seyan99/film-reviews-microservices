package com.seyan.review.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue getUpdateHasReviewQueue() {
        return new Queue("updateHasReviewQueue");
    }
    @Bean
    public Queue getReviewCountQueue() {
        return new Queue("reviewCountQueue");
    }
    @Bean
    public Queue addReviewCommentQueue() {
        return new Queue("addReviewCommentQueue");
    }
    @Bean
    public Queue deleteReviewCommentQueue() {
        return new Queue("deleteReviewCommentQueue");
    }
    @Bean
    public Queue reviewCreationQueue() {
        return new Queue("reviewCreationQueue");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
