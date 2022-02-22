package ru.gosuslugi.pgu.components.regex


import spock.lang.Specification

import java.util.function.Function
import java.util.regex.Pattern

class RegExpContextTest extends Specification {

    def cleanup() {
        RegExpContext.clear()
    }

    def "GetValueByRegex"() {
        when:
        def actualResult = RegExpContext.getValueByRegex(regexp, new ReplaceFirstPatternFunction(value, "after"))
        then:
        actualResult == expectedResult
        RegExpContext.getSize() == expectedSize
        where:
        value         | regexp   | expectedResult | expectedSize
        '1224 324512' | '[0-9]+' | 'after 324512' | 1
    }

    def "MatchesByRegex"() {
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

    static class ReplaceFirstPatternFunction implements Function<Pattern, String> {
        String value
        String replacement

        ReplaceFirstPatternFunction(String value, String replacement) {
            this.value = value
            this.replacement = replacement
        }

        @Override
        String apply(Pattern pattern) {
            return pattern.matcher(value).replaceFirst(replacement)
        }
    }
}
