package ru.gosuslugi.pgu.common.rendering.render.render.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import ru.gosuslugi.pgu.common.rendering.render.data.UsageAwareContext;

import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

public class UsageAwareContextTest {
    private static final String NON_LITERAL = "any";
    private static final String LITERAL = ".literal.";
    private static final String LITERAL_PREFIXED = ".literal.any";
    private UsageAwareContext sut;

    @Before
    public void setUp() {
        Context ctx = mock(Context.class);
        sut = new UsageAwareContext(ctx);
    }

    @Test
    public void shouldAssumeValidKeyWhenStartWithNotLiteralPrefix() {
        // given

        // when
        sut.get(NON_LITERAL);

        // then
        assertTrue(sut.getUnrecognizedKeys().isEmpty());
    }

    @Test
    public void shouldAssumeValidKeyWhenMatchLiteralPrefix() {
        // given

        // when
        sut.get(LITERAL);

        //when
        assertTrue(sut.getUnrecognizedKeys().isEmpty());
    }

    @Test
    public void shouldAssumeUnrecognisedKeyWhenStartWithLiteralPrefix() {
        // given

        // when
        sut.get(LITERAL_PREFIXED);

        // then
        assertEquals(sut.getUnrecognizedKeys().size(), 1);
    }
}
