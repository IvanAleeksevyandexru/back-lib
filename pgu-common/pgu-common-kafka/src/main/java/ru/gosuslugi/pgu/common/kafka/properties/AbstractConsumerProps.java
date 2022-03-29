package ru.gosuslugi.pgu.common.kafka.properties;

import lombok.Data;

import java.time.Duration;

@Data
public abstract class AbstractConsumerProps {

    protected boolean enabled;

    protected String groupId;

    protected String topicName;

    protected Integer processingThreads;

    protected boolean batchProcessing;

    protected Integer maxPollRecords;

    protected Duration pollTimeout;

    protected Integer concurrency;

    protected Integer fetchMaxSize;

    protected Integer partitionFetchBytes;

    private long executeThreadWaitTimeSec = 600;
}
