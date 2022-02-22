package ru.gosuslugi.pgu.components.regex


import spock.lang.Specification

import java.util.function.Function
import java.util.regex.Pattern

class RegExpContextMatcherTest extends Specification {

    def "MatchesByRegex"() {
        RegExpContext.clear()
        when:
        for (int i = 0; i < iterations; i++) {
            RegExpContext.matchesByRegex(value + i, regexp + i + '+$')
        }
        def actualResult = RegExpContext.matchesByRegex(value, regexp)
        then:
        actualResult == expectedResult
        RegExpContext.getSize() == expectedSize
        where:
        value         | regexp        | expectedResult | iterations | expectedSize
        '1224 324512' | '^[0-7\\s]+$' | true           | 5          | 6
        '4567 111111' | '^[0-9\\s]+$' | true           | 1010       | 1011
    }
}
