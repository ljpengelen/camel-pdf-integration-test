package nl.kabisa.scratch.pdf2txt;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class Pdf2TxtComponentIntegrationTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:out")
    private MockEndpoint resultEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:in")
                        .to("pdf:extractText")
                        .to("mock:out");
            }
        };
    }

    @Test
    public void loadFile() throws Exception {
        resultEndpoint.expectedMessageCount(1);

        template.sendBody("direct:in", getClass().getResource("/test.pdf"));
        resultEndpoint.assertIsSatisfied();

        String actual = resultEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assert.assertEquals("Test\n", actual);
    }
}
