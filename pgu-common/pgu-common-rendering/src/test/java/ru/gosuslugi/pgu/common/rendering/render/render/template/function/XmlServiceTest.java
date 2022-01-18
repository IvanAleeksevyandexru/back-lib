package ru.gosuslugi.pgu.common.rendering.render.render.template.function;

import static org.testng.Assert.assertEquals;

import ru.gosuslugi.pgu.common.rendering.render.template.function.XmlService;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XmlServiceTest {
    private static final XmlService SUT = new XmlService();

    @DataProvider
    public static Object[][] xmlServiceInputCases() {
        return new Object[][]{
                {null, null},
                {"", ""},
                {"&quot;", "\""},
                {"<xml></xml>", "<xml></xml>"},
                {"&lt;xml>&lpar;\u003d&rpar;</xml&gt;", "<xml>&lpar;=&rpar;</xml>"},
                {new StringBuilder("<xml>&apos;&amp;&apos;</xml>"), "<xml>'&'</xml>"},
        };
    }

    @Test(dataProvider = "xmlServiceInputCases")
    public void shouldUnescapeCharsProperly(Object input, String expected) {
        // given
        // when
        String actual = SUT.unescape(input);

        // then
        assertEquals(actual, expected);
    }
}
