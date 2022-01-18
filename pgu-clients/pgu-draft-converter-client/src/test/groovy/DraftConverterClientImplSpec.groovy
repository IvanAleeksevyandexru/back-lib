package ru.gosuslugi.pgu.client.draftconverter.impl

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.env.MockEnvironment
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import ru.gosuslugi.pgu.client.draftconverter.DraftConverterClient
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil
import ru.gosuslugi.pgu.dto.XmlCustomConvertRequest
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@Ignore
class DraftConverterClientImplSpec extends Specification {

    @Shared
    RestTemplate restTemplate
    MockRestServiceServer mockServer
    DraftConverterClient apiClient

    def setupSpec() {
        restTemplate = new RestTemplate()
    }

    def setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
        apiClient = new DraftConverterClientImpl('http://url_to_draft_onverter_service',
                Stub(RestTemplateBuilder),
                restTemplate,
                new MockEnvironment(),
                Stub(RestTemplateCustomizer))
    }

    @SuppressWarnings("GroovyAccessibility")
    def 'rest call test'() {
        given:
        def jsonData = JsonProcessingUtil.toJson(Map.<String, String>of("key1", "value1"))
        def requestDto = new XmlCustomConvertRequest('''xml''', '10000000360', 'MedicalOrgList', jsonData)

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(apiClient.draftConverterUrl + apiClient.CUSTOM_XML_DRAFT_REQUEST_PATH)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string('''{"xmlData":"xml","serviceId":"10000000360","templateName":"MedicalOrgList","jsonData":"{\\"key1\\":\\"value1\\"}"}'''))
                .andRespond(withSuccess(_ as String, MediaType.APPLICATION_JSON))
        when:
        apiClient.convertXmlCustom(requestDto)
        then:
        mockServer.verify()
    }

    def 'when exception'() {
        given:
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(apiClient.draftConverterUrl + apiClient.CUSTOM_XML_DRAFT_REQUEST_PATH)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST))
        when:
        apiClient.convertXmlCustom(Mock(XmlCustomConvertRequest))
        then:
        thrown(ExternalServiceException.class)
        mockServer.verify()
    }
}