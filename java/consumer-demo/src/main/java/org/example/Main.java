package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class Main {


    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "192.168.254.16:9092";
    private final static String GROUP_ID = "test-group";
    private static KafkaConsumer<String, String> consumer;
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        //essential config
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS); //server
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);//test-group
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); //key deserializer
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//value deserializer
        //configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); //Auto Commit default true
        //configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,6000); //Auto Commit Interval
        consumer = new KafkaConsumer<String, String>(configs);

        //consumer.subscribe(Arrays.asList(TOPIC_NAME));//default null

        //consumer.subscribe(Arrays.asList(TOPIC_NAME), new RebalanceListener());//add RebalanceListener

        consumer.subscribe(Arrays.asList(TOPIC_NAME), new RebalanceListener());//add RebalanceListener



        //get data
        try{
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for(ConsumerRecord<String,String> record : records){
                    logger.info("record:{}", record);
                }
              //  consumer.commitSync(); //explicit call commit
                //Async
            /*consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
                    if(e!=null)
                        System.err.println("Commit failed");
                    else
                        System.out.println("Commit Success");
                    if(e!=null)
                        logger.error("Commit failed for offsets {}", offsets, e);
                }
            });*/
            }
        }catch (WakeupException e){
            logger.warn("Wakeup consumer");
        }finally {
            logger.warn("Consumer close");
            consumer.close();
        }


    }

    static class ShutdownThread extends Thread {
        public void run() {
            logger.info("Shutdown hook");
            consumer.wakeup();
        }
    }
 }