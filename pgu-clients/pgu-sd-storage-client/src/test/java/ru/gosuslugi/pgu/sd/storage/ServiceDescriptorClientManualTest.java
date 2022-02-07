package ru.gosuslugi.pgu.sd.storage;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.dto.descriptor.ServiceDescriptor;
import ru.gosuslugi.pgu.sd.storage.client.ServiceDescriptorClientImpl;
import ru.gosuslugi.pgu.sd.storage.config.ServiceDescriptorClientProperties;
import ru.gosuslugi.pgu.sd.storage.util.AbstractRestTemplateTest;

import static org.junit.Assert.assertNotNull;

/**
 * Integration tests for {@link ServiceDescriptorClient} methods
 *
 * @author ebalovnev
 */
public class ServiceDescriptorClientManualTest extends AbstractRestTemplateTest {

    /** Test URL */
    public static final String TEST_URL = "http://localhost:8096";

    private final RestTemplate restTemplate = new RestTemplate();

    @Ignore
    @Test
    public void testGet() {
        ServiceDescriptorClient client = new ServiceDescriptorClientImpl(restTemplate, getProperties());
        ServiceDescriptor descriptor = client.getServiceDescriptor("1111");
        assertNotNull(descriptor);
    }

    private ServiceDescriptorClientProperties getProperties() {
        ServiceDescriptorClientProperties result = new ServiceDescriptorClientProperties();
        result.setUrl(TEST_URL);
        return result;
    }
}
