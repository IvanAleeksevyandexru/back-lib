package ru.gosuslugi.pgu.components.regex


import spock.lang.Specification

import java.util.function.Function
import java.util.regex.Pattern

class RegExpContextGetValueTest extends Specification {

    def "GetValueByRegex"() {
        RegExpContext.clear()
        when:
        def actualResult = RegExpContext.getValueByRegex(regexp, new ReplaceFirstPatternFunction(value, "after"))
        then:
        actualResult == expectedResult
        RegExpContext.getSize() == expectedSize
        where:
        value         | regexp   | expectedResult | expectedSize
        '1224 324512' | '[0-9]+' | 'after 324512' | 1
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
