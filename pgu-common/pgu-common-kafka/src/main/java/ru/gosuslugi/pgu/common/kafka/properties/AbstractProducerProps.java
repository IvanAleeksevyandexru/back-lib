package ru.gosuslugi.pgu.common.kafka.properties;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public abstract class AbstractProducerProps {

    protected boolean enabled = false;
    @NotEmpty
    protected String topicName;
    protected boolean autoCreateTopic = false;
    protected int topicPartitions = 1;
    protected short topicReplicationFactor = 1;

}
