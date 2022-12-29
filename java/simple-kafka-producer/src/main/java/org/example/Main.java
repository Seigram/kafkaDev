package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "192.168.254.16:9092";

    public static void main(String[] args) {
                System.out.println("Hello world!");
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        String messageValue = "testMessage3";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME,"Pangyo2", "Pangyo2");
        producer.send(record);
        logger.info("{}", record);
        producer.flush();
        producer.close();
    }
}