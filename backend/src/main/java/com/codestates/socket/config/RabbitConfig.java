package com.codestates.socket.config;


import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {

//    private static final String queueName = "chat.queue";
//    private static final String exchangeName = "chat.exchange";
//    private static final String routingKey = "room.*";
//
//    @Bean
//    public Queue myQueue() {
//        return new Queue(queueName, false);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(exchangeName);
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost("localhost");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        return factory;
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter(){
//        //LocalDateTime serializable을 위해
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
//        objectMapper.registerModule(dateTimeModule());
//
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
//
//        return converter;
//    }
//
//    @Bean
//    public Module dateTimeModule(){
//        return new JavaTimeModule();
//    }
//
//    @Bean
//    RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        rabbitTemplate.setRoutingKey(routingKey);
//
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container(){
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames(queueName);
////        container.setMessageListener(null);
//        return container;
//    }


}
