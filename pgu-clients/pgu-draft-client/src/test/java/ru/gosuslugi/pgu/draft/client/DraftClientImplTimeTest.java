package ru.gosuslugi.pgu.draft.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DraftClientImplTimeTest {

    @Test
    public void toSecondsFromDays() {
        assertEquals(24 * 60 * 60, DraftClientImpl.toSecondsFromDays(1));
        assertEquals(7776000, DraftClientImpl.toSecondsFromDays(90));
    }
}
