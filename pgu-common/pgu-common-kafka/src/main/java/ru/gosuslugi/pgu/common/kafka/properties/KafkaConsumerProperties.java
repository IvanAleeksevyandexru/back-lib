package ru.gosuslugi.pgu.common.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

/**
 * Поля конфигурация для получателя kafka сообщений
 */
@Getter
@Setter
@ToString
public class KafkaConsumerProperties {

    private boolean enabled = false;

    /** название топика, который слушает консюмер */
    private String topic;

    private String groupId = "0";

    private String retryTopic;

    /**
     * Maximum number of records returned in a single call to poll().
     */
    private int maxPollRecords = 100;

    /**
     * Timeout to use when polling the consumer.
     */
    private Duration pollTimeout;

    private Integer fetchMaxSize;

    private Integer partitionFetchBytes;

    private Integer concurrency = 1;

    private String threadPrefixName = "kafka-consumer";

    private int processingThreads = 1;

    private boolean batchProcessing = false;

    private long executeThreadWaitTimeSec = 600;

}
