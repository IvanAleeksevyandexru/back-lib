package ru.gosuslugi.pgu.terrabyte.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties("terrabyte.client")
public class TerrabyteClientProperties {

    /**
     * например http://pgu-dev-feddsnlb.test.gosuslugi.ru/storageservice/api/storage/v1
     */
    private String storageUrl;

    /**
     * например http://pgu-dev-feddsnlb.test.gosuslugi.ru/storageservice/internal/api/storage
     */
    private String internalStorageUrl;

    /**
     * например http://pgu-dev-feddsnlb.test.gosuslugi.ru/storageservice/internal/api/dataservice
     */
    private String internalDataserviceUrl;

}
