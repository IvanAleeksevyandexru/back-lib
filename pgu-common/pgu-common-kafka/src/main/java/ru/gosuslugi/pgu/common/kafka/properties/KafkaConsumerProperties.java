package ru.gosuslugi.pgu.common.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

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
    private String topicName;

    private String groupId = "0";

    /** название топика для ошибок от внешней системы */
    private String errorsTopicName;
    /** название топика для ошибок в ходе исполнения */
    private String selfErrorsTopicName;
    /** название топика для сообщений об успешной отправке  */
    private String responseTopicName;

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

    private boolean batchProcessing = true;

    private long executeThreadWaitTimeSec = 600;


    /** количество партиций для ошибок */
    private Integer selfErrorsPartitions;
    private Integer errorsPartitions;
    private Integer responsePartitions;

    /** количество партиций для ошибок */
    private Short selfErrorsReplicationFactor;
    private Short errorsReplicationFactor;
    private Short responseReplicationFactor;

}
