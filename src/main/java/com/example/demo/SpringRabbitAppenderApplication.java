package com.example.demo;

import java.time.LocalDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SpringRabbitAppenderApplication {

	Log logger = LogFactory.getLog(SpringRabbitAppenderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringRabbitAppenderApplication.class, args);
	}

	@Scheduled(fixedDelay = 2000)
	public void print() {
		logger.info("Logs sent to amqp" + LocalDateTime.now());
	}
	
	@Bean
	public Exchange exchange() {
		return ExchangeBuilder.fanoutExchange("exchange1").build();
	}
	@Bean
	public Queue queue() {
		return QueueBuilder.durable("Q1").build();
	}
	@Bean
	public Binding getBinding() {
		return BindingBuilder.bind(queue()).to(exchange()).with("logs-test").noargs();
	}

	@RabbitListener(queues="Q1")
	public void getMessage(String message) {
		System.out.println("Received: "+message);
	}
}
