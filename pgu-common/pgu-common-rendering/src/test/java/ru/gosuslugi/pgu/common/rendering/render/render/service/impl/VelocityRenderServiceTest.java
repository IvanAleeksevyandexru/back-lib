package ru.gosuslugi.pgu.common.rendering.render.render.service.impl;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import ru.gosuslugi.pgu.common.rendering.render.config.VelocityConfig;
import ru.gosuslugi.pgu.common.rendering.render.config.props.VelocityProperties;
import ru.gosuslugi.pgu.common.rendering.render.data.RenderRequest;
import ru.gosuslugi.pgu.common.rendering.render.exception.RenderTemplateException;
import ru.gosuslugi.pgu.common.rendering.render.service.impl.VelocityRenderService;
import ru.gosuslugi.pgu.common.rendering.template.config.props.TemplateServiceProperties;
import ru.gosuslugi.pgu.common.rendering.template.service.TemplateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Assert;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VelocityRenderServiceTest {
    private static final String ORDER_ID_KEY = "orderId";
    private static final VelocityConfig VELOCITY_CONFIG = new VelocityConfig();
    private static final VelocityContext CONTEXT_PROTOTYPE = VELOCITY_CONFIG.prototypeTemplateContext();
    private static final VelocityEngine ENGINE;
    private static final long ORDER_ID = 12542254L;
    private static final String ORDER_ID_STR = String.valueOf(ORDER_ID);
    private static final String TEMPLATE_STORE_PATH = "render/";
    private static final String ILLEGAL_VAR_KEY = "illegalVar";
    private static final ReentrantReadWriteLock REENTRANT_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    private static final String SI_USER_REGION_CODE_14 = "14700000012";
    private static final String SI_USER_REGION_CODE_56 = "5600000041";
    private static final String SI_BILL_NUMBER = "bill#";
    private static final String SI_CODES_KEY = "codes";
    private static final String SI_USER_REGION_KEY = "userRegion";
    private static final String SERVICE_INFO_KEY = "serviceInfo";

    static {
        VelocityProperties props = new VelocityProperties();
        props.setResourceLoader(VelocityProperties.ResourceLoader.CLASS);
        TemplateServiceProperties templateServiceProps = new TemplateServiceProperties();
        ENGINE = VELOCITY_CONFIG.velocityEngine(props, templateServiceProps);
    }

    private VelocityRenderService sut;

    @BeforeMethod
    public void setUp() {
        TemplateService templateService = Mockito.mock(TemplateService.class);
        Mockito.when(templateService.getOrDownloadAndLock(Mockito.any()))
                .thenReturn(REENTRANT_READ_WRITE_LOCK);

        sut = new VelocityRenderService(CONTEXT_PROTOTYPE, ENGINE, templateService);
    }

    @Test
    public void shouldResolvePlaceholder() {
        // given
        RenderRequest request = createSampleRenderRequests("placeholder.vm");

        // when
        String fileContent = sut.render(request);

        // then
        assertContains(fileContent, ORDER_ID_STR);
    }

    @Test
    public void shouldResolveServiceInfoParamWhenRequested() {
        // given
        RenderRequest request = createSampleRenderRequests("service-info.vm");
        request.getContext().clear();
        request.getContext().put(SERVICE_INFO_KEY, createSampleServiceInfo());

        // when
        String fileContent = sut.render(request);

        // then
        assertContains(fileContent, SI_BILL_NUMBER);
        assertContains(fileContent, SI_USER_REGION_CODE_56);
    }

    @Test
    public void shouldApplyCustomFunction() {
        // given
        RenderRequest request = createSampleRenderRequests("custom-function.vm");
        request.getContext().put("toCapitalize", "foo");

        // when
        String fileContent = sut.render(request);

        // then
        assertContains(fileContent, "Foo");
    }

    @Test
    public void shouldErrorInfoContainsVarNameWhenVarNotFound() {
        // given
        RenderRequest requests = createSampleRenderRequests("illegal-var.vm");
        requests.getContext().remove(ILLEGAL_VAR_KEY);

        // when
        Assert.assertThrows(RenderTemplateException.class, () -> sut.render(requests));
    }

    private RenderRequest createSampleRenderRequests(final String templateFileName) {
        RenderRequest request = new RenderRequest();
        request.setTemplateFileName(TEMPLATE_STORE_PATH + templateFileName);
        request.getContext().put(ORDER_ID_KEY, ORDER_ID);
        return request;
    }

    private Map<String, Object> createSampleServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> userRegion = new HashMap<>();
        userRegion.put(SI_CODES_KEY, List.of(SI_USER_REGION_CODE_14, SI_USER_REGION_CODE_56));
        info.put(SI_USER_REGION_KEY, userRegion);
        info.put("billNumber", SI_BILL_NUMBER);
        return info;
    }

    private void assertContains(String fileContent, final String needle) {
        assertNotNull(fileContent);
        assertFalse(fileContent.isEmpty());
        assertTrue(fileContent.contains(needle));
    }
}
