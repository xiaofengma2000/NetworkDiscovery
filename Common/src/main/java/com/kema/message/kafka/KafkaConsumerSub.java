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
import java.util.function.Consumer;

public class KafkaConsumerSub<T> implements Flow.Subscriber<T> {

    static final Logger logger = LoggerFactory.getLogger(KafkaConsumerSub.class);

    static int index = 1;

    String name;

    Flow.Subscription mySubscription;

    Consumer<T> nextFunction;

    public KafkaConsumerSub(Consumer<T> func){
        this.nextFunction = func;
        this.name = "KafkaConsumerSub" + index++;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        if(mySubscription != null){
            logger.error("Already subscribed, ignore the new subscription : {}", subscription);
        } else {
            mySubscription = subscription;
            mySubscription.request(1);
        }
    }

    @Override
    public void onNext(T item) {
        mySubscription.request(1);
        nextFunction.accept(item);
        logger.info("{} item consumed : {}", name, item);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("Error : {}", throwable);
        mySubscription.cancel();
    }

    @Override
    public void onComplete() {
        mySubscription.cancel();
        logger.info("Done");
    }

}
