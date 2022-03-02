# epgu2sf-lib

## Конфигурация для получения и отправка сообщений через Kafka

### Отправка через KafkaTemplate

Пример конфигурации producer для отправки сообщений (с пояснениями в комментариях):

    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnProperty(prefix = "spring.kafka.producer.rate-limit-analytic", name = "enabled", havingValue = "true")
    public class KafkaRateLimitAnalyticProducerConfig {
    
        private final KafkaProducerCreator kafkaProducerCreator;
    
        // Создание инстанса KafkaProducerProperties с настройками из конфига
        @Bean
        @ConfigurationProperties(prefix = "spring.kafka.producer.rate-limit-analytic")
        public KafkaProducerProperties rateLimitProducerProperties() {
            return new KafkaProducerProperties();
        }

        // Создание топика, если требуются. На проде создание топиков запрещено, 
        // обязательно должна быть возможность отключения.
        @Bean
        @ConditionalOnProperty(prefix = "spring.kafka.producer.rate-limit-analytic", name = "auto-create-topics", havingValue = "true")
        public NewTopic rateLimitAnalyticsTopic() {
            return rateLimitProducerProperties().toNewTopic();
        }
    
        // Создание ProducerFactory. Нужно передать правильные сериализаторы для ключа и value.
        @Bean
        public ProducerFactory<String, RateLimitOverHeadDto> rateLimitOverHeadDtoProducerFactory() {
            return kafkaProducerCreator.createProducerFactory(new StringSerializer(), new JsonSerializer<>());
        }
    
        // Создание KafkaTemplate, с помощью которого будут отправляться сообщения
        @Bean
        public KafkaTemplate<String, RateLimitOverHeadDto> rateLimitOverHeadDtoKafkaTemplate() {
            return kafkaProducerCreator.createKafkaTemplate(rateLimitOverHeadDtoProducerFactory());
        }
    
    }


Далее, в сервисе, из которого будут отправляться сообщения, должны автовайриться KafkaProducerProperties и KafkaTemplate:

    public class RateLimitAnalyticProducerImpl implements RateLimitAnalyticProducer {
    
        private final KafkaTemplate<String, RateLimitOverHeadDto> rateLimitOverHeadDtoKafkaTemplate;
        private final KafkaProducerProperties rateLimitProducerProperties;
    
        @Override
        public void send(RateLimitOverHeadDto rateLimitOverHeadDto) {
            try {
                rateLimitOverHeadDtoKafkaTemplate.send(
                        rateLimitProducerProperties.getTopic(),
                        rateLimitOverHeadDto.getUserId(),
                        rateLimitOverHeadDto
                ).get();
                log.info("Отправлено в Rate Limit Analytic: {}", rateLimitOverHeadDto);
            } catch (ExecutionException | InterruptedException e) {
                log.error("Ошибка отправки в Rate Limit Analytic: {}", rateLimitOverHeadDto, e);
                throw new FormBaseException("Ошибка отправки в Rate Limit Analytic", e);
            }
        }
    }

В этом примере отправка происходит синхронно. Для асинхронной отправки нужно работать с Future, который возвращает 
KafkaTemplate, например так:

    ListenableFuture<SendResult<Long, SuggestOrderDto>> future =
            suggestionKafkaTemplate.send(spKafkaProducersProperties.getSuggestions().getTopic(), userId, dto);
    future.addCallback(new SendResultListener(userId, dto));

## Получение сообщений из очереди

Для обработки сообщений из очереди нужно создать класс Listener, который должен наследоваться от одного из этих абстрактных классов:

    ru.gosuslugi.pgu.common.kafka.service.AbstractMessageListener
    ru.gosuslugi.pgu.common.kafka.service.AbstractBatchMessageListener

**AbstractMessageListener** - базовый класс для обработки сообщений по одной штуке за раз

**AbstractBatchMessageListener** - базовый класс для обработки пачек сообщений в несколько потоков. При этом, если произошла ошибка при обработке одного сообщения, то откатится вся пачка.

Пример конфигурации для настройки listener:

    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnExpression("${spring.kafka.producer.sp-adapter-batch.enabled}")
    public class KafkaSpAdapterConsumerConfig {
    
        private final KafkaConsumerCreator kafkaConsumerCreator;
    
        // Создание инстанса KafkaConsumerProperties с настройками из конфига
        @Bean
        @ConfigurationProperties(prefix = "spring.kafka.consumer.sp-adapter-response")
        public KafkaConsumerProperties spAdapterResponseConsumerProperties() {
            return new KafkaConsumerProperties();
        }
    
        // Создание ConsumerFactory. Нужно передать правильные десериализаторы для ключа и value.
        @Bean
        public ConsumerFactory<Long, SpResponseOkDto> spAdapterResponseConsumerFactory() {
            return kafkaConsumerCreator.createConsumerFactory(
                    new LongDeserializer(),
                    new JsonDeserializer<>(SpResponseOkDto.class),
                    spAdapterResponseConsumerProperties()
            );
        }
    
        // Создание ListenerContainer
        // В этом примере KafkaSpAdapterResponseListener - класс-наследник от AbstractMessageListener
        @Bean 
        public ConcurrentMessageListenerContainer<Long, SpResponseOkDto> spAdapterResponseListenerContainer(
                KafkaSpAdapterResponseListener kafkaSpAdapterResponseListener
        ) {
            return kafkaConsumerCreator.createListenerContainer(
                    spAdapterResponseConsumerFactory(),
                    spAdapterResponseConsumerProperties(),
                    kafkaSpAdapterResponseListener
            );
        }
    ...
