package net.mefmor.demo.spring.camel;

import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@DirtiesContext
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class XmlProcessorTest {

    @Autowired
    private CamelContext context;

    @Produce(uri = "direct:routeStart")
    private ProducerTemplate start;

    @EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;

    @Before
    public void setupRoute() throws Exception {

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:routeStart")
                        .unmarshal().jaxb("net.mefmor.demo.spring.camel.model")
                        .to("mock:result");
            }
        });
    }

    @Test
    public void xmlShouldBeUnmarshalToObject() throws InterruptedException {
        resultEndpoint.expectedMessageCount(1);


        start.sendBody("<purchaseOrder name=\"Camel in Action\" price=\"6999\" amount=\"1\"/>");
        resultEndpoint.assertIsSatisfied();
        final PurchaseOrder actualResult = resultEndpoint.getExchanges().get(0).getIn().getBody(PurchaseOrder.class);


        assertThat(actualResult.getName(), is("Camel in Action"));
        assertThat(actualResult.getPrice(), is(6999.0));
        assertThat(actualResult.getAmount(), is(1.0));
    }
}
