package com.spring_kafka.springkafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;


@SpringBootApplication
public class KafkaApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);
		
		MessageProducer producer = context.getBean(MessageProducer.class);
        MessageListener listener = context.getBean(MessageListener.class);
        
        producer.sendMessage("Hello, World!");
        listener.latch.await(10, TimeUnit.SECONDS);
        
		context.close();
	}

	@Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }
    
	/*
	 * Publishing Messages
	 */
	public static class MessageProducer {

		@Autowired
		private KafkaTemplate<String, String> kafkaTemplate;

		@Autowired
		private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

		@Value(value = "${message.topic.name}")
		private String topicName;

		@Value(value = "${partitioned.topic.name}")
		private String partitionedTopicName;

		@Value(value = "${filtered.topic.name}")
		private String filteredTopicName;

		@Value(value = "${greeting.topic.name}")
		private String greetingTopicName;
		
		public void sendMessage(String message) {
			CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(topicName, message);
			
			completableFuture.whenComplete((result, ex) -> {
				if ( ex == null) {
					 System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                     .offset() + "]");
				} else {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
			});
		}

		public void sendMessageToPartition(String message, int partition) {
			kafkaTemplate.send(partitionedTopicName, partition, null, message);
		}
		
		public void sendMessageToFiltered(String message) {
            kafkaTemplate.send(filteredTopicName, message);
        }

        public void sendGreetingMessage(Greeting greeting) {
            greetingKafkaTemplate.send(greetingTopicName, greeting);
        }
	}
	
	
	/*
	 * Consuming Messages
	 */
	public static class MessageListener {
		
		private CountDownLatch latch = new CountDownLatch(3);
        private CountDownLatch partitionLatch = new CountDownLatch(2);
        private CountDownLatch filterLatch = new CountDownLatch(2);
        private CountDownLatch greetingLatch = new CountDownLatch(1);

        @KafkaListener(topics = "${message.topic.name}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
        public void listenGroupFoo(String message) {
            System.out.println("########## -----------> Received Message in group 'foo': " + message);
            latch.countDown();
        }

	}
}
