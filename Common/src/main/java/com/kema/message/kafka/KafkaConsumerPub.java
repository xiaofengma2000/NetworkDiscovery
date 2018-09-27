package com.kema.message.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class KafkaConsumerPub<T> implements Flow.Publisher<T>{

    static final Logger logger = LoggerFactory.getLogger(KafkaConsumerPub.class);

    static final int MAX_EVENT = 100;

    private final SubmissionPublisher<T> submissionPublisher = new SubmissionPublisher<>();

    KafkaConsumer<?, T> consumer;

    public KafkaConsumerPub(KafkaConsumer<?, T> consumer) {
        this.consumer = consumer;
        start();
    }

    protected void start() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (true) {
                    ConsumerRecords<?, T> records = consumer.poll(Duration.ofSeconds(10));
                    for (ConsumerRecord<?, T> record : records) {
                        logger.debug("topic = {}, partition = {}, offset = {}, key = {}, value = {}",
                                record.topic(), record.partition(), record.offset(),
                                record.key(), record.value());
                        submit(record.value());
                    }
                }
            } finally {
                consumer.close();
            }

        });
    }

    public int submit(T item) {
        //check the estimated maximum to prevent over feed the publisher
        final int submit = submissionPublisher.submit(item);
        while(submit > MAX_EVENT){
            //wait until some events are consumed
            try {
                Thread.sleep(Duration.ofSeconds(5).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return submit;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        submissionPublisher.subscribe(subscriber);
        logger.info("Subscriber {} registered.", subscriber);
    }

}
