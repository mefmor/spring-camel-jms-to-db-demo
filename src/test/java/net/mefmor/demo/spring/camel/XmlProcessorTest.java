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
    private ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    private MockEndpoint mock;

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
        mock.expectedBodiesReceived(new PurchaseOrder("Camel in Action", 6999.0, 1.0));

        template.sendBody("<purchaseOrder name=\"Camel in Action\" price=\"6999\" amount=\"1\"/>");

        mock.assertIsSatisfied();
    }
}
