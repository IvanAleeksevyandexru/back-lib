package ru.gosuslugi.pgu.draft.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.dto.ScenarioDto;
import ru.gosuslugi.pgu.draft.DraftClient;
import ru.gosuslugi.pgu.draft.model.DraftHolderDto;
import ru.gosuslugi.pgu.draft.config.properties.DraftServiceProperties;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Integration tests for {@link DraftClientImpl} methods
 *
 * @author vbalovnev
 */
public class DraftClientImplMockTest {

    private RestTemplate restTemplate = new RestTemplate();
    private MockRestServiceServer mockServer;

    protected DraftClient apiClient;

    @Before
    public void init() throws Exception {
        DraftServiceProperties properties = new DraftServiceProperties();
        properties.setUrl("http://localhost:8072");
        properties.setEnabled(true);
        mockServer = MockRestServiceServer.createServer(restTemplate);
        apiClient = new DraftClientImpl(restTemplate, properties);
    }

    @Test
    public void testPut() throws URISyntaxException {
        long orderId = 117L;
        Long userId = 118L;
        Long orgId = null;

        ScenarioDto scenario = new ScenarioDto();
        scenario.setOrderId(orderId);
        String serviceId = "1";
        Integer draftTtl = 91;

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8072/internal/api/drafts/v3/117")))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(MockRestRequestMatchers.header("token", userId + "@@@@1"))
                .andExpect(content().json("{\"orderId\":" + orderId + ",\"body\":{\"serviceCode\":null,\"targetCode\":null,\"masterOrderId\":null,\"orderId\":" + orderId + ",\"currentScenarioId\":null,\"serviceId\":null,\"currentUrl\":null,\"finishedAndCurrentScreens\":[],\"cachedAnswers\":{},\"currentValue\":{},\"errors\":{},\"applicantAnswers\":{},\"cycledApplicantAnswers\":{\"currentAnswerId\":null,\"answerlist\":[]},\"participants\":{},\"display\":null,\"newContactId\":null,\"attachmentInfo\":{},\"additionalParameters\":{}},\"type\":\"" + serviceId + "\",\"locked\":false,\"updated\":null,\"ttlInSec\":" + (24 * 60 * 60 * draftTtl) + "}"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("{\"orderId\":" + orderId + ", \"body\":{\"orderId\":" + orderId + "}}")
                );

        DraftHolderDto dto = apiClient.saveDraft(scenario, serviceId, userId, orgId, draftTtl, draftTtl);
        Assert.assertEquals(dto.getOrderId(), orderId);
        Assert.assertEquals(dto.getBody().getOrderId(), Long.valueOf(orderId));

        // Verify all expectations met
        mockServer.verify();
    }

    @Test
    public void testGet() throws URISyntaxException {
        long orderId = 117L;
        Long userId = 118L;
        Long orgId = null;

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8072/internal/api/drafts/v3/117")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(MockRestRequestMatchers.header("token", userId + "@@@@1"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("{\"orderId\":" + orderId + ", \"body\":{\"orderId\":" + orderId + "}}")
                );

        DraftHolderDto dto = apiClient.getDraftById(orderId, userId, orgId);
        Assert.assertEquals(dto.getOrderId(), orderId);
        Assert.assertEquals(dto.getBody().getOrderId(), Long.valueOf(orderId));

        // Verify all expectations met
        mockServer.verify();
    }

    @Test
    public void testDelete() throws URISyntaxException {
        long orderId = 117L;
        Long userId = 118L;

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8072/internal/api/drafts/v3/117")))
                .andExpect(method(HttpMethod.DELETE))
                .andExpect(MockRestRequestMatchers.header("token", userId + "@@@@1"))
                .andRespond(withStatus(HttpStatus.OK));

        apiClient.deleteDraft(orderId, userId);

        // Verify all expectations met
        mockServer.verify();
    }
}
