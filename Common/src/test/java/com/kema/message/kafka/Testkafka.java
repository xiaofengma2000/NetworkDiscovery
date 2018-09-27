package com.kema.message.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Testkafka {

    private static String Topic = "test";

    static Logger logger = LoggerFactory.getLogger(Testkafka.class);

    @BeforeClass
    public static void genMsgs(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
//        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 10; i++)
            producer.send(new ProducerRecord<String, String>(Topic, Integer.toString(i), Integer.toString(i)));
        producer.close();
    }

    @Test
    public void test1(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(java.util.Arrays.asList(Topic));
        KafkaConsumerPub<String> pub = new KafkaConsumerPub<>(consumer);

        KafkaConsumerSub<String> sub = new KafkaConsumerSub<>(item -> {
            logger.info("Message received and consumed : {}", item);
        });

        pub.subscribe(sub);

        //wait until the job finishes
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
